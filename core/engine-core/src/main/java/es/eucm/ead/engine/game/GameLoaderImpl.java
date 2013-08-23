/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.engine.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.game.interfaces.GameLoader;
import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.engine.gameobjects.events.EventGO;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.EAdEvent;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.reader2.AdventureReader;
import es.eucm.ead.reader2.model.Manifest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class GameLoaderImpl implements GameLoader {
	private Game game;
	private AdventureReader reader;
	private GameState gameState;
	private EventGOFactory eventFactory;
	private SceneElementGOFactory sceneElementFactory;

	private Manifest currentManifest;

	private EAdChapter currentChapter;

	private Map<String, EAdChapter> chapters;

	private Map<String, EAdScene> scenes;
	private List<EventGO<?>> adventureEvents;
	private List<EventGO<?>> chapterEvents;

	@Inject
	public GameLoaderImpl(AdventureReader reader, Game game,
			GameState gameState, EventGOFactory eventFactory,
			SceneElementGOFactory sceneElementFactory) {
		this.game = game;
		this.reader = reader;
		this.gameState = gameState;
		this.eventFactory = eventFactory;
		this.sceneElementFactory = sceneElementFactory;
		this.chapters = new HashMap<String, EAdChapter>();
		this.scenes = new HashMap<String, EAdScene>();
		this.adventureEvents = new ArrayList<EventGO<?>>();
		this.chapterEvents = new ArrayList<EventGO<?>>();
	}

	@Override
	public void loadGame() {
		currentManifest = reader.getManifest();
		EAdAdventureModel adventure = currentManifest.getModel();
		for (EAdEvent e : adventure.getEvents()) {
			EventGO<?> eventGO = eventFactory.get(e);
			eventGO.setParent(null);
			eventGO.initialize();
			adventureEvents.add(eventGO);
		}
		loadChapter(currentManifest.getInitialChapter());
		game.doHook(GameImpl.HOOK_AFTER_MODEL_READ);
	}

	public Manifest loadManifest() {
		if (currentManifest == null) {
			currentManifest = reader.getManifest();
		}
		return currentManifest;
	}

	@Override
	public void loadChapter(String chapterId) {
		currentChapter = chapters.get(chapterId);
		if (currentChapter == null) {
			currentChapter = reader.readChapter(chapterId);
			chapters.put(chapterId, currentChapter);
		}

		for (EAdEvent e : currentChapter.getEvents()) {
			EventGO<?> eventGO = eventFactory.get(e);
			eventGO.setParent(null);
			eventGO.initialize();
			chapterEvents.add(eventGO);
		}
		int i = 0;
		String initialScene = null;
		for (String id : currentManifest.getChapterIds()) {
			if (chapterId.equals(id)) {
				initialScene = currentManifest.getInitialScenesIds().get(i);
			}
			i++;
		}
		EAdScene scene = loadScene(initialScene);
		ChangeSceneEf changeScene = new ChangeSceneEf(scene);
		game.doHook(GameImpl.HOOK_AFTER_CHAPTER_READ);
		gameState.addEffect(changeScene);
	}

	@Override
	public EAdScene loadScene(String sceneId) {
		EAdScene scene = scenes.get(sceneId);
		if (scene == null) {
			scene = reader.readScene(sceneId);
			scenes.put(sceneId, scene);
		}
		return scene;
	}

	@Override
	public void act(float delta) {
		for (EventGO<?> e : adventureEvents) {
			e.act(delta);
		}
		for (EventGO<?> e : chapterEvents) {
			e.act(delta);
		}
	}
}

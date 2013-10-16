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

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.EAdEngine;
import es.eucm.ead.engine.factories.EffectFactory;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.tracking.GameTracker;
import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.reader.AdventureReader;
import es.eucm.ead.reader.model.Manifest;
import es.eucm.ead.tools.StringHandler;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class GameLoader {
	private EventFactory eventFactory;
	private EffectFactory effectFactory;
	private SceneElementFactory sceneElementFactory;
	private Game game;
	private StringHandler stringHandler;
	private GameTracker gameTracker;

	private AdventureReader reader;

	private Manifest currentManifest;

	private Map<String, EAdChapter> chapters;

	private Map<String, EAdScene> scenes;

	private EAdEngine engine;

	private String currentChapterId;

	@Inject
	public GameLoader(AdventureReader reader, Game game,
			EventFactory eventFactory, EffectFactory effectFactory,
			SceneElementFactory sceneElementFactory, EAdEngine engine,
			StringHandler stringHandler, GameTracker gameTracker) {
		this.engine = engine;
		this.game = game;
		this.reader = reader;
		this.chapters = new HashMap<String, EAdChapter>();
		this.scenes = new HashMap<String, EAdScene>();
		this.eventFactory = eventFactory;
		this.effectFactory = effectFactory;
		this.sceneElementFactory = sceneElementFactory;
		this.stringHandler = stringHandler;
		this.gameTracker = gameTracker;
	}

	@SuppressWarnings( { "all" })
	public void loadGame() {
		currentManifest = reader.getManifest();
		final EAdAdventureModel adventure = currentManifest.getModel();
		// Load plugins
		eventFactory.put(adventure
				.getVarInitialValue(BasicAdventureModel.EVENTS_BINDS));
		effectFactory.put(adventure
				.getVarInitialValue(BasicAdventureModel.EFFECTS_BINDS));
		sceneElementFactory.put(adventure
				.getVarInitialValue(BasicAdventureModel.SCENES_ELEMENT_BINDS));
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {

				// FIXME detect language
				stringHandler.setLanguage("");
				game.getGUI().reset();
				game.setAdventure(adventure);
				game.doHook(Game.HOOK_AFTER_MODEL_READ);
				int width = adventure
						.getVarInitialValue(BasicAdventureModel.GAME_WIDTH);
				int height = adventure
						.getVarInitialValue(BasicAdventureModel.GAME_HEIGHT);

				engine.addDebug(GameLoader.this);
				engine.setGameWidth(width);
				engine.setGameHeight(height);
				engine
						.resize(Gdx.graphics.getWidth(), Gdx.graphics
								.getHeight());
				loadChapter(currentManifest.getInitialChapter());
			}
		});
		gameTracker.startTracking(currentManifest.getModel());
	}

	public Manifest loadManifest() {
		if (currentManifest == null) {
			currentManifest = reader.getManifest();
		}
		return currentManifest;
	}

	public void loadChapter(String chapterId) {
		currentChapterId = chapterId;
		EAdChapter currentChapter = chapters.get(chapterId);
		if (currentChapter == null) {
			currentChapter = reader.readChapter(chapterId);
			chapters.put(chapterId, currentChapter);
		}
		game.setChapter(currentChapter);
		int i = 0;
		String initialScene = null;
		for (String id : currentManifest.getChapterIds()) {
			if (chapterId.equals(id)) {
				initialScene = currentManifest.getInitialScenes().get(i);
			}
			i++;
		}
		EAdScene scene = loadScene(initialScene);
		final ChangeSceneEf changeScene = new ChangeSceneEf(scene);
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				game.doHook(Game.HOOK_AFTER_CHAPTER_READ);
				game.addEffect(changeScene);
			}
		});
	}

	public EAdScene loadScene(String sceneId) {
		EAdScene scene = scenes.get(sceneId);
		if (scene == null) {
			scene = reader.readScene(sceneId);
			scenes.put(sceneId, scene);
		}
		return scene;
	}

	public String getCurrentChapterId() {
		return currentChapterId;
	}

	public EAdEngine getEngine() {
		return engine;
	}
}

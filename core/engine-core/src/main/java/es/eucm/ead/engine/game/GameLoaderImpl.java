package es.eucm.ead.engine.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.game.interfaces.GameLoader;
import es.eucm.ead.engine.gameobjects.events.EventGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.EAdEvent;
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
	private GUI gameState;
	private EventGOFactory eventFactory;
	private SceneElementGOFactory sceneElementFactory;

	private Manifest currentManifest;

	private EAdChapter currentChapter;

	private Map<String, EAdChapter> chapters;

	private Map<String, EAdScene> scenes;
	private List<EventGO<?>> adventureEvents;
	private List<EventGO<?>> chapterEvents;

	@Inject
	public GameLoaderImpl(AdventureReader reader, Game game, GUI gui,
			EventGOFactory eventFactory,
			SceneElementGOFactory sceneElementFactory) {
		this.game = game;
		this.reader = reader;
		this.gameState = gui;
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
		loadScene(initialScene);
		game.doHook(GameImpl.HOOK_AFTER_CHAPTER_READ);
	}

	@Override
	public EAdScene loadScene(String sceneId) {
		EAdScene scene = scenes.get(sceneId);
		if (scene == null) {
			scene = reader.readScene(sceneId);
			scenes.put(sceneId, scene);
		}
		gameState.setScene((SceneGO) sceneElementFactory.get(scene));
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

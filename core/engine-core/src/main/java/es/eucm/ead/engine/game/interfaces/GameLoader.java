package es.eucm.ead.engine.game.interfaces;

import es.eucm.ead.model.elements.scenes.EAdScene;

public interface GameLoader {

	void loadGame();

	void loadChapter(String chapterId);

	EAdScene loadScene(String sceneId);

	void act(float delta);
}

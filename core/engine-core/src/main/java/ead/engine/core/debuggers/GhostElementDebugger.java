package ead.engine.core.debuggers;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.scenes.EAdGhostElement;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;

public class GhostElementDebugger implements Debugger {

	private GameState gameState;

	private EAdScene currentScene;

	private List<DrawableGO<?>> drawables;

	private SceneElementGOFactory factory;

	@Inject
	public GhostElementDebugger(GameState gameState,
			SceneElementGOFactory factory) {
		this.gameState = gameState;
		this.factory = factory;
		drawables = new ArrayList<DrawableGO<?>>();
	}

	@Override
	public List<DrawableGO<?>> getGameObjects() {
		EAdScene newScene = gameState.getScene().getElement();
		if (currentScene != newScene) {
			currentScene = newScene;
			drawables.clear();
			for (EAdSceneElement e : currentScene.getSceneElements()) {
				if (e instanceof EAdGhostElement) {
					SceneElement area = new SceneElement(
							((EAdGhostElement) e).getInteractionArea());
					area.setInitialEnable(false);
					DrawableGO<?> go = factory.get(e);
					area.setPosition(new EAdPosition(go.getPosition()));
					drawables.add(factory.get(area));
				}
			}
		}
		return drawables;
	}

	@Override
	public void setUp(EAdAdventureModel model) {

	}

}

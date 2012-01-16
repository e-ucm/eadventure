package ead.engine.core.gameobjects.transitions;

import com.google.inject.Inject;

import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.resources.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;

public class BasicTransitionGO extends AbstractTransitionGO<EmptyTransition> {

	@Inject
	public BasicTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			SceneLoader sceneLoader) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				eventFactory, sceneLoader );
	}
	




}

package ead.engine.core.gameobjects.go;

import com.google.inject.Inject;

import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class BasicComplexSceneElementGO extends
		ComplexSceneElementGOImpl<ComplexSceneElement> {

	@Inject
	public BasicComplexSceneElementGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
	}

}

package ead.engine.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GdxCanvas;
import ead.engine.core.platform.GdxGUI;
@Singleton
public class AndroidGdxGUI extends GdxGUI {

	@Inject
	public AndroidGdxGUI(EngineConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, InputHandler inputHandler,
			GameState gameState, SceneElementGOFactory gameObjectFactory,
			GdxCanvas canvas, EAdEngine engine) {
		super(platformConfiguration, gameObjectManager, inputHandler, gameState,
				gameObjectFactory, canvas, engine);
	}

	@Override
	public void showSpecialResource(Object object, int x, int y,
			boolean fullscreen) {
		
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void finish() {
		
	}

}

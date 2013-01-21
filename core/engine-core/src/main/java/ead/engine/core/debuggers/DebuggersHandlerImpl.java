package ead.engine.core.debuggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.scenes.SceneElement;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

@Singleton
public class DebuggersHandlerImpl implements DebuggersHandler {

	private static final Logger logger = LoggerFactory
			.getLogger("DebuggersHandler");

	public static final String TRAJECTORY_DEBUGGER = "trajectory_debugger";

	private TrajectoryDebugger trajectoryDebugger;

	private SceneElementGO<?> debuggersHud;

	private AssetHandler assetHandler;
	private SceneElementGOFactory sceneElementFactory;
	private GUI gui;
	private GameState gameState;
	private EventGOFactory eventFactory;

	@Inject
	public DebuggersHandlerImpl(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		this.assetHandler = assetHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;
		this.gameState = gameState;
		this.eventFactory = eventFactory;
	}

	@Override
	public void toggleDebugger(String debuggerId) {

		logger.info("{} toggled", debuggerId);

		if (debuggersHud == null) {
			debuggersHud = gui.getHUD(GUI.DEBBUGERS_HUD_ID);
		}

		if (debuggerId.equals(TRAJECTORY_DEBUGGER)) {
			if (trajectoryDebugger == null) {
				trajectoryDebugger = new TrajectoryDebugger(assetHandler,
						sceneElementFactory, gui, gameState, eventFactory);
				SceneElement e = new SceneElement();
				e.setId(TRAJECTORY_DEBUGGER);
				trajectoryDebugger.setElement(e);
			}

			if (debuggersHud.getChildren().contains(trajectoryDebugger)) {
				debuggersHud.removeSceneElement(trajectoryDebugger);
			} else {
				debuggersHud.addSceneElement(trajectoryDebugger);
			}
		}
	}

}

package ead.guitools.enginegui.effects.usetraces;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.elements.guievents.EAdGUIEvent;
import ead.common.model.elements.guievents.KeyGEv;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.effects.AbstractEffectGO;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.platform.GUI;

public class UseTracesEffectGO extends AbstractEffectGO<UseTracesEffect> {

	private int currentEvent;

	private InputHandler inputHandler;
	
	private int elapsed;

	private static final Logger logger = LoggerFactory.getLogger("UseTracesEffect");

	@Inject
	public UseTracesEffectGO(SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, InputHandler inputHandler) {
		super(gameObjectFactory, gui, gameState);
		this.inputHandler = inputHandler;
	}

	public void initialize() {
		super.initialize();
		currentEvent = 0;
		elapsed = 0;
		logger.info("Using automatic traces.");
	}

	public void update() {
		super.update();
		elapsed += gui.getSkippedMilliseconds();
		while ( currentEvent < element.getTimestamps().size() &&
				elapsed >= element.getTimestamps().get(currentEvent) ){
			InputAction<?> inputAction = getInputAction(element
					.getInputEvents().get(currentEvent), element.getPositions()
					.get(currentEvent));
			if (inputAction != null) {
				inputHandler.addAction(inputAction);
				logger.info("Send " + inputAction);
			}
			elapsed -= element.getTimestamps().get(currentEvent);
			currentEvent++;			
		}
	}

	private InputAction<?> getInputAction(EAdGUIEvent event,
			EAdPosition position) {
		if (event instanceof MouseGEv) {
			return new MouseInputAction((MouseGEv) event, position.getX(),
					position.getY());
		} else if (event instanceof KeyGEv) {
			return new KeyInputAction((KeyGEv) event, new Character(
					(char) position.getX()));
		}
		return null;
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}
	
	public void finish(){
		super.finish();
		logger.info("All traces were consumed.");
	}

	@Override
	public boolean isFinished() {
		return currentEvent >= element.getTimestamps().size() - 1;
	}

}

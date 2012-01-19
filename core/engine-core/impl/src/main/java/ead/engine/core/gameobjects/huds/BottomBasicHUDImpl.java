package ead.engine.core.gameobjects.huds;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.game.GameState;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;

@Singleton
public class BottomBasicHUDImpl extends AbstractHUD implements BottomBasicHUD {
	
	private GameState gameState;

	@Inject
	public BottomBasicHUDImpl(GUI gui, GameState gameState) {
		super(gui);
		this.gameState = gameState;
	}
	
	@Override
	public boolean processAction(InputAction<?> action) {		
		if ( gameState.getValueMap().getValue(SystemFields.BASIC_HUD_OPAQUE)){
			action.consume();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean contains(int x, int y) {
		return gameState.getValueMap().getValue(SystemFields.BASIC_HUD_OPAQUE);
	}

}

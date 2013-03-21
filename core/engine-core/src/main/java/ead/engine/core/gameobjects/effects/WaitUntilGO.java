package ead.engine.core.gameobjects.effects;

import com.google.inject.Inject;

import ead.common.model.elements.effects.WaitUntilEf;
import ead.engine.core.game.interfaces.GameState;

public class WaitUntilGO extends AbstractEffectGO<WaitUntilEf> {

	@Inject
	public WaitUntilGO(GameState gameState) {
		super(gameState);
	}

	public boolean isFinished() {
		return gameState.evaluate(effect.getWaitCondition());
	}

	@Override
	public boolean isQueueable() {
		return true;
	}

}

package ead.engine.core.gameobjects.effects;

import com.google.inject.Inject;

import ead.common.model.elements.effects.EmptyEffect;
import ead.engine.core.game.interfaces.GameState;

public class EmptyEffectGO extends AbstractEffectGO<EmptyEffect> {

	@Inject
	public EmptyEffectGO(GameState gameState) {
		super(gameState);
	}

	public void setElement(EmptyEffect e) {
		super.setElement(e);
	}

}

package ead.plugins.engine.drag15;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.google.inject.Inject;
import com.gwtent.reflection.client.Reflectable;

import ead.common.model.elements.EAdEffect;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.effects.AbstractEffectGO;

@Reflectable
public class Drag15GO extends AbstractEffectGO<Drag15Ef> {

	private GUI gui;

	@Inject
	public Drag15GO(GameState gameState, GUI gui) {
		super(gameState);
		this.gui = gui;
	}

	public void initialize() {
		super.initialize();
		if (action instanceof InputEvent) {
			InputEvent ie = (InputEvent) action;
			Actor dragZone = gui.getScene().findActor(effect.getZoneId());
			if (dragZone != null
					&& dragZone.hit(ie.getStageX(), ie.getStageY(), true) != null) {
				for (EAdEffect e : effect.getDropEffects()) {
					gameState.addEffect(e);
				}
			}
		}
	}

}

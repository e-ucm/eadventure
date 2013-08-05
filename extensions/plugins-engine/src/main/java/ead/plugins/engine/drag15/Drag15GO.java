package ead.plugins.engine.drag15;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.google.inject.Inject;
import com.gwtent.reflection.client.Reflectable;

import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.engine.gameobjects.effects.AbstractEffectGO;

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

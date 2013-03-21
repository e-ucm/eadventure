package ead.engine.core.gameobjects.events;

import com.google.inject.Inject;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.events.WatchFieldEv;
import ead.common.model.elements.events.enums.WatchFieldEvType;
import ead.engine.core.game.interfaces.GameState;

public class WatchFieldEvGO extends AbstractEventGO<WatchFieldEv> implements
		GameState.FieldWatcher {

	private boolean fieldUpdated;

	@Inject
	public WatchFieldEvGO(GameState gameState) {
		super(gameState);
	}

	public void setElement(WatchFieldEv ev) {
		super.setElement(ev);
		fieldUpdated = true;
	}

	@Override
	public void act(float delta) {
		if (fieldUpdated) {
			fieldUpdated = false;
			for (EAdEffect e : element
					.getEffectsForEvent(WatchFieldEvType.WATCH)) {
				gameState.addEffect(e);
			}
		}
	}

	@Override
	public void fieldUpdated() {
		fieldUpdated = true;
	}

	public void release() {
		gameState.removeFieldWatcher(this);
	}

}

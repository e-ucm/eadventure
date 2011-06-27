package es.eucm.eadventure.engine.core.gameobjects.impl.events;

import es.eucm.eadventure.common.model.events.EAdSceneElementTimedEvent;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;

public class SceneElementTimedEventGO extends
		AbstractEventGO<EAdSceneElementTimedEvent> {

	private int elapsedTime;

	private int repeats;

	@Override
	public void initialize() {
		super.initialize();
		elapsedTime = 0;
		repeats = element.getRepeats();
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		if (repeats != 0) {
			if (elapsedTime == 0) {
				this.runEffects(element
						.getEffects(EAdSceneElementTimedEvent.SceneElementTimedEventType.START_TIME));
			}
			elapsedTime += GameLoop.SKIP_MILLIS_TICK;
			if (elapsedTime > element.getTime()) {
				this.runEffects(element
						.getEffects(EAdSceneElementTimedEvent.SceneElementTimedEventType.END_TIME));
				elapsedTime = 0;
				repeats--;
			}
		}
	}
}

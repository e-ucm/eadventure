package es.eucm.eadventure.engine.core.gameobjects.impl.events;

import es.eucm.eadventure.common.model.events.EAdTimerEvent;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.TimerGO;

public class TimerEventGO extends AbstractEventGO<EAdTimerEvent> {

	private boolean triggered;
	
	@Override
	public void update(GameState state) {
		super.update(state);
		TimerGO timer = (TimerGO) gameObjectFactory.get(element.getTimer());
		if (valueMap.getValue(timer.getElement().timerEndedVar())) {
			if (!triggered)
				runEffects(element.getEffects(EAdTimerEvent.TimerEvent.TIMER_ENDED));
			triggered = true;
		} else if (triggered == true){
				triggered = false;
				runEffects(element.getEffects(EAdTimerEvent.TimerEvent.TIMER_RESTARTED));
		}

	
	}
}

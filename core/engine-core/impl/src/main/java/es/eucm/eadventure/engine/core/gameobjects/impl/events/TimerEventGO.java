package es.eucm.eadventure.engine.core.gameobjects.impl.events;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.events.EAdTimerEvent;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.TimerGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class TimerEventGO extends AbstractEventGO<EAdTimerEvent> {

	private boolean triggered;

	@Inject
	public TimerEventGO(AssetHandler assetHandler, StringHandler stringHandler,
			GameObjectFactory gameObjectFactory, GUI gui, GameState gameState,
			ValueMap valueMap, PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}
	
	@Override
	public void update(GameState state) {
		super.update(state);
		TimerGO timer = (TimerGO) gameObjectFactory.get(element.getTimer());
		if (valueMap.getValue(timer.getElement().timerEndedVar())) {
			if (!triggered) {
				//TODO this should only run if it does not restart
				runEffects(element.getEffects(EAdTimerEvent.TimerEvent.TIMER_STOPPED));
				runEffects(element.getEffects(EAdTimerEvent.TimerEvent.TIMER_ENDED));
			}
			triggered = true;
		} else if (triggered == true){
				triggered = false;
				runEffects(element.getEffects(EAdTimerEvent.TimerEvent.TIMER_RESTARTED));
		}

	
	}

}

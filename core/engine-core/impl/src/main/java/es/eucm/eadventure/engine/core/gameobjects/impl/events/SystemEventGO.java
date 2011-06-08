package es.eucm.eadventure.engine.core.gameobjects.impl.events;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.events.EAdSystemEvent;
import es.eucm.eadventure.common.model.events.EAdSystemEvent.Event;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.platform.AssetHandler;

public class SystemEventGO extends AbstractEventGO<EAdSystemEvent> {

	@Inject
	AssetHandler assetHandler;
	
	private boolean triggered = false;
	
	@Override
	public void update(GameState state) {
		super.update(state);
		//TODO probably not enough to just check for assets loaded
		if (assetHandler.isLoaded() && !triggered) {
			runEffects(element.getEffects(Event.GAME_LOADED));
			triggered = true;
		} 
	}


}

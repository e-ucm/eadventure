package es.eucm.eadventure.engine.core.gameobjects.impl.events;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.events.EAdSystemEvent;
import es.eucm.eadventure.common.model.events.EAdSystemEvent.Event;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class SystemEventGO extends AbstractEventGO<EAdSystemEvent> {

	private boolean triggered = false;

	@Inject
	public SystemEventGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

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

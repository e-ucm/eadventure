package es.eucm.eadventure.common.elmentfactories.events;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;

public class EventsFactory {
	
	private static int ID_GENERATOR = 0;
	
	public EAdSceneElementEvent getEvent( EAdSceneElementEvent.SceneElementEvent type, EAdEffect effect ){
		EAdSceneElementEvent event = new EAdSceneElementEventImpl( "scenElementEvent" + ID_GENERATOR++ );
		event.addEffect(type, effect);
		return event;
	}

}

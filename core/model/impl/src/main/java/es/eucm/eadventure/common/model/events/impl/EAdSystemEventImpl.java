package es.eucm.eadventure.common.model.events.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.events.EAdSystemEvent;

@Element(runtime=EAdSystemEventImpl.class, detailed=EAdSystemEventImpl.class)
public class EAdSystemEventImpl extends AbstractEAdEvent implements EAdSystemEvent {

	public EAdSystemEventImpl(String id) {
		super(id);
	}

}

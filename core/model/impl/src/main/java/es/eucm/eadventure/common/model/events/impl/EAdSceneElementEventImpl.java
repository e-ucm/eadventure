package es.eucm.eadventure.common.model.events.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;

@Element(runtime=EAdSceneElementEventImpl.class,  detailed=EAdSceneElementEventImpl.class)
public class EAdSceneElementEventImpl extends AbstractEAdEvent implements EAdSceneElementEvent {

	public EAdSceneElementEventImpl(String id) {
		super(id);
	}

}

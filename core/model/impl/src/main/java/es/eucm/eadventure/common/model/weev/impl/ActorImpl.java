package es.eucm.eadventure.common.model.weev.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.weev.Actor;

/**
 * Default {@link Actor} implementation
 */
@Element(detailed = ActorImpl.class, runtime = EAdSceneElementDefImpl.class)
public class ActorImpl extends EAdSceneElementDefImpl implements Actor {

	public ActorImpl(String id) {
		super(id);
	}

}

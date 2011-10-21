package es.eucm.eadventure.common.model.weev.world.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.weev.impl.AbstractTransition;
import es.eucm.eadventure.common.model.weev.world.Space;
import es.eucm.eadventure.common.model.weev.world.SpaceLink;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Default {@link SpaceLink} implementation
 */
@Element(detailed = SpaceLinkImpl.class, runtime = SpaceLinkImpl.class)
public class SpaceLinkImpl extends AbstractTransition<Space> implements SpaceLink {

	private EAdString name;
	
	public SpaceLinkImpl() {
		name = EAdString.newEAdString("name");
	}
	
	@Override
	public EAdString getName() {
		return name;
	}

}

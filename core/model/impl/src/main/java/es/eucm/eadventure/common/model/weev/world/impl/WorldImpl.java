package es.eucm.eadventure.common.model.weev.world.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.impl.AbstractWEEVElement;
import es.eucm.eadventure.common.model.weev.world.ActorPlacement;
import es.eucm.eadventure.common.model.weev.world.Space;
import es.eucm.eadventure.common.model.weev.world.SpaceLink;
import es.eucm.eadventure.common.model.weev.world.World;

/**
 * Default implementation of the @{link World} interface
 */
@Element(detailed = WorldImpl.class, runtime = WorldImpl.class)
public class WorldImpl extends AbstractWEEVElement implements World {

	private EAdList<Space> spaces;
	
	private EAdList<SpaceLink> spaceLinks;
	
	private EAdList<ActorPlacement> actorPlacements;
	
	@Param(value = "initialSpace")
	private Space initialSpace;
	
	public WorldImpl() {
		spaces = new EAdListImpl<Space>(Space.class);
		spaceLinks = new EAdListImpl<SpaceLink>(SpaceLink.class);
		actorPlacements = new EAdListImpl<ActorPlacement>(ActorPlacement.class);
	}
	
	@Override
	public EAdList<Space> getSpaces() {
		return spaces;
	}

	@Override
	public EAdList<SpaceLink> getSpaceLinks() {
		return spaceLinks;
	}

	@Override
	public EAdList<ActorPlacement> getActorPlacement() {
		return actorPlacements;
	}

	@Override
	public Space getInitialSpace() {
		return initialSpace;
	}

	public void setInitialSpace(Space initialSpace) {
		this.initialSpace = initialSpace;
	}
}

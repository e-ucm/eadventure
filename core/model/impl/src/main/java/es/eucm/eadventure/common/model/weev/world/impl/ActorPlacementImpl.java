package es.eucm.eadventure.common.model.weev.world.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.Actor;
import es.eucm.eadventure.common.model.weev.world.ActorPlacement;
import es.eucm.eadventure.common.model.weev.world.Space;

/**
 * Default {@link ActorPlacement} implementation
 */
@Element(detailed = ActorPlacementImpl.class, runtime = ActorPlacementImpl.class)
public class ActorPlacementImpl extends AbstractWorldElement implements ActorPlacement {

	@Param(value = "space")
	private Space space;

	@Param(value = "actor")
	private Actor actor;
	
	@Override
	public Space getSpace() {
		return space;
	}
	
	public void setSpace(Space space) {
		this.space = space;
	}

	@Override
	public Actor getActor() {
		return actor;
	}
	
	public void setActor(Actor actor) {
		this.actor = actor;
	}

}

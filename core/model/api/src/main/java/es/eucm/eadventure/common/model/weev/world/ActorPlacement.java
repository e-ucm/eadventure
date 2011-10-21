package es.eucm.eadventure.common.model.weev.world;

import es.eucm.eadventure.common.model.weev.Actor;
import es.eucm.eadventure.common.model.weev.common.Positioned;

/**
 * Represents the placement of an {@link Actor} in a {@link Space}
 * 
 */
public interface ActorPlacement extends WorldElement, Positioned {

	/**
	 * @return the {@link Space} where the actor is placed
	 */
	Space getSpace();
	
	/**
	 * @return the {@link Actor} that is placed by this placement
	 */
	Actor getActor();

}

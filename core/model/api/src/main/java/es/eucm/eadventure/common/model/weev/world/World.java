package es.eucm.eadventure.common.model.weev.world;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.weev.WEEVElement;

/**
 * The World element in the {@link WEEVModel}.
 * <p>
 * The world contains the spaces, the links between spaces and the placement for
 * actors within those spaces
 */
public interface World extends WEEVElement {

	/**
	 * @return the list of {@link Space}s that make up the world
	 */
	EAdList<Space> getSpaces();

	/**
	 * @return the list of {@link SpaceLink}s in the world
	 */
	EAdList<SpaceLink> getSpaceLinks();

	/**
	 * @return the list of {@link ActorPlacement}s that stores where actors are
	 *         placed in the world
	 */
	EAdList<ActorPlacement> getActorPlacement();

	/**
	 * @return the initial space of the story
	 */
	Space getInitialSpace();
}

package es.eucm.eadventure.common.model.weev.world;

import es.eucm.eadventure.common.model.weev.common.Positioned;
import es.eucm.eadventure.common.params.EAdString;

/**
 * A link between two different {@link Space}s, which has a name and orientation
 */
public interface SpaceLink extends WorldElement, Positioned {
	
	/**
	 * @return the {@Space} where the link comes from
	 */
	Space getStartSpace();
	
	/**
	 * @return the {@Space} where the link takes to
	 */
	Space getEndSpace();
	
	/**
	 * @return the name of the link
	 */
	EAdString getName();
	
}

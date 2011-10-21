package es.eucm.eadventure.common.model.weev.world;

import es.eucm.eadventure.common.model.weev.Transition;
import es.eucm.eadventure.common.params.EAdString;

/**
 * A link between two different {@link Space}s, which has a name and orientation
 */
public interface SpaceLink extends WorldElement, Transition<Space> {
	
	/**
	 * @return the name of the link
	 */
	EAdString getName();
	
}

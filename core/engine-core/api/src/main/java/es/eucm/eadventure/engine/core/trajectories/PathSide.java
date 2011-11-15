package es.eucm.eadventure.engine.core.trajectories;

import es.eucm.eadventure.common.params.geom.EAdPosition;

/**
 * A single side in a trajectory path
 */
public interface PathSide {

	/**
	 * @return the length of the side
	 */
	float getLength();

	/**
	 * @return the speed factor to be used in the side
	 */
	float getSpeedFactor();

	/**
	 * @param last
	 *            true if is the last side in the path
	 * @return the position of end of the side
	 */
	EAdPosition getEndPosition(boolean last);

}

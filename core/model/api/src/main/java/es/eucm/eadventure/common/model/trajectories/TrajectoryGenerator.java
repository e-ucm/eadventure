package es.eucm.eadventure.common.model.trajectories;

import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.params.EAdPosition;

/**
 * General interface for trajectory generator. These generators are usually used
 * to move the character around the scene
 * 
 * 
 */
public interface TrajectoryGenerator {

	/**
	 * Returns a list of points representing the trajectory to go to the given x
	 * and y coordinates
	 * 
	 * @param currentPosition
	 *            the start position for the trajectory
	 * 
	 * @param x
	 *            target x coordinate
	 * @param y
	 *            target y coordinate
	 * @return the list of points
	 */
	EAdList<EAdPosition> getTrajectory(EAdPosition currentPosition, int x, int y);

}

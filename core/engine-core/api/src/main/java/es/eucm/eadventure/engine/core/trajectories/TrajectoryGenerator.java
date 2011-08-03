package es.eucm.eadventure.engine.core.trajectories;

import java.util.List;

import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.EAdPosition;

/**
 * General interface for trajectories generators. Trajectories are build from a
 * {@link TrajectoryDefinition}
 * 
 * 
 * @param <T>
 *            the class of the trajectory definition
 */
public interface TrajectoryGenerator<T extends TrajectoryDefinition> {
	
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
	List<EAdPosition> getTrajectory(T trajectoryDefintion, EAdPosition currentPosition, int x, int y);

}

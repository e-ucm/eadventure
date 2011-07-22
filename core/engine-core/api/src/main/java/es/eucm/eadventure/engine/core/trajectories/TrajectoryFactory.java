package es.eucm.eadventure.engine.core.trajectories;

import java.util.List;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;

/**
 * A factory with all {@link TrajectoryGenerator} for all
 * {@link TrajectoryDefinition}.
 */
public interface TrajectoryFactory {

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
	<T extends TrajectoryDefinition> List<EAdPosition> getTrajectory(
			T trajectoryDefintion, EAdPosition currentPosition, int x, int y);

}

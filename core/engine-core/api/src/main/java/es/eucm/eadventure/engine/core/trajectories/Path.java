package es.eucm.eadventure.engine.core.trajectories;

import java.util.List;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;

/**
 * A path or section in a trajectory
 */
public interface Path {

	/**
	 * @return Get the {@link PathSide}s in the trajectory
	 */
	List<PathSide> getSides();

	/**
	 * @param p
	 *            The {@link PathSide}
	 * @param trajectory
	 *            The {@link TrajectoryDefinition} where the trajectory is
	 *            defined
	 * @return The effect associated with the given path in the given trajectory
	 */
	EAdEffect getChangeSideEffect(PathSide p, TrajectoryDefinition trajectory);

}

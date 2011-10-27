package es.eucm.eadventure.engine.core.trajectories;

import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;

/**
 * A factory with all {@link TrajectoryGenerator} for all
 * {@link TrajectoryDefinition}.
 */
public interface TrajectoryFactory extends
		TrajectoryGenerator<TrajectoryDefinition> {

}

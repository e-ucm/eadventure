package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.interfaces.AbstractFactory;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

public class TrajectoryFactoryImpl extends
		AbstractFactory<TrajectoryGenerator<?>> implements TrajectoryFactory {

	@Inject
	public TrajectoryFactoryImpl(
			MapProvider<Class<?>, TrajectoryGenerator<?>> mapProvider) {
		super(mapProvider);
	}

	@Override
	public <T extends TrajectoryDefinition> List<EAdPosition> getTrajectory(
			T trajectoryDefinition, EAdPosition currentPosition, int x, int y) {

		@SuppressWarnings("unchecked")
		TrajectoryGenerator<T> generator = (TrajectoryGenerator<T>) this
				.get(trajectoryDefinition.getClass());

		return generator.getTrajectory(trajectoryDefinition, currentPosition,
				x, y);
	}
}

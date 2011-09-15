package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.interfaces.AbstractFactory;
import es.eucm.eadventure.common.interfaces.InterfacesProvider;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

public class TrajectoryFactoryImpl extends
		AbstractFactory<TrajectoryGenerator<?>> implements TrajectoryFactory {

	@Inject
	public TrajectoryFactoryImpl(
			MapProvider<Class<?>, TrajectoryGenerator<?>> mapProvider,
			InterfacesProvider interfacesProvider) {
		super(mapProvider, interfacesProvider);
	}

	@Override
	public List<EAdPosition> getTrajectory(
			TrajectoryDefinition trajectoryDefinition, EAdPosition currentPosition, int x, int y) {

		@SuppressWarnings("unchecked")
		TrajectoryGenerator<TrajectoryDefinition> generator = (TrajectoryGenerator<TrajectoryDefinition>) this
				.get(trajectoryDefinition.getClass());

		return generator.getTrajectory(trajectoryDefinition, currentPosition,
				x, y);
	}

	@Override
	public List<EAdPosition> getTrajectory(
			TrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int x, int y, SceneElementGO<?> sceneElement) {

		@SuppressWarnings("unchecked")
		TrajectoryGenerator<TrajectoryDefinition> generator = (TrajectoryGenerator<TrajectoryDefinition>) this
				.get(trajectoryDefinition.getClass());

		return generator.getTrajectory(trajectoryDefinition, currentPosition,
				x, y, sceneElement);

	}

	@Override
	public boolean canGetTo(TrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, SceneElementGO<?> sceneElement) {
		// TODO Auto-generated method stub
		return false;
	}
}

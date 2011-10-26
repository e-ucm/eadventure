package es.eucm.eadventure.engine.core.impl.modules;

import com.google.inject.AbstractModule;

import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;
import es.eucm.eadventure.engine.core.trajectories.impl.TrajectoryFactoryImpl;

public class TrajectoryFactoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind( TrajectoryFactory.class).to(TrajectoryFactoryImpl.class);
	}

}

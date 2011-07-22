package es.eucm.eadventure.engine.core.impl.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.TrajectoryFactoryMapProvider;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;
import es.eucm.eadventure.engine.core.trajectories.impl.TrajectoryFactoryImpl;

public class TrajectoryFactoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<MapProvider<Class<?>, TrajectoryGenerator<?>>>() {}).to(TrajectoryFactoryMapProvider.class);
		bind( TrajectoryFactory.class).to(TrajectoryFactoryImpl.class);
	}

}

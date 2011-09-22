package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;
import es.eucm.eadventure.engine.core.trajectories.impl.NodeTrajectoryGenerator;
import es.eucm.eadventure.engine.core.trajectories.impl.SimpleTrajectoryGenerator;

@Singleton
public class TrajectoryFactoryMapProvider extends AbstractMapProvider<Class<?>, TrajectoryGenerator<?>> {

	private static Map<Class<?>, TrajectoryGenerator<?>> tempMap = new HashMap<Class<?>, TrajectoryGenerator<?>>();

	@Inject
	public TrajectoryFactoryMapProvider(TrajectoryFactory trajectoryFactory,
			GameObjectFactory gameObjectFactory,
			ValueMap valueMap) {
		super();
		factoryMap.put(SimpleTrajectoryDefinition.class, new SimpleTrajectoryGenerator());
		factoryMap.put(NodeTrajectoryDefinition.class, new NodeTrajectoryGenerator(gameObjectFactory, valueMap));
		factoryMap.putAll(tempMap);
	}
	
	public static void add(Class<?> trjaectoryDefinition, TrajectoryGenerator<?> trajectoryGenerator) {
		tempMap.put(trjaectoryDefinition, trajectoryGenerator);
	}

}

package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.HashMap;
import java.util.Map;

import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;
import es.eucm.eadventure.engine.core.trajectories.impl.NodeTrajectoryGenerator;
import es.eucm.eadventure.engine.core.trajectories.impl.SimpleTrajectoryGenerator;
import es.eucm.eadventure.engine.core.trajectories.impl.dijkstra.DijkstraNodeTrajectoryGenerator;

public class TrajectoryFactoryMapProvider extends AbstractMapProvider<Class<?>, TrajectoryGenerator<?>> {

	private static Map<Class<?>, TrajectoryGenerator<?>> tempMap = new HashMap<Class<?>, TrajectoryGenerator<?>>();
	
	private ValueMap valueMap;
	
	private SceneElementGOFactory sceneElementFactory;

	public TrajectoryFactoryMapProvider(TrajectoryFactory trajectoryFactory,
			SceneElementGOFactory gameObjectFactory,
			ValueMap valueMap) {
		super();
		this.valueMap = valueMap;
		this.sceneElementFactory = gameObjectFactory;

	}
	
	public Map<Class<?>, TrajectoryGenerator<?>> getMap(){
		factoryMap.put(SimpleTrajectoryDefinition.class, new SimpleTrajectoryGenerator());
//		factoryMap.put(NodeTrajectoryDefinition.class, new NodeTrajectoryGenerator(sceneElementFactory, valueMap));
		factoryMap.put(NodeTrajectoryDefinition.class, new DijkstraNodeTrajectoryGenerator(sceneElementFactory, valueMap));
		factoryMap.putAll(tempMap);
		factoryMap.putAll(tempMap);
		return super.getMap();
	}
	
	public static void add(Class<?> trjaectoryDefinition, TrajectoryGenerator<?> trajectoryGenerator) {
		tempMap.put(trjaectoryDefinition, trajectoryGenerator);
	}

}

package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.List;

import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

public class NodeTrajectoryGenerator implements
		TrajectoryGenerator<NodeTrajectoryDefinition> {

	@Override
	public List<EAdPosition> getTrajectory(
			NodeTrajectoryDefinition trajectoryDefintion,
			EAdPosition currentPosition, int x, int y) {

		return null;
	}

	@Override
	public List<EAdPosition> getTrajectory(
			NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, SceneElementGO<?> sceneElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canGetTo(NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, SceneElementGO<?> sceneElement) {
		// TODO Auto-generated method stub
		return false;
	}

}

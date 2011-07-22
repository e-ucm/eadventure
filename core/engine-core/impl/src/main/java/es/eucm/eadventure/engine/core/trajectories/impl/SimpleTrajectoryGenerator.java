package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

public class SimpleTrajectoryGenerator implements
		TrajectoryGenerator<SimpleTrajectoryDefinition> {

	@Override
	public List<EAdPosition> getTrajectory(
			SimpleTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int x, int y) {

		List<EAdPosition> list = new ArrayList<EAdPosition>();
		if (trajectoryDefinition.isOnlyHoriztonal()) {
			list.add(new EAdPosition(x, currentPosition.getY()));
		} else {
			list.add(new EAdPosition(x, y));
		}

		return list;

	}

}

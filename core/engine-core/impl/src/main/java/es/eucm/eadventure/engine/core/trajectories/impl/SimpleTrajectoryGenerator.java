package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

public class SimpleTrajectoryGenerator implements
		TrajectoryGenerator<SimpleTrajectoryDefinition> {

	@Override
	public Path getTrajectory(
			SimpleTrajectoryDefinition def,
			EAdPosition currentPosition, int x, int y) {

		List<EAdPosition> list = new ArrayList<EAdPosition>();
		if (def.isOnlyHoriztonal()) {
			list.add(new EAdPositionImpl(getX(def, x), currentPosition.getY()));
		} else {
			list.add(new EAdPositionImpl(getX(def, x), getY(def, y)));
		}

		return new SimplePathImpl(list, currentPosition);
	}

	@Override
	public Path getTrajectory(
			SimpleTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int x, int y, SceneElementGO<?> sceneElement) {

		List<EAdPosition> list = new ArrayList<EAdPosition>();
		if (trajectoryDefinition.isOnlyHoriztonal()) {
			list.add(new EAdPositionImpl(x, currentPosition.getY()));
		} else {
			list.add(new EAdPositionImpl(x, y));
		}

		return new SimplePathImpl(list,currentPosition);
	}

	@Override
	public boolean canGetTo(SimpleTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, SceneElementGO<?> sceneElement) {
		//TODO check barriers?
		return false;
	}
	
	private int getX( SimpleTrajectoryDefinition def, int x ){
		if ( x > def.right() )
			return def.right();
		if ( x < def.left() )
			return def.left();
		return x;
	}
	
	private int getY( SimpleTrajectoryDefinition def, int y ){
		if ( y > def.bottom() )
			return def.bottom();
		if ( y < def.top() )
			return def.top();
		return y;
	}

}

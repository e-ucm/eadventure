package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

public class SimpleTrajectoryGenerator implements
		TrajectoryGenerator<SimpleTrajectoryDefinition> {

	/**
	 * The values in the game
	 */
	private ValueMap valueMap;

	@Inject
	public SimpleTrajectoryGenerator(ValueMap valueMap) {
		this.valueMap = valueMap;
	}
	
	@Override
	public Path getTrajectory(
			SimpleTrajectoryDefinition def,
			EAdElement movingElement, int x, int y) {

		EAdPosition currentPosition = getCurrentPosition(movingElement);
		
		List<EAdPosition> list = new ArrayList<EAdPosition>();
		if (def.isOnlyHoriztonal()) {
			list.add(new EAdPositionImpl(getX(def, x), currentPosition.getY()));
		} else {
			list.add(new EAdPositionImpl(getX(def, x), getY(def, y)));
		}

		return new SimplePathImpl(list, currentPosition, valueMap.getValue(movingElement, EAdBasicSceneElement.VAR_SCALE));
	}

	@Override
	public Path getTrajectory(
			SimpleTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, int x, int y, SceneElementGO<?> sceneElement) {

		EAdPosition currentPosition = getCurrentPosition(movingElement);

		List<EAdPosition> list = new ArrayList<EAdPosition>();
		if (trajectoryDefinition.isOnlyHoriztonal()) {
			list.add(new EAdPositionImpl(x, currentPosition.getY()));
		} else {
			list.add(new EAdPositionImpl(x, y));
		}

		return new SimplePathImpl(list,currentPosition, valueMap.getValue(movingElement, EAdBasicSceneElement.VAR_SCALE));
	}

	@Override
	public boolean canGetTo(SimpleTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, SceneElementGO<?> sceneElement) {
		//TODO check barriers?
		return false;
	}
	
	private int getX( SimpleTrajectoryDefinition def, int x ){
		if ( x > def.getRight() )
			return def.getRight();
		if ( x < def.getLeft() )
			return def.getLeft();
		return x;
	}
	
	private int getY( SimpleTrajectoryDefinition def, int y ){
		if ( y > def.getBottom() )
			return def.getBottom();
		if ( y < def.getTop() )
			return def.getTop();
		return y;
	}
	
	private EAdPosition getCurrentPosition(EAdElement element) {
		int x = valueMap.getValue(element,
				EAdBasicSceneElement.VAR_X);
		int y = valueMap.getValue(element,
				EAdBasicSceneElement.VAR_Y);
		return new EAdPositionImpl(x, y);
	}

}

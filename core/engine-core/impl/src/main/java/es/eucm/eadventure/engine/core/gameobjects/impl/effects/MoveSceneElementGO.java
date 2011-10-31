/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.interfaces.features.Oriented.Orientation;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement.CommonStates;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.sceneelement.SceneElementEffectGO;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.PathSide;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;
import es.eucm.eadventure.engine.core.trajectories.impl.SimplePathImpl;
import es.eucm.eadventure.engine.core.util.impl.EAdInterpolator;

/**
 * Game object for {@link EAdMoveSceneElement} effect
 * 
 * 
 */
public class MoveSceneElementGO extends
		SceneElementEffectGO<EAdMoveSceneElement> {

	private static final EAdVarDef<MoveSceneElementGO> VAR_ELEMENT_MOVING = new EAdVarDefImpl<MoveSceneElementGO>(
			"element_moving", MoveSceneElementGO.class, null);

	/**
	 * Pixels traveled per second
	 */
	private static final int PIXELS_PER_SECOND = 250;

	private OperatorFactory operatorFactory;

	private TrajectoryFactory trajectoryFactory;

	private boolean finishedSide = false;

	private boolean finished = false;

	private String oldState;

	private boolean firstUpdate = true;

	private Integer initX;

	private Integer initY;

	private Integer targetX;

	private Integer targetY;

	private float totalTime;

	private Path path;

	private int currentSide;

	@Inject
	public MoveSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, OperatorFactory operatorFactory,
			TrajectoryFactory trajectoryFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
		this.operatorFactory = operatorFactory;
		this.trajectoryFactory = trajectoryFactory;
	}

	@Override
	public void initilize() {
		super.initilize();
		ValueMap valueMap = gameState.getValueMap();

		int x = valueMap.getValue(element.getSceneElement(),
				EAdBasicSceneElement.VAR_X);
		int y = valueMap.getValue(element.getSceneElement(),
				EAdBasicSceneElement.VAR_Y);

		int endX = operatorFactory.operate(Integer.class, element.getXTarget());
		int endY = operatorFactory.operate(Integer.class, element.getYTarget());

		EAdPositionImpl currentPosition = new EAdPositionImpl(x, y);

		TrajectoryDefinition d = valueMap.getValue(gameState.getScene()
				.getElement(), EAdSceneImpl.VAR_TRAJECTORY_DEFINITION);
		if (d != null && element.useTrajectory()) {
			path = trajectoryFactory.getTrajectory(d, currentPosition, endX,
					endY);
		} else {
			List<EAdPosition> list = new ArrayList<EAdPosition>();
			list.add(new EAdPositionImpl(endX, endY));
			path = new SimplePathImpl(list, currentPosition);
		}

		currentSide = 0;
		MoveSceneElementGO go = gameState.getValueMap().getValue(sceneElement,
				VAR_ELEMENT_MOVING);
		if (go != null) {
			go.stop();
		}
		gameState.getValueMap()
				.setValue(sceneElement, VAR_ELEMENT_MOVING, this);
		updateTarget();

	}

	private void updateTarget() {
		if (currentSide < path.getSides().size()) {
			PathSide side = path.getSides().get(currentSide);

			initX = gameState.getValueMap().getValue(element.getSceneElement(),
					EAdBasicSceneElement.VAR_X);
			initY = gameState.getValueMap().getValue(element.getSceneElement(),
					EAdBasicSceneElement.VAR_Y);

			EAdPosition p = side.getEndPosition(currentSide == path.getSides()
					.size() - 1);
			targetX = p.getX();
			targetY = p.getY();

			totalTime = (side.getLenght() / PIXELS_PER_SECOND * 1000)
					* side.getSpeedFactor();

			updateDirection();
			currentSide++;
			finishedSide = false;
		} else {
			finished = true;
		}
	}

	private void updateDirection() {

		float xv = targetX - initX;
		float yv = targetY - initY;
		double module = Math.sqrt(xv * xv + yv * yv);
		double angle = Math.acos(xv / module) * Math.signum(-yv);

		Orientation tempDirection = Orientation.W;
		
		if ( -0.00001f < angle && angle < 0.00001f ){
			tempDirection = initX > targetX ? Orientation.W : Orientation.E;
		}
		else if (angle < 3 * Math.PI / 4 && angle >= Math.PI / 4) {
			tempDirection = Orientation.N;
		} else if (angle < Math.PI / 4 && angle >= -Math.PI / 4) {
			tempDirection = Orientation.E;
		} else if (angle < -Math.PI / 4 && angle >= -3 * Math.PI / 4) {
			tempDirection = Orientation.S;
		}

		gameState.getValueMap().setValue(sceneElement,
				EAdBasicSceneElement.VAR_ORIENTATION, tempDirection);

	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	public void update() {
		if (!finished) {
			super.update();
			if (firstUpdate) {
				firstUpdate = false;
				oldState = gameState.getValueMap().getValue(
						element.getSceneElement(),
						EAdBasicSceneElement.VAR_STATE);

				gameState.getValueMap().setValue(element.getSceneElement(),
						EAdBasicSceneElement.VAR_STATE,
						CommonStates.EAD_STATE_WALKING.toString());
			}

			if (finishedSide) {
				updateTarget();
			}

			if (currentTime <= totalTime) {
				gameState.getValueMap().setValue(
						sceneElement,
						EAdBasicSceneElement.VAR_X,
						initX
								+ (int) EAdInterpolator.LINEAR
										.interpolate(currentTime, totalTime,
												targetX - initX));
				gameState.getValueMap().setValue(
						sceneElement,
						EAdBasicSceneElement.VAR_Y,
						initY
								+ (int) EAdInterpolator.LINEAR
										.interpolate(currentTime, totalTime,
												targetY - initY));
			} else {
				gameState.getValueMap().setValue(sceneElement,
						EAdBasicSceneElement.VAR_X, (int) targetX);
				gameState.getValueMap().setValue(sceneElement,
						EAdBasicSceneElement.VAR_Y, (int) targetY);
				finishedSide = true;
			}
		}
	}

	public void finish() {
		super.finish();
		gameState.getValueMap().setValue(element.getSceneElement(),
				EAdBasicSceneElement.VAR_STATE, oldState);
		gameState.getValueMap()
				.setValue(sceneElement, VAR_ELEMENT_MOVING, null);
	}

	public void stop() {
		super.stop();
		gameState.getValueMap().setValue(element.getSceneElement(),
				EAdBasicSceneElement.VAR_STATE, oldState);
	}

}

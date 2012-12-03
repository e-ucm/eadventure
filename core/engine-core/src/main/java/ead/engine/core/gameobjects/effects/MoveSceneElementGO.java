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

package ead.engine.core.gameobjects.effects;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.EAdTrajectoryDefinition;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.VarDef;
import ead.common.util.EAdPosition;
import ead.common.util.Interpolator;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.effects.sceneelement.SceneElementEffectGO;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.operators.OperatorFactory;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.trajectories.Path;
import ead.engine.core.trajectories.PathSide;
import ead.engine.core.trajectories.SimplePathImpl;
import ead.engine.core.trajectories.TrajectoryFactory;
import ead.engine.core.trajectories.dijkstra.DijkstraPathSide;

/**
 * Game object for {@link MoveSceneElementEf} effect
 * 
 * 
 */
public class MoveSceneElementGO extends
		SceneElementEffectGO<MoveSceneElementEf> {

	private static final EAdVarDef<MoveSceneElementGO> VAR_ELEMENT_MOVING = new VarDef<MoveSceneElementGO>(
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

	private Float initScale;

	private Integer targetX;

	private Integer targetY;

	private Float targetScale;

	private float totalTime;

	private Path path;

	private int currentSide;

	@Inject
	public MoveSceneElementGO(AssetHandler assetHandler,
			OperatorFactory operatorFactory,
			SceneElementGOFactory gameObjectFactory,
			TrajectoryFactory trajectoryFactory, GUI gui, GameState gameState) {
		super(assetHandler, gameObjectFactory, gui, gameState);
		this.operatorFactory = operatorFactory;
		this.trajectoryFactory = trajectoryFactory;
	}

	@Override
	public void initialize() {
		super.initialize();
		ValueMap valueMap = gameState.getValueMap();

		int endX = operatorFactory.operate(Integer.class, element.getxTarget());
		int endY = operatorFactory.operate(Integer.class, element.getyTarget());

		EAdSceneElement target = element.getTarget() != null ? valueMap
				.getValue(element.getTarget(),
						SceneElementDef.VAR_SCENE_ELEMENT) : null;

		EAdTrajectoryDefinition d = valueMap.getValue(gameState.getScene()
				.getElement(), BasicScene.VAR_TRAJECTORY_DEFINITION);
		if (d != null && element.isUseTrajectory()) {
			if (target == null)
				path = trajectoryFactory.getTrajectory(d, sceneElement, endX,
						endY);
			else
				path = trajectoryFactory.getTrajectory(d, sceneElement, endX,
						endY, sceneElementFactory.get(target));
		} else {
			List<EAdPosition> list = new ArrayList<EAdPosition>();
			list.add(new EAdPosition(endX, endY));
			int x = valueMap.getValue(sceneElement, SceneElement.VAR_X);
			int y = valueMap.getValue(sceneElement, SceneElement.VAR_Y);
			float scale = valueMap.getValue(sceneElement,
					SceneElement.VAR_SCALE);
			EAdPosition currentPosition = new EAdPosition(x, y);
			path = new SimplePathImpl(list, currentPosition, scale);
		}

		currentSide = 0;
		MoveSceneElementGO go = gameState.getValueMap().getValue(sceneElement,
				VAR_ELEMENT_MOVING);
		if (go != null) {
			go.stop();
		}
		gameState.getValueMap()
				.setValue(sceneElement, VAR_ELEMENT_MOVING, this);
		currentTime = 0;
		totalTime = 0;
		updateTarget();

	}

	private void updateTarget() {
		if (currentSide < path.getSides().size()) {
			PathSide side = path.getSides().get(currentSide);

			initX = gameState.getValueMap().getValue(sceneElement,
					SceneElement.VAR_X);
			initY = gameState.getValueMap().getValue(sceneElement,
					SceneElement.VAR_Y);
			initScale = gameState.getValueMap().getValue(sceneElement,
					SceneElement.VAR_SCALE);

			EAdPosition p = side.getEndPosition(currentSide == path.getSides()
					.size() - 1);
			targetX = p.getX();
			targetY = p.getY();
			targetScale = side.getEndScale();

			currentTime = (int) (currentTime - totalTime);

			totalTime = (side.getLength() / PIXELS_PER_SECOND * 1000)
					* side.getSpeedFactor();

			// TODO should be more generic...
			EAdTrajectoryDefinition d = gameState.getValueMap().getValue(
					gameState.getScene().getElement(),
					BasicScene.VAR_TRAJECTORY_DEFINITION);
			if (d != null && element.isUseTrajectory()
					&& side instanceof DijkstraPathSide) {
				gameState.getValueMap().setValue(sceneElement,
						NodeTrajectoryDefinition.VAR_CURRENT_SIDE,
						((DijkstraPathSide) side).getSide());
			}

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

		if (-0.00001f < angle && angle < 0.00001f) {
			tempDirection = initX > targetX ? Orientation.W : Orientation.E;
		} else if (angle < 3 * Math.PI / 4 && angle >= Math.PI / 4) {
			tempDirection = Orientation.N;
		} else if (angle < Math.PI / 4 && angle >= -Math.PI / 4) {
			tempDirection = Orientation.E;
		} else if (angle < -Math.PI / 4 && angle >= -3 * Math.PI / 4) {
			tempDirection = Orientation.S;
		}

		gameState.getValueMap().setValue(sceneElement,
				SceneElement.VAR_ORIENTATION, tempDirection);

	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void update() {
		if (!finished) {
			super.update();
			if (firstUpdate) {
				firstUpdate = false;
				oldState = gameState.getValueMap().getValue(sceneElement,
						SceneElement.VAR_STATE);

				gameState.getValueMap().setValue(sceneElement,
						SceneElement.VAR_STATE,
						CommonStates.EAD_STATE_WALKING.toString());
			}

			if (finishedSide) {
				updateTarget();
			}

			if (currentTime <= totalTime) {
				gameState.getValueMap().setValue(
						sceneElement,
						SceneElement.VAR_X,
						initX
								+ (int) Interpolator.LINEAR
										.interpolate(currentTime, totalTime,
												targetX - initX));
				gameState.getValueMap().setValue(
						sceneElement,
						SceneElement.VAR_Y,
						initY
								+ (int) Interpolator.LINEAR
										.interpolate(currentTime, totalTime,
												targetY - initY));
				gameState.getValueMap().setValue(
						sceneElement,
						SceneElement.VAR_SCALE,
						initScale
								+ (float) Interpolator.LINEAR.interpolate(
										currentTime, totalTime, targetScale
												- initScale));

			} else {
				gameState.getValueMap().setValue(sceneElement,
						SceneElement.VAR_X, (int) targetX);
				gameState.getValueMap().setValue(sceneElement,
						SceneElement.VAR_Y, (int) targetY);
				finishedSide = true;
			}
		}
	}

	public void finish() {
		gameState.getValueMap().setValue(sceneElement, SceneElement.VAR_STATE,
				oldState);
		gameState.getValueMap().setValue(sceneElement, VAR_ELEMENT_MOVING,
				(Object) null);

		if (path.isGetsTo() || element.getTarget() == null)
			super.finish();
	}

	public void stop() {
		super.stop();
		gameState.getValueMap().setValue(sceneElement, SceneElement.VAR_STATE,
				oldState);
	}

}

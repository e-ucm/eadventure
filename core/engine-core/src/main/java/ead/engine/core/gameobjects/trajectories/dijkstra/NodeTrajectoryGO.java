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

package ead.engine.core.gameobjects.trajectories.dijkstra;

import com.google.inject.Inject;

import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.NodeTrajectory;
import ead.common.model.elements.variables.SystemFields;
import ead.common.util.EAdPosition;
import ead.common.util.Interpolator;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.trajectories.AbstractTrajectoryGO;

public class NodeTrajectoryGO extends AbstractTrajectoryGO<NodeTrajectory> {
	/**
	 * Pixels traveled per second
	 */
	private static final int PIXELS_PER_SECOND = 250;

	private DijkstraNodeTrajectoryGenerator generator;

	private boolean finishedSide = false;

	private boolean finished = false;

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

	private int currentTime;

	private EAdSceneElement sceneElement;

	@Inject
	public NodeTrajectoryGO(SceneElementGOFactory sceneElementFactory,
			GameState gameState) {
		super(gameState);
		this.generator = new DijkstraNodeTrajectoryGenerator(
				sceneElementFactory, gameState);
	}

	@Override
	public void set(SceneElementGO<?> movingElement, int destinyX,
			int destinyY, SceneElementGO<?> target) {
		super.set(movingElement, destinyX, destinyY, target);
		this.sceneElement = (SceneElement) movingElement.getElement();
		currentTime = 0;

		path = generator.getTrajectory(this.trajectory, movingElement
				.getElement(), destinyX, destinyY);

		currentSide = 0;
		targetX = destinyX;
		targetY = destinyY;
		finished = false;
		firstUpdate = true;
		initScale = 1.0f;
		initX = movingElement.getX();
		initY = movingElement.getY();
		targetScale = 1.0f;
		currentTime = 0;
		totalTime = 0;
		updateTarget();
	}

	@Override
	public void update() {
		currentTime += gameState.getValue(SystemFields.ELAPSED_TIME_PER_UPDATE);
		if (!finished) {
			if (firstUpdate) {
				firstUpdate = false;
				gameState.setValue(sceneElement, SceneElement.VAR_STATE,
						CommonStates.EAD_STATE_WALKING.toString());
			}

			if (finishedSide) {
				updateTarget();
			}

			if (currentTime <= totalTime) {
				gameState.setValue(sceneElement, SceneElement.VAR_X, initX
						+ (int) Interpolator.LINEAR.interpolate(currentTime,
								totalTime, targetX - initX));
				gameState.setValue(sceneElement, SceneElement.VAR_Y, initY
						+ (int) Interpolator.LINEAR.interpolate(currentTime,
								totalTime, targetY - initY));
				gameState.setValue(sceneElement, SceneElement.VAR_SCALE,
						initScale
								+ (float) Interpolator.LINEAR.interpolate(
										currentTime, totalTime, targetScale
												- initScale));

			} else {
				gameState.setValue(sceneElement, SceneElement.VAR_X,
						(int) targetX);
				gameState.setValue(sceneElement, SceneElement.VAR_Y,
						(int) targetY);
				finishedSide = true;
			}
		}

		if (finished) {
			movingElement.setState(CommonStates.EAD_STATE_DEFAULT.toString());

			//			if (path.isGetsTo() || effect.getTarget() == null)
			//				super.finish();
		}
	}

	private void updateTarget() {
		if (currentSide < path.getSides().size()) {
			PathSide side = path.getSides().get(currentSide);

			initX = gameState.getValue(movingElement.getElement(),
					SceneElement.VAR_X);
			initY = gameState.getValue(movingElement.getElement(),
					SceneElement.VAR_Y);
			initScale = gameState.getValue(movingElement.getElement(),
					SceneElement.VAR_SCALE);

			EAdPosition p = side.getEndPosition(currentSide == path.getSides()
					.size() - 1);
			targetX = p.getX();
			targetY = p.getY();
			targetScale = side.getEndScale();

			currentTime = (int) (currentTime - totalTime);

			totalTime = (side.getLength() / PIXELS_PER_SECOND * 1000)
					* side.getSpeedFactor();

			gameState.setValue(movingElement.getElement(),
					NodeTrajectory.VAR_CURRENT_SIDE, ((PathSide) side)
							.getSide());

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

		movingElement.setOrientation(tempDirection);
	}

	@Override
	public boolean isDone() {
		return finished;
	}

}

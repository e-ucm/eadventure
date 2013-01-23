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

package ead.engine.core.gameobjects.trajectories.simple;

import com.google.inject.Inject;

import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.common.model.elements.variables.SystemFields;
import ead.common.util.Interpolator;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.trajectories.AbstractTrajectoryGO;

public class SimpleTrajectoryGO extends AbstractTrajectoryGO<SimpleTrajectory> {

	// We want to move 800px every 4 seconds
	private static final int TIME_PER_UNIT = 4000 / 800;

	private float totalTime;

	private float currentTime;

	private int startX;

	private int startY;

	private int diffX;

	private int diffY;

	@Inject
	public SimpleTrajectoryGO(GameState gameState) {
		super(gameState);
	}

	public void set(SceneElementGO<?> movingElement, int destinyX,
			int destinyY, SceneElementGO<?> target) {
		super.set(movingElement, destinyX, destinyY, target);
		startX = movingElement.getX();
		startY = movingElement.getY();
		if (trajectory.isOnlyHorizontal()) {
			this.destinyY = startY;
		}

		if (!trajectory.isFreeWalk()) {
			this.destinyX = Math.min(trajectory.getRight(), Math.max(trajectory
					.getLeft(), this.destinyX));
			if (!trajectory.isOnlyHorizontal()) {
				this.destinyY = Math.max(trajectory.getTop(), Math.min(
						trajectory.getBottom(), this.destinyY));
			}
		}

		diffX = this.destinyX - startX;
		diffY = this.destinyY - startY;
		float distance = (float) Math.sqrt(diffX * diffX + diffY * diffY);
		totalTime = distance * TIME_PER_UNIT;
		currentTime = 0;

		// If it's really going to move...
		if (Math.abs(diffX) > 0.1f || Math.abs(diffY) > 0.1f) {
			movingElement.setState(CommonStates.EAD_STATE_WALKING.toString());

			if (Math.abs(diffX) > Math.abs(diffY)) {
				if (diffX > 0) {
					movingElement.setOrientation(Orientation.E);
				} else {
					movingElement.setOrientation(Orientation.W);
				}
			} else {
				if (diffY > 0) {
					movingElement.setOrientation(Orientation.S);
				} else {
					movingElement.setOrientation(Orientation.N);
				}
			}
		}
	}

	@Override
	public void update() {
		currentTime += gameState.getValue(SystemFields.ELAPSED_TIME_PER_UPDATE);
		float x = Interpolator.LINEAR
				.interpolate(currentTime, totalTime, diffX);
		float y = Interpolator.LINEAR
				.interpolate(currentTime, totalTime, diffY);
		movingElement.setX((int) (startX + x));
		movingElement.setY((int) (startY + y));
		if (currentTime >= totalTime) {
			movingElement.setState(CommonStates.EAD_STATE_DEFAULT.toString());
		}
	}

	@Override
	public boolean isDone() {
		return currentTime >= totalTime;
	}

}

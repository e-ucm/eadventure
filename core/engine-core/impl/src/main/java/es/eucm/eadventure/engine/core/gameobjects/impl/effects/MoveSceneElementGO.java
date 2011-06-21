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

import com.google.inject.Inject;

import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.OperatorFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.interfaces.LinearInterpolation;
import es.eucm.eadventure.engine.core.gameobjects.interfaces.Interpolation;

/**
 * Game object for {@link EAdMoveSceneElement} effect
 * 
 * 
 */
public class MoveSceneElementGO extends AbstractEffectGO<EAdMoveSceneElement> {

	/**
	 * Pixels traveled per second
	 */
	private static final int PIXELS_PER_SECOND = 100;

	/**
	 * Movement interpolation
	 */
	private Interpolation interpolationX, interpolationY;

	private boolean isIsometric = true;

	@Inject
	private OperatorFactory operatorFactory;
	
	@Override
	public void initilize() {
		super.initilize();
		valueMap.setValue(element.animationEnded(), Boolean.FALSE);
		EAdMoveSceneElement effect = element;
		SceneElementGO<?> a = (SceneElementGO<?>) gameObjectFactory.get(effect
				.getSceneElement());

		int x = valueMap.getValue(element.getSceneElement().positionXVar());
		int y = valueMap.getValue(element.getSceneElement().positionYVar());

		int targetX = this.operatorFactory.operate(new IntegerVar(" "), effect.getXTarget());
		int targetY = this.operatorFactory.operate(new IntegerVar(" "), effect.getYTarget());
		
		float distance = (float) Math.sqrt(Math.pow(x - targetX, 2)
				+ Math.pow(y - targetY, 2));

		if (effect.getSpeed() != EAdMoveSceneElement.MovementSpeed.INSTANT) {
			int timeToFinish = Math.round((distance * 1000 / PIXELS_PER_SECOND)
					* effect.getSpeed().getSpeedFactor());
	
			interpolationX = new LinearInterpolation<Integer>(a.getElement().positionXVar(), x,
					targetX, timeToFinish, 0, false, valueMap);
			interpolationY = new LinearInterpolation<Integer>(a.getElement().positionYVar(), y,
					targetY, timeToFinish, 0, false, valueMap);
	
			interpolationX.play();
			interpolationY.play();
			updateDirection(a, x, targetX, y, targetY);
		} else {
			valueMap.setValue(a.getElement().positionXVar(), targetX);
			valueMap.setValue(a.getElement().positionYVar(), targetY);
			valueMap.setValue(element.animationEnded(), Boolean.TRUE);
		}

	}

	private void updateDirection(SceneElementGO<?> a, float x, float targetX,
			float y, float targetY) {
		Orientation tempDirection = Orientation.EAST;

		// FIXME Este isometric debe venir de alguna parte
		if (isIsometric) {
			float xv = targetX - x;
			float yv = targetY - y;
			double module = Math.sqrt(xv * xv + yv * yv);
			double angle = Math.acos(xv / module) * Math.signum(-yv);

			tempDirection = Orientation.WEST;

			if (angle < 3 * Math.PI / 4 && angle >= Math.PI / 4) {
				tempDirection = Orientation.NORTH;
			} else if (angle < Math.PI / 4 && angle >= -Math.PI / 4) {
				tempDirection = Orientation.EAST;
			} else if (angle < -Math.PI / 4 && angle >= -3 * Math.PI / 4) {
				tempDirection = Orientation.SOUTH;
			}

		} else {
			float velocityX = 0;
			if (targetX > x)
				velocityX = PIXELS_PER_SECOND;
			else if (targetX < x)
				velocityX = -PIXELS_PER_SECOND;

			float velocityY = 0;
			if (targetY > y)
				velocityY = PIXELS_PER_SECOND;
			else if (targetX < y)
				velocityY = -PIXELS_PER_SECOND;
			if (Math.abs(velocityY) > Math.abs(velocityX)) {
				if (velocityY > 0) {
					tempDirection = Orientation.SOUTH;
				} else if (velocityY < 0) {
					tempDirection = Orientation.NORTH;
				}
			} else {
				if (velocityX > 0) {
					tempDirection = Orientation.EAST;
				} else if (velocityX < 0) {
					tempDirection = Orientation.WEST;
				}
			}
		}

		a.setOrientation(tempDirection);

	}

	@Override
	public void update(GameState state) {
		if (interpolationX != null && interpolationY != null) {
			interpolationX.update(GameLoop.SKIP_MILLIS_TICK);
			interpolationY.update(GameLoop.SKIP_MILLIS_TICK);
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		if (interpolationX != null && interpolationY != null) {
			boolean finished  = !interpolationX.isRunning() && !interpolationY.isRunning();
			if (finished)
				valueMap.setValue(element.animationEnded(), Boolean.TRUE);
			return finished;
		}
		return true;
	}

}

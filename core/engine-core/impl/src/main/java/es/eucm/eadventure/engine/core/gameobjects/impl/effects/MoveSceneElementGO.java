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

import es.eucm.eadventure.common.interfaces.features.Oriented.Orientation;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElement.CommonStates;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

/**
 * Game object for {@link EAdMoveSceneElement} effect
 * 
 * 
 */
public class MoveSceneElementGO extends AbstractEffectGO<EAdMoveSceneElement> {

	/**
	 * Pixels traveled per second
	 */
	private static final int PIXELS_PER_SECOND = 200;

	private boolean isIsometric = true;

	private OperatorFactory operatorFactory;

	@Inject
	public MoveSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration,
			OperatorFactory operatorFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void initilize() {
		super.initilize();

		valueMap.setValue(element.getSceneElement().getVars().getVar(EAdSceneElementVars.VAR_STATE),
				CommonStates.EAD_STATE_WALKING.toString());

		valueMap.setValue(element.animationEnded(), Boolean.FALSE);
		EAdMoveSceneElement effect = element;
		SceneElementGO<?> a = (SceneElementGO<?>) gameObjectFactory.get(effect
				.getSceneElement());

		int x = valueMap.getValue(element.getSceneElement().getVars().getVar(EAdSceneElementVars.VAR_X));
		int y = valueMap.getValue(element.getSceneElement().getVars().getVar(EAdSceneElementVars.VAR_Y));

		int targetX = this.operatorFactory.operate(new IntegerVar(" "),
				effect.getXTarget());
		int targetY = this.operatorFactory.operate(new IntegerVar(" "),
				effect.getYTarget());

		float distance = (float) Math.sqrt(Math.pow(x - targetX, 2)
				+ Math.pow(y - targetY, 2));

		if (effect.getSpeed() != EAdMoveSceneElement.MovementSpeed.INSTANT) {
			int timeToFinish = Math.round((distance * 1000 / PIXELS_PER_SECOND)
					* effect.getSpeed().getSpeedFactor());

			if (targetX != x) {
				gameState.addEffect(new EAdVarInterpolationEffect(
						"interpolationX", a.getElement().getVars().getVar(EAdSceneElementVars.VAR_X), x,
						targetX, timeToFinish, LoopType.NO_LOOP));
			}

			if (targetY != y) {
				gameState.addEffect(new EAdVarInterpolationEffect(
						"interpolationY", a.getElement().getVars().getVar(EAdSceneElementVars.VAR_Y), y,
						targetY, timeToFinish, LoopType.NO_LOOP));
			}

			updateDirection(a, x, targetX, y, targetY);
		} else {
			valueMap.setValue(a.getElement().getVars().getVar(EAdSceneElementVars.VAR_X), targetX);
			valueMap.setValue(a.getElement().getVars().getVar(EAdSceneElementVars.VAR_Y), targetY);
			valueMap.setValue(element.animationEnded(), Boolean.TRUE);
		}

	}

	private void updateDirection(SceneElementGO<?> a, float x, float targetX,
			float y, float targetY) {
		Orientation tempDirection = Orientation.E;

		// FIXME Este isometric debe venir de alguna parte
		if (isIsometric) {
			float xv = targetX - x;
			float yv = targetY - y;
			double module = Math.sqrt(xv * xv + yv * yv);
			double angle = Math.acos(xv / module) * Math.signum(-yv);

			tempDirection = Orientation.W;

			if (angle < 3 * Math.PI / 4 && angle >= Math.PI / 4) {
				tempDirection = Orientation.N;
			} else if (angle < Math.PI / 4 && angle >= -Math.PI / 4) {
				tempDirection = Orientation.E;
			} else if (angle < -Math.PI / 4 && angle >= -3 * Math.PI / 4) {
				tempDirection = Orientation.S;
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
					tempDirection = Orientation.S;
				} else if (velocityY < 0) {
					tempDirection = Orientation.N;
				}
			} else {
				if (velocityX > 0) {
					tempDirection = Orientation.E;
				} else if (velocityX < 0) {
					tempDirection = Orientation.W;
				}
			}
		}

		valueMap.setValue(element.getSceneElement().getVars().getVar(EAdSceneElementVars.VAR_ORIENTATION),
				tempDirection);

	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}

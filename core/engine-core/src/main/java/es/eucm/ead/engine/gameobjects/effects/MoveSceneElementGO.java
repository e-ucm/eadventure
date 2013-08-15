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

package es.eucm.ead.engine.gameobjects.effects;

import com.google.inject.Inject;

import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.effects.sceneelements.MoveSceneElementEf;
import es.eucm.ead.model.elements.enums.CommonStates;
import es.eucm.ead.model.elements.scenes.BasicScene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.trajectories.EAdTrajectory;
import es.eucm.ead.model.elements.trajectories.SimpleTrajectory;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.model.params.variables.VarDef;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.factories.TrajectoryFactory;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.engine.game.interfaces.ValueMap;
import es.eucm.ead.engine.gameobjects.effects.sceneelement.SceneElementEffectGO;
import es.eucm.ead.engine.gameobjects.trajectories.TrajectoryGO;

/**
 * Game object for {@link MoveSceneElementEf} effect
 * 
 * 
 */
public class MoveSceneElementGO extends
		SceneElementEffectGO<MoveSceneElementEf> {

	public static final EAdVarDef<MoveSceneElementGO> VAR_ELEMENT_MOVING = new VarDef<MoveSceneElementGO>(
			"element_moving", MoveSceneElementGO.class, null);

	private static final EAdTrajectory DEFAULT_TRAJECTORY = new SimpleTrajectory();

	private TrajectoryFactory trajectoryFactory;

	private SceneElementGOFactory sceneElementFactory;

	private GUI gui;

	private TrajectoryGO<? extends EAdTrajectory> trajectory;

	private boolean cancelMovement;

	@Inject
	public MoveSceneElementGO(GameState gameState,
			SceneElementGOFactory sceneElementFactory,
			TrajectoryFactory trajectoryFactory, GUI gui) {
		super(gameState);
		this.trajectoryFactory = trajectoryFactory;
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;
	}

	@Override
	public void initialize() {
		super.initialize();
		ValueMap valueMap = gameState;
		float endX = 0;
		float endY = 0;
		cancelMovement = false;

		SceneElementGO movingElement = sceneElementFactory.get(sceneElement);

		if (effect.getXtarget() != null && effect.getYtarget() != null) {
			endX = gameState.operate(Float.class, effect.getXtarget());
			endY = gameState.operate(Float.class, effect.getYtarget());
		} else if (effect.getTargetSceneElement() != null) {
			endX = gameState.getValue(effect.getTargetSceneElement(),
					SceneElement.VAR_X);
			endY = gameState.getValue(effect.getTargetSceneElement(),
					SceneElement.VAR_Y);
			float width = movingElement.getWidth();
			float height = movingElement.getHeight();
			float dispX = movingElement.getDispX();
			float dispY = movingElement.getDispY();

			float centerX = dispX * width - width / 2;
			float centerY = dispY * height - height / 2;

			endX -= centerX;
			endY -= centerY;
		} else {
			cancelMovement = true;
		}

		if (!cancelMovement) {
			EAdTrajectory d = DEFAULT_TRAJECTORY;

			if (effect.isUseTrajectory()) {

				EAdTrajectory sceneTrajectory = valueMap.getValue(gui
						.getScene().getElement(),
						BasicScene.VAR_TRAJECTORY_DEFINITION);
				if (sceneTrajectory != null) {
					d = sceneTrajectory;
				}
			}

			trajectory = trajectoryFactory.get(d);
			trajectory.set(movingElement, endX, endY, sceneElementFactory
					.get(effect.getTargetSceneElement()));

		}
		// Check if the element is controlled by other move scene effect
		MoveSceneElementGO go = gameState.getValue(sceneElement,
				VAR_ELEMENT_MOVING);
		if (go != null) {
			go.stop();
		}
		if (!cancelMovement) {
			gameState.setValue(sceneElement, VAR_ELEMENT_MOVING, this);
		}

	}

	@Override
	public boolean isFinished() {
		return cancelMovement || trajectory.isDone();
	}

	@Override
	public void act(float delta) {
		trajectory.act(delta);
	}

	public void finish() {
		if ((!cancelMovement && trajectory.isReachedTarget())
				|| !this.effect.isUseTrajectory()) {
			super.finish();
		}
		gameState.setValue(sceneElement, VAR_ELEMENT_MOVING,
				(MoveSceneElementGO) null);

		if (trajectory != null) {
			//			trajectoryFactory.remove(trajectory);
		}
	}

	public void stop() {
		super.stop();
		gameState.setValue(sceneElement, SceneElement.VAR_STATE,
				CommonStates.DEFAULT.toString());
		gameState.setValue(sceneElement, VAR_ELEMENT_MOVING,
				(MoveSceneElementGO) null);
		if (trajectory != null) {
			//			trajectoryFactory.remove(trajectory);
		}
	}

	public boolean isQueueable() {
		return true;
	}

}

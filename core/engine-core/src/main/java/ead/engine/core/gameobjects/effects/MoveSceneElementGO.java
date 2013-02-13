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

import com.google.inject.Inject;

import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.EAdTrajectory;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.common.model.params.variables.EAdVarDef;
import ead.common.model.params.variables.VarDef;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.factories.TrajectoryFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.effects.sceneelement.SceneElementEffectGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.trajectories.TrajectoryGO;
import ead.engine.core.platform.GUI;

/**
 * Game object for {@link MoveSceneElementEf} effect
 * 
 * 
 */
public class MoveSceneElementGO extends
		SceneElementEffectGO<MoveSceneElementEf> {

	private static final EAdVarDef<MoveSceneElementGO> VAR_ELEMENT_MOVING = new VarDef<MoveSceneElementGO>(
			"element_moving", MoveSceneElementGO.class, null);

	private static final EAdTrajectory DEFAULT_TRAJECTORY = new SimpleTrajectory();

	private TrajectoryFactory trajectoryFactory;

	private SceneElementGOFactory sceneElementFactory;

	private GUI gui;

	private TrajectoryGO<? extends EAdTrajectory> trajectory;

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

		SceneElementGO<?> movingElement = sceneElementFactory.get(sceneElement);

		if (effect.getxTarget() != null && effect.getyTarget() != null) {
			endX = gameState.operate(Integer.class, effect.getxTarget());
			endY = gameState.operate(Integer.class, effect.getyTarget());
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
		}

		EAdTrajectory d = DEFAULT_TRAJECTORY;

		if (effect.isUseTrajectory()) {

			EAdTrajectory sceneTrajectory = valueMap.getValue(gui.getScene()
					.getElement(), BasicScene.VAR_TRAJECTORY_DEFINITION);
			if (sceneTrajectory != null) {
				d = sceneTrajectory;
			}
		}

		trajectory = trajectoryFactory.get(d);
		trajectory.set(movingElement, endX, endY, sceneElement == null ? null
				: sceneElementFactory.get(sceneElement));

		// Check if the element is controlled by other move scene effect
		MoveSceneElementGO go = gameState.getValue(sceneElement,
				VAR_ELEMENT_MOVING);
		if (go != null) {
			go.stop();
		}
		gameState.setValue(sceneElement, VAR_ELEMENT_MOVING, this);

	}

	@Override
	public boolean isFinished() {
		return trajectory.isDone();
	}

	@Override
	public void act(float delta) {
		trajectory.act(delta);
	}

	public void finish() {
		super.finish();
		gameState.setValue(sceneElement, VAR_ELEMENT_MOVING, (MoveSceneElementGO) null);
	}

	public void stop() {
		super.stop();
		gameState.setValue(sceneElement, SceneElement.VAR_STATE,
				CommonStates.EAD_STATE_DEFAULT.toString());
	}

	public boolean isQueueable() {
		return true;
	}

}

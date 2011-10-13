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

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveActiveElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement.MovementSpeed;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.PathSide;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;

public class MoveActiveElementGO extends AbstractEffectGO<EAdMoveActiveElement> {

	private TrajectoryFactory trajectoryFactory;

	@Inject
	public MoveActiveElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration,
			TrajectoryFactory trajectoryFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
		this.trajectoryFactory = trajectoryFactory;
	}

	@Override
	public void initilize() {
		super.initilize();
		Object object = gameState.getScene().getElement();
		if (object instanceof EAdScene && action instanceof MouseAction) {
			int x = element.getTargetX() == EAdMoveActiveElement.MOUSE_COORDINATE ? valueMap
					.getValue(SystemFields.MOUSE_X) : element.getTargetX();
			int y = element.getTargetY() == EAdMoveActiveElement.MOUSE_COORDINATE ? valueMap
					.getValue(SystemFields.MOUSE_Y) : element.getTargetY();
			EAdScene scene = (EAdScene) object;
			TrajectoryDefinition trajectoryDefinition = valueMap.getValue(
					scene, EAdSceneImpl.VAR_TRAJECTORY_DEFINITION);
			if (trajectoryDefinition != null) {
				EAdPositionImpl pos = new EAdPositionImpl(0, 0);
				pos.setX(valueMap.getValue(gameState.getActiveElement(),
						EAdBasicSceneElement.VAR_X));
				pos.setY(valueMap.getValue(gameState.getActiveElement(),
						EAdBasicSceneElement.VAR_Y));
				Path trajectory = trajectoryFactory.getTrajectory(
						trajectoryDefinition, pos, x, y);
				for (int i = 0; i < trajectory.getSides().size(); i++) {
					PathSide p = trajectory.getSides().get(i);
					EAdMoveSceneElement effect = new EAdMoveSceneElement(
							"trajectory", gameState.getActiveElement(),
							p.getEndPosition(
									i == trajectory.getSides().size() - 1)
									.getX(), p.getEndPosition(
									i == trajectory.getSides().size() - 1)
									.getY(), MovementSpeed.NORMAL);
					effect.setSpeedFactor(p.getSpeedFactor());
					effect.setBlocking(true);
					effect.setQueueable(true);
					gameState.addEffect(effect);

					EAdEffect e = trajectory.getChangeSideEffect(p,
							trajectoryDefinition);
					if (e != null) {
						e.setQueueable(true);
						gameState.addEffect(e);
					}
				}
			}
		}
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

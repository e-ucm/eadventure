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

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.MovementSpeed;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.PathSide;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryFactory;

public class AbstractMoveActiveElementGO<P extends EAdEffect> extends AbstractEffectGO<P> {

	protected TrajectoryFactory trajectoryFactory;

	protected AbstractMoveActiveElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState,
			TrajectoryFactory trajectoryFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
		this.trajectoryFactory = trajectoryFactory;
	}
	protected EAdEffect getSideEffect(PathSide p, Path trajectory, int i, TrajectoryDefinition trajectoryDefinition) {
		EAdMoveSceneElement effect = new EAdMoveSceneElement( gameState.getActiveElement(),
				p.getEndPosition(
						i == trajectory.getSides().size() - 1)
						.getX(), p.getEndPosition(
						i == trajectory.getSides().size() - 1)
						.getY(), MovementSpeed.NORMAL);
		effect.setId("trajectory");
		effect.setSpeedFactor(p.getSpeedFactor());
		effect.setBlocking(true);
		effect.setQueueable(true);

		EAdEffect e = trajectory.getChangeSideEffect(p,
				trajectoryDefinition);
		if (e != null) {
			e.setQueueable(true);
			effect.getFinalEffects().add(e);
		}
		
		return effect;
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

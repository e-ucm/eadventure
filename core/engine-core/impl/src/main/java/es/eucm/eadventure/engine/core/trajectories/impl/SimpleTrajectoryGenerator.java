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

package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.engine.core.game.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.go.SceneElementGO;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

public class SimpleTrajectoryGenerator implements
		TrajectoryGenerator<SimpleTrajectoryDefinition> {

	/**
	 * The values in the game
	 */
	private ValueMap valueMap;

	@Inject
	public SimpleTrajectoryGenerator(ValueMap valueMap) {
		this.valueMap = valueMap;
	}
	
	@Override
	public Path getTrajectory(
			SimpleTrajectoryDefinition def,
			EAdElement movingElement, int x, int y) {

		EAdPosition currentPosition = getCurrentPosition(movingElement);
		
		List<EAdPosition> list = new ArrayList<EAdPosition>();
		if (def.isOnlyHorizontal()) {
			list.add(new EAdPositionImpl(getX(def, x), currentPosition.getY()));
		} else {
			list.add(new EAdPositionImpl(getX(def, x), getY(def, y)));
		}

		return new SimplePathImpl(list, currentPosition, valueMap.getValue(movingElement, EAdBasicSceneElement.VAR_SCALE));
	}

	@Override
	public Path getTrajectory(
			SimpleTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, int x, int y, SceneElementGO<?> sceneElement) {

		EAdPosition currentPosition = getCurrentPosition(movingElement);

		List<EAdPosition> list = new ArrayList<EAdPosition>();
		if (trajectoryDefinition.isOnlyHorizontal()) {
			list.add(new EAdPositionImpl(x, currentPosition.getY()));
		} else {
			list.add(new EAdPositionImpl(x, y));
		}

		return new SimplePathImpl(list,currentPosition, valueMap.getValue(movingElement, EAdBasicSceneElement.VAR_SCALE));
	}

	@Override
	public boolean canGetTo(SimpleTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, SceneElementGO<?> sceneElement) {
		//TODO check barriers?
		return false;
	}
	
	private int getX( SimpleTrajectoryDefinition def, int x ){
		if ( x > def.getRight() )
			return def.getRight();
		if ( x < def.getLeft() )
			return def.getLeft();
		return x;
	}
	
	private int getY( SimpleTrajectoryDefinition def, int y ){
		if ( y > def.getBottom() )
			return def.getBottom();
		if ( y < def.getTop() )
			return def.getTop();
		return y;
	}
	
	private EAdPosition getCurrentPosition(EAdElement element) {
		int x = valueMap.getValue(element,
				EAdBasicSceneElement.VAR_X);
		int y = valueMap.getValue(element,
				EAdBasicSceneElement.VAR_Y);
		return new EAdPositionImpl(x, y);
	}

}

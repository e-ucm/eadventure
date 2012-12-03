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

package ead.engine.core.trajectories;

import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.trajectories.EAdTrajectoryDefinition;
import ead.engine.core.gameobjects.go.SceneElementGO;

/**
 * General interface for trajectories generators. Trajectories are build from a
 * {@link EAdTrajectoryDefinition}
 * 
 * 
 * @param <T>
 *            the class of the trajectory definition
 */
public interface TrajectoryGenerator<T extends EAdTrajectoryDefinition> {

	/**
	 * Returns a {@link Path} representing the trajectory to go to the given x
	 * and y coordinates
	 * 
	 * @param trajectoryDefinition
	 *            the trajectory definition for which to calculate the actual
	 *            trajectory
	 * @param movingElement
	 * 			  the element that is to be moved
	 * @param x
	 *            target x coordinate
	 * @param y
	 *            target y coordinate
	 * @return the path
	 */
	Path getTrajectory(T trajectoryDefinition, EAdSceneElement movingElement,
			int x, int y);

	/**
	 * Returns a {@link Path} representing the trajectory to get as close as
	 * possible to the given {@link SceneElementGO}. The distance to the element
	 * will depend on different variables of the SceneElement.
	 * 
	 * @param trajectoryDefinition
	 *            the trajectory definition for which to calculate the actual
	 *            trajectory
	 * @param movingElement
	 * 			  the element that is to be moved
	 * @param x
	 *            target x coordinate
	 * @param y
	 *            target y coordinate
	 * @param sceneElement
	 *            The element towards which to move
	 * @return a path
	 */
	Path getTrajectory(T trajectoryDefinition, EAdSceneElement movingElement,
			int x, int y, SceneElementGO<?> sceneElement);

	/**
	 * Returns true if the player can get to the given {@link SceneElementGO}
	 * using the current trajectory definition and all existing obstacles.
	 * 
	 * @param trajectoryDefinition
	 *            the trajectory definition for which to establish if the player
	 *            can get to the element
	 * @param currentPosition
	 *            the start position of the player
	 * @param sceneElement
	 *            the element that needs to be reached
	 * @return true if the element can be reached, false otherwise
	 */
	boolean canGetTo(T trajectoryDefinition, EAdSceneElement movingElement,
			SceneElementGO<?> sceneElement);

}

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

package es.eucm.ead.engine.gameobjects.trajectories;

import java.util.List;

import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.trajectories.Trajectory;
import es.eucm.ead.engine.gameobjects.GameObject;

/**
 * General interface for trajectories generators. Trajectories are build from a
 * {@link es.eucm.ead.model.elements.trajectories.Trajectory}
 * 
 * 
 * @param <T>
 *            the class of the trajectory definition
 */
public interface TrajectoryGO<T extends Trajectory> extends GameObject<T> {

	/***
	 * Sets the attributes for the trajectory
	 * 
	 * @param movingElement
	 *            the moving element
	 * @param destinationX
	 *            destination coordinate x
	 * @param destinationY
	 *            destination coordinate y
	 * @param target
	 *            the target element trying to reach (it could be {@code null}
	 */
	void set(SceneElementGO movingElement, float destinationX,
			float destinationY, SceneElementGO target);

	/**
	 * Returns true when the trajectory is completed
	 * 
	 * @return
	 */
	boolean isDone();

	/**
	 * Returns true if the moving element has reached the target (if any). It
	 * also returns true if no target is set
	 * 
	 * @return
	 */
	boolean isReachedTarget();

	/**
	 * Returns the current calculated path
	 * 
	 * @return
	 */
	List<Float> getCurrentPath();

}

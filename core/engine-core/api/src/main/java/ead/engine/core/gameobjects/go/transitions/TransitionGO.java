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

package ead.engine.core.gameobjects.go.transitions;

import java.util.List;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.transitions.EAdTransition;
import ead.engine.core.gameobjects.go.SceneGO;

/**
 * A transition game object
 * 
 */
public interface TransitionGO<T extends EAdTransition> extends
		SceneGO<EAdScene>, SceneLoaderListener {

	/**
	 * Set the previous scene for the transition
	 * 
	 * @param scene
	 *            the previous scene
	 */
	void setPrevious(SceneGO<?> scene);

	/**
	 * Sets the next scene for the transition
	 * 
	 * @param scene
	 *            the next scene for the transition
	 */
	void setNext(EAdScene scene);

	/**
	 * Returns if the next scene is loaded
	 * 
	 * @return
	 */
	boolean isLoadedNextScene();

	/**
	 * Returns if the transition is finished
	 * 
	 * @return
	 */
	boolean isFinished();

	/**
	 * Sets the transition data
	 * 
	 * @param transition
	 *            the transition data
	 */
	void setTransition(T transition);

	/**
	 * Returns the list of TransitionListener. You can add or remove your
	 * transition listeners in this list
	 */
	List<TransitionListener> getTransitionListeners();

	public interface TransitionListener {

		/**
		 * Method called when transition begins
		 */
		void transitionBegins();

		/**
		 * Method called when transition ends
		 */
		void transitionEnds();

	}

}

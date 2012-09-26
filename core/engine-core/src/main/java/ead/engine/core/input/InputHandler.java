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

package ead.engine.core.input;

import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.util.EAdTransformation;

/**
 * An element registering all the GUI actions performed, and the current state
 * for all the input methods (mouse, keyboards, joysticks...)
 * 
 */
public interface InputHandler {

	/**
	 * Adds an input action to be processed
	 * 
	 * @param action
	 *            the input action (a mouse action, a keyboard action...)
	 */
	void addAction(InputAction<?> action);

	/**
	 * Check if the input is in the given state
	 * 
	 * @param state
	 *            the input state to check
	 * @return
	 */
	boolean checkState(InputState state);

	/**
	 * Process the GUI actions pending in the queue
	 */
	void processActions();

	/**
	 * Returns the x coordinate of the pointer (normally the mouse). This
	 * coordinate is expressed in the game coordinate system
	 * 
	 * @return the pointer's x coordinate
	 */
	int getPointerX();

	/**
	 * Returns the y coordinate of the pointer (normally the mouse). This
	 * coordinate is expressed in the game coordinate system
	 * 
	 * @return the pointer's y coordinate
	 */
	int getPointerY();

	/**
	 * Returns the raw x coordinate of the pointer. This coordinate is the
	 * absolute position in window coordinates system
	 * 
	 * @return the x coordinate of the pointer in the window
	 */
	int getRawPointerX();

	/**
	 * Returns the raw y coordinate of the pointer. This coordinate is the
	 * absolute position in window coordinates system
	 * 
	 * @return the y coordinate of the pointer in the window
	 */
	int getRawPointerY();

	/**
	 * Returns the element under the pointer (normally, the mouse)
	 * 
	 * @return the element under the pointer
	 */
	DrawableGO<?> getGameObjectUnderPointer();

	SceneElementGO<?> getDraggingGameObject();

	EAdSceneElementDef getDraggingElement();

	void setInitialTransformation(EAdTransformation initialTransformation);

	/**
	 * Clears all the pending inputs for the game
	 */
	void clearAllInputs();

	/**
	 * Returns if the input handler is currently processing user input.
	 * 
	 * @return
	 */
	boolean isProcessingInput();

}

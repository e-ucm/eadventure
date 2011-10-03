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

package es.eucm.eadventure.engine.core;

import java.util.Queue;

import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;

/**
 * <p>
 * Represents the state of the mouse (or virtual mouse element) in the game.
 * </p>
 */
public interface MouseState {

	/**
	 * Default value when the mouse is not inside the game window
	 */
	static final int OUT_VAL = -1;

	/**
	 * The actual mouse position along the x axis within the window. If the
	 * mouse is outside, it will return {@link OUT_VAL}
	 * 
	 * @return the mouse position along the x axis within the window
	 */
	int getMouseX();

	/**
	 * The actual mouse position along the y axis within the window. If the
	 * mouse is outside, it will return {@link OUT_VAL}
	 * 
	 * @return the mouse position along the y axis within the window
	 */
	int getMouseY();

	/**
	 * Set the position of the mouse pointer
	 * 
	 * @param mouseX
	 *            The position along the x axis
	 * @param mouseY
	 *            The position along the y axis
	 */
	void setMousePosition(int mouseX, int mouseY);

	/**
	 * Returns true if the main mouse button is pressed. This is used for
	 * dragging elements in the game.
	 * 
	 * @return true if main mouse button is pressed
	 */
	boolean isMousePressed();

	/**
	 * Set the value of the main mouse button pressed status. This is used for
	 * dragging elements in the game.
	 * 
	 * @param mousePressed
	 *            the pressed status of the main mouse button
	 */
	void setMousePressed(boolean mousePressed);

	/**
	 * Returns the queue of mouse events or actions, of type {@link MouseAction}
	 * registered in the game.
	 * 
	 * @return the queue of registered mouse events
	 */
	Queue<MouseAction> getMouseEvents();

	/**
	 * Returns the game object that is immediately under the mouse. This is used
	 * to pass events and to draw the name of the element in the game HUD if
	 * needed.
	 * 
	 * @return the game object currently under the mouse
	 */
	GameObject<?> getGameObjectUnderMouse();

	/**
	 * Set the game object that is directly under the mouse pointer
	 * 
	 * @param elementUnderMouse
	 *            the game object under the mouse pointer
	 */
	void setGameObjectUnderMouse(GameObject<?> elementUnderMouse);

	/**
	 * Returns the game object that is being dragged, null if no object is being
	 * dragged
	 * 
	 * @return the game object that is being dragged, null if no object is being
	 *         dragged
	 */
	GameObject<?> getDraggingGameObject();

	/**
	 * Set the game object that is being dragged, null to clear the object
	 * 
	 * @param draggingElement
	 *            the object that is being dragged, null to clear the object
	 * @param offsetX
	 * @param offsetY
	 */
	void setDraggingGameObject(GameObject<?> draggingElement);

	/**
	 * Returns true if the mouse is inside the game window
	 * 
	 * @return true if the mouse is inside the game window
	 */
	boolean isInside();

	/**
	 * Returns the amount of pixels the dragging element was dragged along the x
	 * axis
	 * 
	 * @return how much the dragging element was dragged along the x axis
	 */
	int getDragDifX();

	/**
	 * Returns the amount of pixels the dragging element was dragged along the y
	 * axis
	 * 
	 * @return how much the dragging element was dragged along the y axis
	 */
	int getDragDifY();

}

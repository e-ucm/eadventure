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

import java.util.LinkedList;
import java.util.Queue;

import com.google.inject.Singleton;

import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.input.states.MouseState;
import ead.engine.core.util.EAdTransformation;

@Singleton
public class MouseHandler {

	/**
	 * Constant representing the mouse out of the window
	 */
	public static final int OUT_VAL = Integer.MIN_VALUE;

	/**
	 * Z used in drag
	 */
	private static final int DRAG_Z = Integer.MAX_VALUE;

	private GameState gameState;

	private EAdTransformation initialTransformation;

	// Mouse position

	/**
	 * X coordinate of the windows' pixel the mouse is in
	 */
	private int mouseRawX = OUT_VAL;

	/**
	 * Y coordinate of the windows' pixel the mouse is in
	 */
	private int mouseRawY = OUT_VAL;

	// Mouse pressed

	/**
	 * Whether the mouse is pressed
	 */
	private boolean mousePressed = false;

	/**
	 * Which button is pressed on the mouse
	 */
	private MouseGEvButtonType buttonPressed = null;

	/**
	 * Queue with mouse events
	 */
	private Queue<MouseInputAction> mouseEvents;

	/**
	 * Game object under the mouse
	 */
	private DrawableGO<?> gameObjectUnderMouse;

	// Drag & Drop attributes

	/**
	 * Game object being dragged
	 */
	private SceneElementGO<?> draggingGameObject;

	/**
	 * X virtual coordinate where the drag was started
	 */
	private int initDragX;

	/**
	 * Y virtual coordinate where the drag was started
	 */
	private int initDragY;

	private int diffX;

	private int diffY;

	/**
	 * Z of the scene element being dragged
	 */
	private int initZ;

	public MouseHandler(GameState gameState) {
		mouseEvents = new LinkedList<MouseInputAction>();
		this.gameState = gameState;
	}

	/**
	 * The actual mouse position along the x axis within the window. If the
	 * mouse is outside, it will return {@link OUT_VAL}
	 * 
	 * @return the mouse position along the x axis within the window
	 */
	public int getMouseX() {
		return mouseRawX;
	}

	/**
	 * The actual mouse position along the y axis within the window. If the
	 * mouse is outside, it will return {@link OUT_VAL}
	 * 
	 * @return the mouse position along the y axis within the window
	 */
	public int getMouseY() {
		return mouseRawY;
	}

	private void updateDrag() {
		if (this.draggingGameObject != null) {
			EAdSceneElement e = draggingGameObject.getElement();
			int mouseVirtualX = gameState.getValueMap().getValue(
					SystemFields.MOUSE_SCENE_X);
			int mouseVirtualY = gameState.getValueMap().getValue(
					SystemFields.MOUSE_SCENE_Y);
			gameState.getValueMap().setValue(e, SceneElement.VAR_X,
					mouseVirtualX - diffX);
			gameState.getValueMap().setValue(e, SceneElement.VAR_Y,
					mouseVirtualY - diffY);
		}
	}

	/**
	 * Returns true if the main mouse button is pressed. This is used for
	 * dragging elements in the game.
	 * 
	 * @return true if main mouse button is pressed
	 */
	public boolean isMousePressed(MouseGEvButtonType button) {
		return mousePressed && button == this.buttonPressed;
	}

	/**
	 * Set the value of the main mouse button pressed status. This is used for
	 * dragging elements in the game.
	 * 
	 * @param mousePressed
	 *            the pressed status of the main mouse button
	 */
	public void setMousePressed(boolean mousePressed, MouseGEvButtonType button) {
		this.mousePressed = mousePressed;
		this.buttonPressed = button;
	}

	/**
	 * Returns the queue of mouse events or actions, of type {@link MouseAction}
	 * registered in the game.
	 * 
	 * @return the queue of registered mouse events
	 */
	public Queue<MouseInputAction> getMouseEvents() {
		return mouseEvents;
	}

	/**
	 * Returns the game object that is immediately under the mouse. This is used
	 * to pass events and to draw the name of the element in the game HUD if
	 * needed.
	 * 
	 * @return the game object currently under the mouse
	 */
	public DrawableGO<?> getGameObjectUnderMouse() {
		return gameObjectUnderMouse;
	}

	/**
	 * Set the game object that is directly under the mouse pointer
	 * 
	 * @param currentGO
	 *            the game object under the mouse pointer
	 */
	public void setGameObjectUnderMouse(DrawableGO<?> gameObjectUnderMouse) {
		this.gameObjectUnderMouse = gameObjectUnderMouse;
	}

	/**
	 * Returns true if the mouse is inside the game window
	 * 
	 * @return true if the mouse is inside the game window
	 */
	public boolean isInside() {
		if (mouseRawX == OUT_VAL && mouseRawY == OUT_VAL)
			return false;
		return true;
	}

	/**
	 * The definition of the object being dragged
	 * 
	 * @return
	 */
	public EAdSceneElementDef getDraggingElement() {
		if (draggingGameObject != null)
			return draggingGameObject.getElement().getDefinition();
		else
			return null;
	}

	/**
	 * Returns the x coordinate for the mouse, using the platform scale
	 * 
	 * @return
	 */
	public int getMouseScaledX() {
		return gameState.getValueMap().getValue(SystemFields.MOUSE_X);
	}

	/**
	 * Returns the y coordinate for the mouse, using the platform scale
	 * 
	 * @return
	 */
	public int getMouseScaledY() {
		return gameState.getValueMap().getValue(SystemFields.MOUSE_Y);
	}

	/**
	 * Returns the game object created to represent the drag. This is not the
	 * original game object dragged.
	 * 
	 * @return the game object that is being dragged, null if no object is being
	 *         dragged
	 */
	public SceneElementGO<?> getDraggingGameObject() {
		return draggingGameObject;
	}

	/**
	 * Set the game object that is being dragged, null to clear the object
	 * 
	 * @param draggingElement
	 *            the object that is being dragged, null to clear the object
	 * @param offsetX
	 * @param offsetY
	 */
	public void setDraggingGameObject(SceneElementGO<?> dragElementGO) {

		if (dragElementGO != null) {
			draggingGameObject = dragElementGO;
			EAdSceneElement sceneElement = dragElementGO.getElement();

			initDragX = gameState.getValueMap().getValue(sceneElement,
					SceneElement.VAR_X);
			initDragY = gameState.getValueMap().getValue(sceneElement,
					SceneElement.VAR_Y);

			int mouseVirtualX = gameState.getValueMap().getValue(
					SystemFields.MOUSE_SCENE_X);
			int mouseVirtualY = gameState.getValueMap().getValue(
					SystemFields.MOUSE_SCENE_Y);
			diffX = mouseVirtualX - initDragX;
			diffY = mouseVirtualY - initDragY;

			initZ = gameState.getValueMap().getValue(sceneElement,
					SceneElement.VAR_Z);

			gameState.getValueMap().setValue(sceneElement, SceneElement.VAR_Z,
					DRAG_Z);

		} else {
			if (draggingGameObject != null) {
				EAdSceneElement sceneElement = draggingGameObject.getElement();
				gameState.getValueMap().setValue(sceneElement,
						SceneElement.VAR_Z, initZ);
				if (gameState.getValueMap().getValue(sceneElement,
						SceneElement.VAR_RETURN_WHEN_DRAGGED)) {
					gameState.getValueMap().setValue(sceneElement,
							SceneElement.VAR_X, initDragX);
					gameState.getValueMap().setValue(sceneElement,
							SceneElement.VAR_Y, initDragY);
				}
			}
			draggingGameObject = null;
		}
	}

	/**
	 * Updates the system variables. Any time a system variable is added to
	 * {@link SystemFields}, its update must be added in here
	 */
	public void setPosition(int x, int y) {
		this.mouseRawX = x;
		this.mouseRawY = y;

		if (initialTransformation != null) {
			// Mouse
			float mouse[] = initialTransformation.getMatrix()
					.multiplyPointInverse(x, y, true);
			gameState.getValueMap().setValue(SystemFields.MOUSE_X,
					(int) mouse[0]);
			gameState.getValueMap().setValue(SystemFields.MOUSE_Y,
					(int) mouse[1]);

			if (gameState.getScene() != null) {
				EAdTransformation t = gameState.getScene().getTransformation();
				if (t != null) {
					mouse = t.getMatrix().multiplyPointInverse(x, y, true);
				}
			}

			gameState.getValueMap().setValue(SystemFields.MOUSE_SCENE_X,
					(int) mouse[0]);
			gameState.getValueMap().setValue(SystemFields.MOUSE_SCENE_Y,
					(int) mouse[1]);
		}

		updateDrag();

	}

	public boolean checkState(MouseState state) {
		if (state.isInside()
				&& (state.getMouseButton() == MouseGEvButtonType.NO_BUTTON || state
						.getMouseButton() == null)) {
			return isInside();
		} else if (state.isInside() && state.isPressed()
				&& this.isMousePressed(state.getMouseButton())) {
			return isInside();
		}
		return false;
	}

	public void setInitialTransformation(EAdTransformation t) {
		this.initialTransformation = t;
	}

}

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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.DragGEvType;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.Renderable;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.input.actions.DragInputAction;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.input.states.KeyboardState;
import ead.engine.core.input.states.MouseState;
import ead.engine.core.util.EAdTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class InputHandlerImpl implements InputHandler {

	/**
	 * Logger
	 */
	protected static final Logger logger = LoggerFactory.getLogger("InputHandler");

	/**
	 * Maximum number of events to be processes per cycle
	 */
	private int MAX_EVENTS_PER_CYCLE = 10;

	/**
	 * Maximum number of events that can be in the queue
	 */
	private int MAX_EVENTS_IN_QUEUE = 30;

	private MouseHandler mouseHandler;

	private KeyboardHandler keyboardHandler;

	private GameState gameState;

	private GameObjectManager gameObjects;

	private boolean checkDrag = true;

	// FIXME this must be configurable
	private boolean propagateEvents = true;

	@Inject
	public InputHandlerImpl(GameState gameState, GameObjectManager gameObjects) {
		mouseHandler = new MouseHandler(gameState);
		keyboardHandler = new KeyboardHandler();
		this.gameObjects = gameObjects;
		this.gameState = gameState;
	}

	@Override
	public void addAction(InputAction<?> action) {
		if (action instanceof MouseInputAction) {
			mouseHandler.getMouseEvents().add((MouseInputAction) action);
		} else if (action instanceof KeyInputAction) {
			keyboardHandler.getKeyActions().add((KeyInputAction) action);
		}
	}

	@Override
	public void processActions() {
		if (gameState.getValueMap().getValue(SystemFields.PROCESS_INPUT)) {
			// Keyboard
			processKeyActions();

			// Mouse
			processMouseActions();
			processEnterExit();
			processDrag();
		} else {
			clearAllInputs();
		}
	}

	/**
	 * Process keyboard actions
	 */
	private void processKeyActions() {
		int j = 0;
		while ( ! keyboardHandler.getKeyActions().isEmpty()
                && (j < MAX_EVENTS_PER_CYCLE
                        || keyboardHandler.getKeyActions().size()
                            > MAX_EVENTS_IN_QUEUE)) {

			KeyInputAction action = keyboardHandler.getKeyActions().poll();

			DrawableGO<?> go = gameState.getActiveElement();

            // first, the active element gets a try at consuming it
            if (go != null)
				go.processAction(action);

            // then, all elements get a try
			int i = gameObjects.getGameObjects().size() - 1;
			while (propagateEvents && !action.isConsumed() && i >= 0) {
				go = gameObjects.getGameObjects().get(i);
				go.processAction(action);
				i--;
			}

			if (action.isConsumed() && go != null) {
				logger.info("Action {} consumed by {}", action, go);
			} else {
				logger.info("Action {} not consumed by any element", action);
			}
			j++;
		}
	}

	/**
	 * Process actions (i.e. button presses) of the mouse
	 */
	private void processMouseActions() {
		int j = 0;
		while (!mouseHandler.getMouseEvents().isEmpty()
				&& (j < MAX_EVENTS_PER_CYCLE || mouseHandler.getMouseEvents()
						.size() > MAX_EVENTS_IN_QUEUE)) {

			MouseInputAction action = mouseHandler.getMouseEvents().poll();
			MouseInputAction mouseAction = (MouseInputAction) action;
			int x = mouseAction.getVirtualX();
			int y = mouseAction.getVirtualY();
			mouseHandler.setPosition(x, y);
			switch (mouseAction.getType()) {
			case MOVED:
				action.consume();
				break;
			case PRESSED:
				mouseHandler.setMousePressed(true, mouseAction.getButton());
				break;
			case RELEASED:
				mouseHandler.setMousePressed(false, mouseAction.getButton());
				break;
			}

			int i = gameObjects.getGameObjects().size() - 1;

			while (!action.isConsumed() && i >= 0) {
				DrawableGO<?> go = gameObjects.getGameObjects().get(i);
				EAdTransformation t = gameObjects.getTransformations().get(i);

				if (contains(go, x, y, t)) {
					go.processAction(action);
				}

				if (action.isConsumed())
					logger.info("Action {} consumed by {}", action, go);

				i = propagateEvents ? i - 1 : -1;
			}

			j++;
		}
	}

	/**
	 * Process movements to the mouse pointer
	 */
	private void processEnterExit() {
		DrawableGO<?> oldGO = mouseHandler.getGameObjectUnderMouse();
		DrawableGO<?> currentGO = getGOUnderMouse();
		if (oldGO != currentGO) {
			SceneElementGO<?> draggedGO = mouseHandler.getDraggingGameObject();
			int x = mouseHandler.getMouseScaledX();
			int y = mouseHandler.getMouseScaledY();

			if (oldGO != null) {
				MouseInputAction exitAction = new MouseInputAction(
						MouseGEv.MOUSE_EXITED, x, y);
				oldGO.processAction(exitAction);

				if (draggedGO != null) {
					DragInputAction action = new DragInputAction(
							mouseHandler.getDraggingElement(),
							DragGEvType.EXITED, x, y);
					oldGO.processAction(action);
				}
			}

			if (currentGO != null) {
				MouseInputAction enterAction = new MouseInputAction(
						MouseGEv.MOUSE_ENTERED, x, y);
				currentGO.processAction(enterAction);
				if (draggedGO != null) {
					DragInputAction action = new DragInputAction(
							mouseHandler.getDraggingElement(),
							DragGEvType.ENTERED, x, y);
					currentGO.processAction(action);
				}
			}
			mouseHandler.setGameObjectUnderMouse(currentGO);
		}
	}

	@Override
	public boolean checkState(InputState state) {
		if (state instanceof KeyboardState) {
			return keyboardHandler.checkState((KeyboardState) state);
		} else if (state instanceof MouseState) {
			return mouseHandler.checkState((MouseState) state);
		}
		return false;
	}

	@Override
	public DrawableGO<?> getGameObjectUnderPointer() {
		return mouseHandler.getGameObjectUnderMouse();
	}

	@Override
	public SceneElementGO<?> getDraggingGameObject() {
		return mouseHandler.getDraggingGameObject();
	}

	@Override
	public int getPointerX() {
		return mouseHandler.getMouseScaledX();
	}

	@Override
	public int getPointerY() {
		return mouseHandler.getMouseScaledY();
	}

	@Override
	public EAdSceneElementDef getDraggingElement() {
		return mouseHandler.getDraggingElement();
	}

	private boolean contains(DrawableGO<?> go, int x, int y, EAdTransformation t) {
		if (go.isEnable()) {
			float[] mouse = t.getMatrix().multiplyPointInverse(x, y, true);
			return ((Renderable) go).contains((int) mouse[0], (int) mouse[1]);

		}
		return false;
	}

	private DrawableGO<?> getGOUnderMouse() {
		if (checkState(MouseState.POINTER_INSIDE)) {
			for (int i = gameObjects.getGameObjects().size() - 1; i >= 0; i--) {
				DrawableGO<?> tempGameObject = gameObjects.getGameObjects()
						.get(i);
				if (tempGameObject != mouseHandler.getDraggingGameObject()) {
					EAdTransformation t = gameObjects.getTransformations().get(
							i);
					if (contains(tempGameObject, mouseHandler.getMouseX(),
							mouseHandler.getMouseY(), t)) {
						return tempGameObject;
					}
				}
			}
		}
		return null;
	}

	private void processDrag() {
		DrawableGO<?> currentDraggedGO = mouseHandler.getDraggingGameObject();
		int x = getPointerX();
		int y = getPointerY();
		if (currentDraggedGO != null) {
			if (!checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				DrawableGO<?> goMouse = getGameObjectUnderPointer();
				MouseInputAction drop = new MouseInputAction(
						MouseGEv.MOUSE_DROP, x, y);
				currentDraggedGO.processAction(drop);
				if (goMouse != null) {
					// Exit too
					DragInputAction action = new DragInputAction(
							mouseHandler.getDraggingElement(),
							DragGEvType.EXITED, x, y);
					goMouse.processAction(action);
					// Drop
					DragInputAction action2 = new DragInputAction(
							mouseHandler.getDraggingElement(),
							DragGEvType.DROP, x, y);
					goMouse.processAction(action2);
				}
				mouseHandler.setDraggingGameObject(null);
				checkDrag = true;
			}
		} else {
			if (checkDrag && checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				checkDrag = false;
				DrawableGO<?> go = getGameObjectUnderPointer();
				if (go != null) {
					SceneElementGO<?> draggedGO = go.getDraggableElement();
					if (draggedGO != null) {
						mouseHandler.setDraggingGameObject(draggedGO);
						mouseHandler.getDraggingGameObject().processAction(
								new MouseInputAction(
										MouseGEv.MOUSE_START_DRAG, x, y));
					}
				}

			} else if (!checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				checkDrag = true;
			}
		}

	}

	@Override
	public int getRawPointerX() {
		return mouseHandler.getMouseX();
	}

	@Override
	public int getRawPointerY() {
		return mouseHandler.getMouseY();
	}

	public void setInitialTransformation(EAdTransformation initialTransformation) {
		mouseHandler.setInitialTransformation(initialTransformation);
	}

	@Override
	public void clearAllInputs() {
		keyboardHandler.getKeyActions().clear();
		if (!mouseHandler.getMouseEvents().isEmpty()) {
			MouseInputAction action = mouseHandler.getMouseEvents().poll();
			mouseHandler
					.setPosition(action.getVirtualX(), action.getVirtualY());
		}
		mouseHandler.getMouseEvents().clear();

	}

}

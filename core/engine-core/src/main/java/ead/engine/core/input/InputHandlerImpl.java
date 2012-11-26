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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.DragGEvType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.input.actions.DragInputAction;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.input.states.KeyboardState;
import ead.engine.core.input.states.MouseState;
import ead.engine.core.platform.GUI;
import ead.engine.core.tracking.GameTracker;
import ead.engine.core.util.EAdTransformation;

@Singleton
public class InputHandlerImpl implements InputHandler {

	/**
	 * Logger
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger("InputHandler");

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

	private boolean checkDrag = true;

	private GameTracker tracker;

	private GUI gui;

	private boolean processInput;

	@Inject
	public InputHandlerImpl(GameState gameState, GameTracker tracker,
			GUI gui) {
		mouseHandler = new MouseHandler(gameState);
		keyboardHandler = new KeyboardHandler();
		this.gameState = gameState;
		this.tracker = tracker;
		this.gui = gui;
	}

	public boolean isProcessingInput() {
		return processInput;
	}

	@Override
	public void addAction(InputAction<?> action) {
		if (action instanceof MouseInputAction) {
			mouseHandler.getMouseEvents().add(
					(MouseInputAction) action);
		} else if (action instanceof KeyInputAction) {
			keyboardHandler.getKeyActions().add(
					(KeyInputAction) action);
		}
	}

	@Override
	public void processActions() {

		processInput = gameState.getValueMap().getValue(
				SystemFields.PROCESS_INPUT);
		if (processInput) {

			processKeyActions();
			processEnterExit();
			processDrag();
		}

		processMouseActions(processInput);
	}

	/**
	 * Process keyboard actions
	 */
	private void processKeyActions() {
		int j = 0;
		while (!keyboardHandler.getKeyActions().isEmpty()
				&& (j < MAX_EVENTS_PER_CYCLE || keyboardHandler
						.getKeyActions().size() > MAX_EVENTS_IN_QUEUE)) {

			KeyInputAction action = keyboardHandler.getKeyActions()
					.poll();

			DrawableGO<?> go = hudProcess(action);
			if (go == null) {
				go = gameState.getActiveElement();
				// only the active element gets a try to consume it
				if (go != null) {
					go.processAction(action);
				}
			}

			if (action.isConsumed() && go != null) {
				tracker.track(action, go);
				logger.info("Action {} consumed by {}", action, go);
			} else {
				logger.info("Action {} not consumed by any element",
						action);

			}
			j++;
		}
	}

	/**
	 * Process actions (i.e. button presses) of the mouse
	 * 
	 * @param processInput
	 */
	private void processMouseActions(boolean processInput) {
		int j = 0;
		while (!mouseHandler.getMouseEvents().isEmpty()
				&& (j < MAX_EVENTS_PER_CYCLE || mouseHandler
						.getMouseEvents().size() > MAX_EVENTS_IN_QUEUE)) {

			MouseInputAction action = mouseHandler.getMouseEvents()
					.poll();
			MouseInputAction mouseAction = (MouseInputAction) action;
			int x = mouseAction.getVirtualX();
			int y = mouseAction.getVirtualY();
			mouseHandler.setPosition(x, y);
			switch (mouseAction.getType()) {
			case MOVED:
				action.consume();
				break;
			case PRESSED:
				mouseHandler.setMousePressed(true,
						mouseAction.getButton());
				break;
			case RELEASED:
				mouseHandler.setMousePressed(false,
						mouseAction.getButton());
				break;
			default:
				break;
			}

			DrawableGO<?> go = hudProcess(action);

			if (!action.isConsumed()) {
				if (processInput) {
					go = gameState.getScene().processAction(action);
				}
			}

			if (go != null) {
				tracker.track(action, go);
			}

			if (action.isConsumed() && go != null
					&& mouseAction.getType() == MouseGEvType.PRESSED) {
				logger.info("Action {} consumed by {}", action, go);
			} else if (mouseAction.getType() == MouseGEvType.PRESSED) {
				logger.info("Action {} not consumed by any element",
						action);
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
			SceneElementGO<?> draggedGO = mouseHandler
					.getDraggingGameObject();
			int x = mouseHandler.getMouseScaledX();
			int y = mouseHandler.getMouseScaledY();

			if (oldGO != null) {
				MouseInputAction exitAction = new MouseInputAction(
						MouseGEv.MOUSE_EXITED, x, y);
				oldGO.processAction(exitAction);
				tracker.track(exitAction, oldGO);

				if (draggedGO != null) {
					DragInputAction action = new DragInputAction(
							mouseHandler.getDraggingElement(),
							DragGEvType.EXITED, x, y);

					oldGO.processAction(action);
					tracker.track(exitAction, oldGO);
				}
			}

			if (currentGO != null) {
				MouseInputAction enterAction = new MouseInputAction(
						MouseGEv.MOUSE_ENTERED, x, y);
				currentGO.processAction(enterAction);
				tracker.track(enterAction, currentGO);
				if (draggedGO != null) {
					DragInputAction action = new DragInputAction(
							mouseHandler.getDraggingElement(),
							DragGEvType.ENTERED, x, y);
					currentGO.processAction(action);
					tracker.track(action, currentGO);
				}
			}
			mouseHandler.setGameObjectUnderMouse(currentGO);
		}
	}

	private DrawableGO<?> hudProcess(InputAction<?> a) {
		DrawableGO<?> go = null;
		int i = gui.getHUDs().size() - 1;
		while (!a.isConsumed() && i >= 0) {
			go = gui.getHUDs().get(i--).processAction(a);
		}
		return go;
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

	private DrawableGO<?> getGOUnderMouse() {
		if (checkState(MouseState.POINTER_INSIDE)) {
			return gameState.getScene().getFirstGOIn(
					mouseHandler.getMouseX(),
					mouseHandler.getMouseY());
		}
		return null;
	}

	private void processDrag() {
		DrawableGO<?> currentDraggedGO = mouseHandler
				.getDraggingGameObject();
		DrawableGO<?> currentGO = getGameObjectUnderPointer();
		int x = getPointerX();
		int y = getPointerY();
		if (currentDraggedGO != null) {
			if (!checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				List<DrawableGO<?>> goMouseList = getAllGOUnderMouse();
				MouseInputAction drop = new MouseInputAction(
						MouseGEv.MOUSE_DROP, x, y);
				currentDraggedGO.processAction(drop);
				tracker.track(drop, currentDraggedGO);

				if (goMouseList.size() > 0) {
					DrawableGO<?> goMouse = null;
					int i = 0;

					// Exit too
					if (currentGO != null) {
						DragInputAction action = new DragInputAction(
								mouseHandler.getDraggingElement(),
								DragGEvType.EXITED, x, y);

						currentGO.processAction(action);
						tracker.track(action, currentGO);

					}

					// Drop
					DragInputAction action2 = new DragInputAction(
							mouseHandler.getDraggingElement(),
							DragGEvType.DROP, x, y);
					i = 0;
					while (i < goMouseList.size()) {
						goMouse = goMouseList.get(i);
						goMouse.processAction(action2);
						tracker.track(action2, goMouse);
						i++;
					}
				}
				mouseHandler.setDraggingGameObject(null);
				checkDrag = true;
			}
		} else {
			if (checkDrag
					&& checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				checkDrag = false;
				List<DrawableGO<?>> goList = getAllGOUnderMouse();
				boolean dragFound = false;
				int i = 0;
				while (!dragFound && i < goList.size()) {
					DrawableGO<?> go = goList.get(i);
					SceneElementGO<?> draggedGO = go
							.getDraggableElement();
					if (draggedGO != null) {
						mouseHandler.setDraggingGameObject(draggedGO);
						MouseInputAction action = new MouseInputAction(
								MouseGEv.MOUSE_START_DRAG, x, y);
						mouseHandler.getDraggingGameObject()
								.processAction(action);
						tracker.track(action,
								mouseHandler.getDraggingGameObject());
						dragFound = true;
					}
					i++;
				}

			} else if (!checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				checkDrag = true;
			}
		}

	}

	private List<DrawableGO<?>> getAllGOUnderMouse() {
		if (checkState(MouseState.POINTER_INSIDE)) {
			return gameState.getScene().getAllGOIn(
					mouseHandler.getMouseX(),
					mouseHandler.getMouseY());
		}
		return null;
	}

	@Override
	public int getRawPointerX() {
		return mouseHandler.getMouseX();
	}

	@Override
	public int getRawPointerY() {
		return mouseHandler.getMouseY();
	}

	public void setInitialTransformation(
			EAdTransformation initialTransformation) {
		mouseHandler.setInitialTransformation(initialTransformation);
	}

	@Override
	public void clearAllInputs() {
		keyboardHandler.getKeyActions().clear();
		if (!mouseHandler.getMouseEvents().isEmpty()) {
			MouseInputAction action = mouseHandler.getMouseEvents()
					.poll();
			mouseHandler.setPosition(action.getVirtualX(),
					action.getVirtualY());
		}
		mouseHandler.getMouseEvents().clear();

	}

}

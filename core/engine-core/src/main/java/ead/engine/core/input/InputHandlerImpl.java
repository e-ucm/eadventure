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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.DragGEvType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
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

	private List<SceneElementGO<?>> mouseUnder;

	@Inject
	public InputHandlerImpl(GameState gameState, GameTracker tracker, GUI gui) {
		mouseHandler = new MouseHandler(gameState, gui);
		keyboardHandler = new KeyboardHandler();
		this.gameState = gameState;
		this.tracker = tracker;
		this.gui = gui;
		mouseUnder = new ArrayList<SceneElementGO<?>>();
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
		processKeyActions();
		processEnterExit();
		processDrag();
		processMouseActions();
	}

	/**
	 * Process keyboard actions
	 */
	private void processKeyActions() {
		int j = 0;
		while (!keyboardHandler.getKeyActions().isEmpty()
				&& (j < MAX_EVENTS_PER_CYCLE || keyboardHandler.getKeyActions()
						.size() > MAX_EVENTS_IN_QUEUE)) {

			KeyInputAction action = keyboardHandler.getKeyActions().poll();

			SceneElementGO<?> go = gui.processAction(action);

			if (action.isConsumed() && go != null) {
				tracker.track(action, go);
				logger.info("Action {} consumed by {}", action, go);
			} else {
				logger.info("Action {} not consumed by any element", action);

			}
			j++;
		}
	}

	/**
	 * Process actions (i.e. button presses) of the mouse
	 * 
	 * @param processInput
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
			default:
				break;
			}

			SceneElementGO<?> go = gui.processAction(mouseAction);

			if (go != null) {
				tracker.track(action, go);
			}

			if (action.isConsumed() && go != null
					&& mouseAction.getType() == MouseGEvType.PRESSED) {
				logger.info("Action {} consumed by {}", action, go);
			} else if (mouseAction.getType() == MouseGEvType.PRESSED) {
				if (action.isConsumed()) {
					logger.info("Action consumed by engine filter.", action);
				} else {
					logger
							.info("Action {} not consumed by any element",
									action);
				}
			}

			j++;
		}
	}

	/**
	 * Process movements to the mouse pointer
	 */
	private void processEnterExit() {
		SceneElementGO<?> oldGO = mouseHandler.getGameObjectUnderMouse();
		SceneElementGO<?> currentGO = getGOUnderMouse();
		if (oldGO != currentGO) {
			SceneElementGO<? extends EAdSceneElement> draggedGO = mouseHandler
					.getDraggingGameObject();
			int x = mouseHandler.getMouseScaledX();
			int y = mouseHandler.getMouseScaledY();

			if (oldGO != null) {
				MouseInputAction exitAction = new MouseInputAction(
						MouseGEv.MOUSE_EXITED, x, y);
				oldGO.processAction(exitAction);
				tracker.track(exitAction, oldGO);
				gameState.setValue(oldGO.getElement(),
						SceneElement.VAR_MOUSE_OVER, false);

				if (draggedGO != null) {
					DragInputAction action = new DragInputAction(mouseHandler
							.getDraggingElement(), DragGEvType.EXITED, x, y);

					oldGO.processAction(action);
					tracker.track(exitAction, oldGO);
				}
			}

			if (currentGO != null) {
				MouseInputAction enterAction = new MouseInputAction(
						MouseGEv.MOUSE_ENTERED, x, y);
				currentGO.processAction(enterAction);
				gameState.setValue(currentGO.getElement(),
						SceneElement.VAR_MOUSE_OVER, true);
				tracker.track(enterAction, currentGO);
				if (draggedGO != null) {
					DragInputAction action = new DragInputAction(mouseHandler
							.getDraggingElement(), DragGEvType.ENTERED, x, y);
					currentGO.processAction(action);
					tracker.track(action, currentGO);
				}
			}
		}
		mouseHandler.setGameObjectUnderMouse(currentGO);
		gameState.setValue(SystemFields.MOUSE_OVER_ELEMENT,
				currentGO == null ? null : currentGO.getElement());
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
	public SceneElementGO<?> getGameObjectUnderPointer() {
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

	private SceneElementGO<?> getGOUnderMouse() {
		if (checkState(MouseState.POINTER_INSIDE)) {
			return gui.getGameObjectIn(mouseHandler.getMouseX(), mouseHandler
					.getMouseY());
		}
		return null;
	}

	private void processDrag() {
		SceneElementGO<?> currentDraggedGO = mouseHandler
				.getDraggingGameObject();
		SceneElementGO<?> currentGO = getGameObjectUnderPointer();
		int x = getPointerX();
		int y = getPointerY();
		if (currentDraggedGO != null) {
			if (!checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				List<SceneElementGO<?>> goMouseList = getAllGOUnderMouse();
				MouseInputAction drop = new MouseInputAction(
						MouseGEv.MOUSE_DROP, x, y);
				currentDraggedGO.processAction(drop);
				tracker.track(drop, currentDraggedGO);

				if (goMouseList.size() > 0) {
					SceneElementGO<?> goMouse = null;
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
					DragInputAction action2 = new DragInputAction(mouseHandler
							.getDraggingElement(), DragGEvType.DROP, x, y);
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
			if (checkDrag && checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				checkDrag = false;
				List<SceneElementGO<?>> goList = getAllGOUnderMouse();
				boolean dragFound = false;
				int i = 0;
				while (!dragFound && i < goList.size()) {
					SceneElementGO<?> go = goList.get(i);
					SceneElementGO<?> draggedGO = null;
					if (go.isDraggable()) {
						draggedGO = go;
					}
					if (draggedGO != null) {
						mouseHandler.setDraggingGameObject(draggedGO);
						MouseInputAction action = new MouseInputAction(
								MouseGEv.MOUSE_START_DRAG, x, y);
						mouseHandler.getDraggingGameObject().processAction(
								action);
						tracker.track(action, mouseHandler
								.getDraggingGameObject());
						dragFound = true;
					}
					i++;
				}

			} else if (!checkState(MouseState.LEFT_BUTTON_PRESSED)) {
				checkDrag = true;
			}
		}

	}

	private List<SceneElementGO<?>> getAllGOUnderMouse() {
		mouseUnder.clear();
		if (checkState(MouseState.POINTER_INSIDE)) {
			gui.getScene().getAllGOIn(mouseHandler.getMouseX(),
					mouseHandler.getMouseY(), mouseUnder);
		}
		return mouseUnder;
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

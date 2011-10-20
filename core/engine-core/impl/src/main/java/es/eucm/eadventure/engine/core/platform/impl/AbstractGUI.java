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

package es.eucm.eadventure.engine.core.platform.impl;

import java.util.logging.Logger;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.guievents.EAdDragEvent.DragAction;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.Renderable;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.impl.DrawableGameObject;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.guiactions.impl.DragActionImpl;
import es.eucm.eadventure.engine.core.guiactions.impl.MouseActionImpl;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdMatrixImpl;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;

/**
 * <p>
 * Abstract implementation of the GUI (Graphic User Interface) for the
 * eAdventure 2 games
 * </p>
 * 
 * @param <T>
 *            A parameter for the graphic context of the GUI (e.g. in AWT Java
 *            it will be Graphics2D)
 */
public abstract class AbstractGUI<T> implements GUI {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger("AbstractGUI");

	/**
	 * Maximum number of events to be processes per cycle
	 */
	private int MAX_EVENTS_PER_CYCLE = 10;

	/**
	 * Maximum number of events that can be in the queue
	 */
	private int MAX_EVENTS_IN_QUEUE = 30;

	/**
	 * Platform configuration parameters
	 */
	protected PlatformConfiguration platformConfiguration;

	/**
	 * Game object manager
	 */
	protected GameObjectManager gameObjects;

	/**
	 * The current mouse state
	 */
	protected MouseState mouseState;

	/**
	 * The current keyboard state
	 */
	protected KeyboardState keyboardState;

	protected GameState gameState;

	protected GameObjectFactory gameObjectFactory;

	protected EAdCanvas<T> eAdCanvas;

	private boolean checkDrag;

	
	public AbstractGUI(PlatformConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, MouseState mouseState,
			KeyboardState keyboardState,
			GameState gameState, GameObjectFactory gameObjectFactory,
			EAdCanvas<T> canvas) {
		this.platformConfiguration = platformConfiguration;
		this.gameObjects = gameObjectManager;
		this.mouseState = mouseState;
		this.keyboardState = keyboardState;
		this.gameState = gameState;
		this.gameObjectFactory = gameObjectFactory;
		this.eAdCanvas = canvas;
		checkDrag = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.GUI#addElement(es.eucm.eadventure
	 * .engine.core.gameobjects.GameObject)
	 * 
	 * The element should not be offset as it is being dragged in the scene
	 */
	@Override
	public void addElement(GameObject<?> element,
			EAdTransformation transformation) {
		EAdTransformation t = element.getTransformation();
		if (t != null) {
			EAdTransformation tResult = addTransformation(transformation, t);
			if (tResult.isVisible()) {
				gameObjects.add(element, tResult);
				element.doLayout(tResult);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#prepareGUI()
	 */
	@Override
	public void prepareGUI(EAdTransformation t) {
		if (gameObjects.getHUD() != null) {
			gameObjects.add(gameObjects.getHUD(), t);
			gameObjects.getHUD().update();
			gameObjects.getHUD().doLayout(t);
		}

		gameObjects.swap();

		if (mouseState.getDraggingGameObject() != null) {
			if (!gameObjects.getGameObjects().contains(
					mouseState.getDraggingGameObject())) {
				mouseState.setDraggingGameObject(null);
			} else {
				int pos = gameObjects.getGameObjects().indexOf(
						mouseState.getDraggingGameObject());
				if (pos != -1) {
					gameObjects.getGameObjects().remove(pos);
					gameObjects.getTransformations().remove(pos);
				}
			}
		}
	}

	/**
	 * Process the different sort of inputs received by the game
	 */
	protected void processInput() {

		processMouseMovement();

		processDrag();

		processMouseActions();

		processKeyActions();
	}

	/**
	 * Process movements to the mouse pointer
	 */
	private void processMouseMovement() {
		GameObject<?> oldGO = mouseState.getGameObjectUnderMouse();
		GameObject<?> currentGO = getGOUnderMouse();
		if (oldGO != currentGO) {
			GameObject<?> draggedGO = mouseState.getDraggingGameObject();
			int x = mouseState.getMouseX();
			int y = mouseState.getMouseY();

			if (oldGO != null) {
				MouseActionImpl exitAction = new MouseActionImpl(
						EAdMouseEventImpl.MOUSE_EXITED, x, y);
				oldGO.processAction(exitAction);

				if (draggedGO != null) {
					DragActionImpl action = new DragActionImpl(
							(EAdElement) draggedGO.getElement(),
							DragAction.EXITED, x, y);
					oldGO.processAction(action);
				}
			}

			if (currentGO != null) {
				MouseActionImpl enterAction = new MouseActionImpl(
						EAdMouseEventImpl.MOUSE_ENTERED, x, y);
				currentGO.processAction(enterAction);
				if (draggedGO != null) {
					DragActionImpl action = new DragActionImpl(
							(EAdElement) draggedGO.getElement(),
							DragAction.ENTERED, x, y);
					currentGO.processAction(action);
				}
			}
			mouseState.setGameObjectUnderMouse(currentGO);
		}
	}

	private void processDrag() {
		GameObject<?> currentDraggedGO = mouseState.getDraggingGameObject();
		int x = mouseState.getMouseX();
		int y = mouseState.getMouseY();
		if (currentDraggedGO != null) {
			if (!mouseState.isMousePressed()) {
				GameObject<?> goMouse = mouseState.getGameObjectUnderMouse();
				if (goMouse != null) {
					// Exit too
					DragActionImpl action = new DragActionImpl(
							(EAdElement) currentDraggedGO.getElement(),
							DragAction.EXITED, x, y);
					goMouse.processAction(action);
					// Drop
					DragActionImpl action2 = new DragActionImpl(
							(EAdElement) currentDraggedGO.getElement(),
							DragAction.DROP, x, y);
					goMouse.processAction(action2);
				}
				MouseActionImpl drop = new MouseActionImpl(
						EAdMouseEventImpl.MOUSE_DROP, x, y);
				currentDraggedGO.processAction(drop);
				mouseState.setDraggingGameObject(null);
				checkDrag = true;
			}
		} else {
			if (checkDrag && mouseState.isMousePressed()) {
				checkDrag = false;
				GameObject<?> go = mouseState.getGameObjectUnderMouse();
				if (go != null) {
					GameObject<?> draggedGO = go
							.getDraggableElement(mouseState);
					if (draggedGO != null) {
						mouseState.setDraggingGameObject(draggedGO);
						draggedGO.processAction(new MouseActionImpl(
								EAdMouseEventImpl.MOUSE_START_DRAG, x, y));
					}
				}

			} else if (!mouseState.isMousePressed()) {
				checkDrag = true;
			}
		}

	}

	private GameObject<?> getGOUnderMouse() {
		if (mouseState.isInside()) {
			for (int i = gameObjects.getGameObjects().size() - 1; i >= 0; i--) {
				GameObject<?> tempGameObject = gameObjects.getGameObjects()
						.get(i);
				if (tempGameObject != mouseState.getDraggingGameObject()) {
					EAdTransformation t = gameObjects.getTransformations().get(
							i);
					if (contains(tempGameObject, mouseState.getMouseX(),
							mouseState.getMouseY(), t)) {
						return tempGameObject;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Process actions (i.e. button presses) of the mouse
	 */
	private void processMouseActions() {
		int j = 0;
		while (!mouseState.getMouseEvents().isEmpty()
				&& (j < MAX_EVENTS_PER_CYCLE || mouseState.getMouseEvents()
						.size() > MAX_EVENTS_IN_QUEUE)) {
			j++;
			MouseAction action = mouseState.getMouseEvents().poll();
			for (int i = gameObjects.getGameObjects().size() - 1; i >= 0; i--) {
				GameObject<?> gameObject = gameObjects.getGameObjects().get(i);
				EAdTransformation t = gameObjects.getTransformations().get(i);
				if (contains(gameObject, action.getVirtualX(),
						action.getVirtualY(), t)) {
					logger.info("Action " + action + " passed to "
							+ (gameObject.getElement() + ""));
					gameObject.processAction(action);
				}
				if (action.isConsumed())
					break;
			}
		}
	}

	private boolean contains(GameObject<?> go, int x, int y, EAdTransformation t) {
		if (go.isEnable()) {
			float[] mouse = t.getMatrix().postMultiplyPointInverse(x, y);
			if (go instanceof DrawableGameObject<?>) {
				return ((DrawableGameObject<?>) go).contains((int) mouse[0], (int) mouse[1]);
			}
		}
		return false;
	}

	/**
	 * Process keyboard actions
	 */
	private void processKeyActions() {
		int j = 0;
		while (!keyboardState.getKeyActions().isEmpty()
				&& (j < MAX_EVENTS_PER_CYCLE || keyboardState.getKeyActions()
						.size() > MAX_EVENTS_IN_QUEUE)) {
			j++;
			processKeyAction(keyboardState.getKeyActions().poll());
		}
	}

	/**
	 * Process a specific keyboard action
	 * 
	 * @param action
	 *            The keyboard action
	 */
	protected void processKeyAction(KeyAction action) {
		if (gameState.getActiveElement() != null)
			gameObjectFactory.get(gameState.getActiveElement()).processAction(
					action);
		for (int i = gameObjects.getGameObjects().size() - 1; action != null
				&& i >= 0 && !action.isConsumed(); i--) {
			logger.info("Action " + action + " passed to "
					+ gameObjects.getGameObjects().get(i));
			gameObjects.getGameObjects().get(i).processAction(action);
		}
	}

	/**
	 * Render the game objects into the graphic context
	 * @param interpolation
	 *            The current interpolation between ideal game frames
	 */
	protected void render(float interpolation) {
		// TODO use interpolation
		synchronized (GameObjectManager.lock) {
			for (int i = 0; i < gameObjects.getGameObjects().size(); i++) {
				EAdTransformation t = gameObjects.getTransformations().get(i);
				eAdCanvas.setTransformation(t);
				((Renderable) gameObjects.getGameObjects().get(i))
						.render(eAdCanvas);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#getWidth()
	 */
	@Override
	public int getWidth() {
		return platformConfiguration.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#getHeight()
	 */
	@Override
	public int getHeight() {
		return platformConfiguration.getHeight();
	}

	public int[] getGameElementGUIOffset(GameObject<?> gameObject) {
		synchronized (GameObjectManager.lock) {
			int pos = gameObjects.getGameObjects().indexOf(gameObject);
			if (pos == -1)
				return null;
			EAdTransformation t = gameObjects.getTransformations().get(pos);
			int[] offset = new int[2];
			offset[0] = (int) t.getMatrix().getOffsetX();
			offset[1] = (int) t.getMatrix().getOffsetY();
			return offset;
		}
	}

	public void changeCursor(Image image) {

	}

	public EAdTransformation addTransformation(EAdTransformation t1,
			EAdTransformation t2) {
		EAdMatrixImpl m = new EAdMatrixImpl();
		m.postMultiply(t1.getMatrix().getFlatMatrix());
		m.postMultiply(t2.getMatrix().getFlatMatrix());
		float alpha = t1.getAlpha() * t2.getAlpha();
		boolean visible = t1.isVisible() && t2.isVisible();
		EAdTransformationImpl t = new EAdTransformationImpl(m, visible, alpha);
		return t;
	}

}

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

package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Logger;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.params.guievents.EAdKeyEvent.KeyActionType;
import es.eucm.eadventure.common.model.params.guievents.EAdKeyEvent.KeyCode;
import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.guiactions.DropAction;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.guiactions.impl.DropActionImpl;
import es.eucm.eadventure.engine.core.guiactions.impl.KeyActionImpl;
import es.eucm.eadventure.engine.core.guiactions.impl.MouseActionImpl;

/**
 * <p> This class listens to input by the user and converts it into the
 * engine input model </p>
 *
 */
public class DesktopInputListener implements MouseListener,
		MouseMotionListener, KeyListener {

	/**
	 * The state of the mouse
	 */
	private MouseState mouseState;

	/**
	 * The state of the keyboard
	 */
	private KeyboardState keyboardState;

	/**
	 * The logger
	 */
	private static final Logger logger = Logger
			.getLogger("DesktopInputListener");

	public DesktopInputListener(MouseState mouseState,
			KeyboardState keyboardState) {
		this.mouseState = mouseState;
		this.keyboardState = keyboardState;
		logger.info("New instance");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		MouseAction action = getMouseAction(e,
				mouseState.getVirtualMouseX(), mouseState.getVirtualMouseY(), true);
		if (action != null)
			mouseState.getMouseEvents().add(action);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseState.setMousePressed(true);
		mouseState.setMousePosition(e.getX(), e.getY());
		
		MouseAction action = getMouseAction(e,
				mouseState.getVirtualMouseX(), mouseState.getVirtualMouseY(), false);
		if (action != null)
			mouseState.getMouseEvents().add(action);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseState.setMousePressed(false);
		mouseState.setMousePosition(e.getX(), e.getY());
		if (mouseState.getDraggingGameObject() != null) {
			DropAction action = new DropActionImpl(mouseState.getVirtualMouseX(),
					mouseState.getVirtualMouseY(),
					(GameObject<? extends EAdElement>) mouseState.getDraggingGameObject());
			mouseState.setDraggingGameObject(null);
			mouseState.getMouseEvents().add(action);
		} 
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseState.setMousePosition(e.getX(), e.getY());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseState.setMousePosition(MouseState.OUT_VAL, MouseState.OUT_VAL);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseState.setMousePosition(e.getX(), e.getY());
		if (mouseState.getDraggingGameObject() == null
				&& mouseState.getGameObjectUnderMouse() != null) {
			mouseState.setDraggingGameObject(mouseState
					.getGameObjectUnderMouse().getDraggableElement(mouseState));
			MouseAction action = new MouseActionImpl(MouseActionType.DRAG,
					mouseState.getVirtualMouseX(), mouseState.getVirtualMouseY());
			mouseState.getMouseEvents().add(action);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseState.setMousePosition(e.getX(), e.getY());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keyboardState.getKeyActions().add(
				getKeyboardAction(
						KeyActionType.KEY_TYPED, e));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyboardState.getKeyActions().add(
				getKeyboardAction(
						KeyActionType.KEY_PRESSED, e));

		// TODO should be done by the GUI or the BasicHUDGO?
		/*
		 * if (e.getKeyCode() == KeyEvent.VK_UP) { Robot r; try { r = new
		 * Robot(); r.mouseMove(20, 20); } catch (AWTException e1) { 
		 * catch block e1.printStackTrace(); } }
		 */

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
	
	/**
	 * Get the GUI {@link KeyAction}, form the Java {@link KeyActionType}
	 * 
	 * @param actionType The action type
	 * @param keyEvent The key event
	 * @return The GUI {@link KeyAction}
	 */
	public KeyAction getKeyboardAction(KeyActionType actionType, KeyEvent keyEvent) {
		switch (keyEvent.getKeyCode()) {
		case KeyEvent.VK_UP:
			return new KeyActionImpl(actionType, KeyCode.ARROW_UP);
		case KeyEvent.VK_DOWN:
			return new KeyActionImpl(actionType, KeyCode.ARROW_DOWN);
		case KeyEvent.VK_LEFT:
			return new KeyActionImpl(actionType, KeyCode.ARROW_LEFT);
		case KeyEvent.VK_RIGHT:
			return new KeyActionImpl(actionType, KeyCode.ARROW_RIGHT);
		case KeyEvent.VK_ENTER:
			return new KeyActionImpl(actionType, KeyCode.RETURN);
		case KeyEvent.VK_ESCAPE:
			return new KeyActionImpl(actionType, KeyCode.ESC);
		}
		if (keyEvent.getKeyChar() != 0)
			return new KeyActionImpl(actionType, keyEvent.getKeyChar());
		return null;
	}
	
	/**
	 * Get the GUI {@link MouseAction} from the Java {@link MouseEvent}, given the position in the virtual GUI representation
	 * 
	 * @param e The Java {@link MouseEvent}
	 * @param virtualX The position along the X axis
	 * @param virtualY The position along the Y axis
	 * @param click True if the mouse button is clicked
	 * @return The GUI {@link MouseAction}
	 */
	public MouseAction getMouseAction(MouseEvent e, int virtualX, int virtualY, boolean click) {
		if (e.getButton() == MouseEvent.NOBUTTON) {
			return null; //new MouseActionImpl(MouseAction.MouseActionType.MOVED, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1 && !click) {
			return new MouseActionImpl(MouseActionType.PRESSED, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1 && click) {
			return new MouseActionImpl(MouseActionType.LEFT_CLICK, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && click) {
			return new MouseActionImpl(MouseActionType.DOUBLE_CLICK, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1 && click) {
			return new MouseActionImpl(MouseActionType.RIGHT_CLICK, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 0 && click) {
			return null;
		}
	 	return null;
		
	}


}

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

import es.eucm.eadventure.common.model.guievents.enums.KeyActionType;
import es.eucm.eadventure.common.model.guievents.enums.KeyCode;
import es.eucm.eadventure.common.model.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.guievents.enums.MouseActionType;
import es.eucm.eadventure.common.model.guievents.enums.MouseButton;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.guiactions.impl.KeyActionImpl;
import es.eucm.eadventure.engine.core.guiactions.impl.MouseActionImpl;

/**
 * <p>
 * This class listens to input by the user and converts it into the engine input
 * model
 * </p>
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
		MouseActionType action = MouseActionType.CLICK;
		if (e.getClickCount() == 2)
			action = MouseActionType.DOUBLE_CLICK;

		mouseState.getMouseEvents().add(
				getMouseAction(e, action, mouseState.getMouseX(),
						mouseState.getMouseY()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseState.setMousePressed(true);
		mouseState.setMousePosition(e.getX(), e.getY());
		mouseState.getMouseEvents().add(
				getMouseAction(e, MouseActionType.PRESSED,
						mouseState.getMouseX(), mouseState.getMouseY()));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseState.setMousePressed(false);
		mouseState.setMousePosition(e.getX(), e.getY());
		mouseState.getMouseEvents().add(
				getMouseAction(e, MouseActionType.RELEASED,
						mouseState.getMouseX(), mouseState.getMouseY()));
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
		mouseState.setMousePressed(true);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseState.setMousePosition(e.getX(), e.getY());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keyboardState.getKeyActions().add(
				getKeyboardAction(KeyActionType.KEY_TYPED, e));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyboardState.getKeyActions().add(
				getKeyboardAction(KeyActionType.KEY_PRESSED, e));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyboardState.getKeyActions().add(
				getKeyboardAction(KeyActionType.KEY_RELEASED, e));
	}

	/**
	 * Get the GUI {@link KeyAction}, form the Java {@link KeyActionType}
	 * 
	 * @param actionType
	 *            The action type
	 * @param keyEvent
	 *            The key event
	 * @return The GUI {@link KeyAction}
	 */
	public KeyAction getKeyboardAction(KeyActionType actionType,
			KeyEvent keyEvent) {
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
	 * Get the GUI {@link MouseAction} from the Java {@link MouseEvent}, given
	 * the position in the virtual GUI representation
	 * 
	 * @param e
	 *            The Java {@link MouseEvent}
	 * @param action
	 *            the performed action
	 * @param virtualX
	 *            The position along the X axis
	 * @param virtualY
	 *            The position along the Y axis
	 * @return The GUI {@link MouseAction}
	 */
	public MouseAction getMouseAction(MouseEvent e, MouseActionType action,
			int virtualX, int virtualY) {
		MouseButton b = getMouseButton(e.getButton());
		EAdMouseEvent event = EAdMouseEventImpl.getMouseEvent(action, b);
		return new MouseActionImpl(event, virtualX, virtualY);

	}

	private MouseButton getMouseButton(int button) {
		switch (button) {
		case MouseEvent.BUTTON1:
			return MouseButton.BUTTON_1;
		case MouseEvent.BUTTON2:
			return MouseButton.BUTTON_2;
		case MouseEvent.BUTTON3:
			return MouseButton.BUTTON_3;
		default:
			return MouseButton.NO_BUTTON;
		}
	}

}

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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.MouseHandler;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private InputHandler inputHandler;

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger("DesktopInputListener");

	public DesktopInputListener(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
		logger.info("New instance");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		MouseGEvType action = MouseGEvType.CLICK;
		if (e.getClickCount() == 2)
			action = MouseGEvType.DOUBLE_CLICK;

		inputHandler.addAction(getMouseAction(e, action, e.getX(),
				e.getY()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		inputHandler.addAction(getMouseAction(e, MouseGEvType.PRESSED,
				e.getX(), e.getY()));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		inputHandler.addAction(getMouseAction(e, MouseGEvType.RELEASED,
				e.getX(), e.getY()));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		inputHandler.addAction(getMouseAction(e, MouseGEvType.MOVED, e.getX(), e.getY()));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		inputHandler.addAction(getMouseAction(e, MouseGEvType.MOVED, MouseHandler.OUT_VAL, MouseHandler.OUT_VAL));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		inputHandler.addAction(getMouseAction(e, MouseGEvType.MOVED, e.getX(), e.getY()));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		inputHandler.addAction(getMouseAction(e, MouseGEvType.MOVED, e.getX(), e.getY()));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		inputHandler.addAction(getKeyboardAction(KeyEventType.KEY_TYPED, e));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		inputHandler
				.addAction(getKeyboardAction(KeyEventType.KEY_PRESSED, e));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		inputHandler
				.addAction(getKeyboardAction(KeyEventType.KEY_RELEASED, e));
	}

	/**
	 * Get the GUI {@link KeyAction}, form the Java {@link KeyEventType}
	 *
	 * @param actionType
	 *            The action type
	 * @param keyEvent
	 *            The key event
	 * @return The GUI {@link KeyAction}
	 */
	public KeyInputAction getKeyboardAction(KeyEventType actionType,
			KeyEvent keyEvent) {
		switch (keyEvent.getKeyCode()) {
		case KeyEvent.VK_UP:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_UP);
		case KeyEvent.VK_DOWN:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_DOWN);
		case KeyEvent.VK_LEFT:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_LEFT);
		case KeyEvent.VK_RIGHT:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_RIGHT);
		case KeyEvent.VK_ENTER:
			return new KeyInputAction(actionType, KeyGEvCode.RETURN);
		case KeyEvent.VK_ESCAPE:
			return new KeyInputAction(actionType, KeyGEvCode.ESC);
		}
		if (keyEvent.getKeyChar() != 0)
			return new KeyInputAction(actionType, keyEvent.getKeyChar());
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
	public MouseInputAction getMouseAction(MouseEvent e, MouseGEvType action,
			int virtualX, int virtualY) {
		MouseGEvButtonType b = getMouseButton(e.getButton());
		MouseGEv event = MouseGEv.getMouseEvent(action, b);
		return new MouseInputAction(event, virtualX, virtualY);

	}

	private MouseGEvButtonType getMouseButton(int button) {
		switch (button) {
		case MouseEvent.BUTTON1:
			return MouseGEvButtonType.BUTTON_1;
		case MouseEvent.BUTTON2:
			return MouseGEvButtonType.BUTTON_2;
		case MouseEvent.BUTTON3:
			return MouseGEvButtonType.BUTTON_3;
		default:
			return MouseGEvButtonType.NO_BUTTON;
		}
	}

}

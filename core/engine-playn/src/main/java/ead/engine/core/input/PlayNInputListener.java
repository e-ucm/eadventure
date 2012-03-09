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

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;

public class PlayNInputListener implements Mouse.Listener, Keyboard.Listener {

	private InputHandler inputHandler;

	public PlayNInputListener(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	@Override
	public void onMouseDown(ButtonEvent event) {
		inputHandler.addAction(new MouseInputAction(MouseGEv.getMouseEvent(
				MouseGEvType.PRESSED, getMouseButton2(event.button())),
				(int) event.x(), (int) event.y()));
	}

	@Override
	public void onMouseUp(ButtonEvent event) {
		inputHandler.addAction(new MouseInputAction(MouseGEv.getMouseEvent(
				MouseGEvType.RELEASED, getMouseButton2(event.button())),
				(int) event.x(), (int) event.y()));
		inputHandler.addAction(new MouseInputAction(MouseGEv.getMouseEvent(
				MouseGEvType.CLICK, getMouseButton2(event.button())),
				(int) event.x(), (int) event.y()));

	}

	@Override
	public void onMouseMove(MotionEvent event) {
		inputHandler.addAction(new MouseInputAction(MouseGEv.MOUSE_MOVED,
				(int) event.x(), (int) event.y()));

	}

	@Override
	public void onMouseWheelScroll(WheelEvent event) {

	}

	private MouseGEvButtonType getMouseButton2(int b) {
		switch (b) {
		case Mouse.BUTTON_LEFT:
			return MouseGEvButtonType.BUTTON_1;
		case Mouse.BUTTON_MIDDLE:
			return MouseGEvButtonType.BUTTON_2;
		case Mouse.BUTTON_RIGHT:
			return MouseGEvButtonType.BUTTON_3;
		default:
			return MouseGEvButtonType.NO_BUTTON;
		}
	}

	@Override
	public void onKeyDown(Event event) {
		KeyInputAction action = getKeyboardAction(KeyEventType.KEY_PRESSED,
				event.key());
		if (action != null)
			inputHandler.addAction(action);

	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// FIXME probably is a good idea to use only virtual keys
	}

	@Override
	public void onKeyUp(Event event) {
		KeyInputAction action = getKeyboardAction(KeyEventType.KEY_RELEASED,
				event.key());
		if (action != null)
			inputHandler.addAction(action);

	}

	public KeyInputAction getKeyboardAction(KeyEventType actionType, Key key) {
		switch (key) {
		case UP:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_UP);
		case DOWN:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_DOWN);
		case LEFT:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_LEFT);
		case RIGHT:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_RIGHT);
		case ENTER:
			return new KeyInputAction(actionType, KeyGEvCode.RETURN);
		case ESCAPE:
			return new KeyInputAction(actionType, KeyGEvCode.ESC);
		}
		return null;
	}
}

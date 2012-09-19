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

package ead.engine.core.gdx.platform;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;

public class GdxInputHandler implements InputProcessor {

	private InputHandler inputHandler;

	private Camera c;

	public GdxInputHandler(InputHandler inputHandler, Camera c) {
		this.inputHandler = inputHandler;
		this.c = c;
	}

	@Override
	public boolean keyDown(int keycode) {
		KeyGEvCode code = getKeyCode(keycode);
		if (code != null) {
			inputHandler.addAction(new KeyInputAction(KeyEventType.KEY_PRESSED,
					code));
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		KeyGEvCode code = getKeyCode(keycode);
		if (code != null) {
			inputHandler.addAction(new KeyInputAction(
					KeyEventType.KEY_RELEASED, code));
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		inputHandler.addAction(new KeyInputAction(KeyEventType.KEY_TYPED,
				KeyGEvCode.CHARACTER, character));
		return true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		inputHandler.addAction(getMouseAction(button, MouseGEvType.PRESSED, x,
				y));
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		inputHandler.addAction(getMouseAction(button, MouseGEvType.RELEASED, x,
				y));
		inputHandler
				.addAction(getMouseAction(button, MouseGEvType.CLICK, x, y));
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		mouseMoved(x, y);
		return true;
	}

	private Vector3 vector = new Vector3(0, 0, 0);

	@Override
	public boolean mouseMoved(int x, int y) {
		inputHandler.addAction(getMouseAction(-1, MouseGEvType.MOVED, x, y));
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public MouseInputAction getMouseAction(int button, MouseGEvType action,
			int virtualX, int virtualY) {
		vector.x = virtualX;
		vector.y = virtualY;
		vector.z = 0;
		c.unproject(vector);
		MouseGEvButtonType b = getMouseButton(button);
		MouseGEv event = MouseGEv.getMouseEvent(action, b);
		return new MouseInputAction(event, (int) vector.x, (int) vector.y);
	}

	private MouseGEvButtonType getMouseButton(int button) {
		switch (button) {
		case 0:
			return MouseGEvButtonType.BUTTON_1;
		case 2:
			return MouseGEvButtonType.BUTTON_2;
		case 1:
			return MouseGEvButtonType.BUTTON_3;
		default:
			return MouseGEvButtonType.NO_BUTTON;
		}
	}

	public KeyGEvCode getKeyCode(int code) {
		switch (code) {
		case Input.Keys.NUM_0:
			return KeyGEvCode.NUM_0;
		case Input.Keys.NUM_1:
			return KeyGEvCode.NUM_1;
		case Input.Keys.NUM_2:
			return KeyGEvCode.NUM_2;
		case Input.Keys.NUM_3:
			return KeyGEvCode.NUM_3;
		case Input.Keys.NUM_4:
			return KeyGEvCode.NUM_4;
		case Input.Keys.NUM_5:
			return KeyGEvCode.NUM_5;
		case Input.Keys.NUM_6:
			return KeyGEvCode.NUM_6;
		case Input.Keys.NUM_7:
			return KeyGEvCode.NUM_7;
		case Input.Keys.NUM_8:
			return KeyGEvCode.NUM_8;
		case Input.Keys.NUM_9:
			return KeyGEvCode.NUM_9;
		case Input.Keys.A:
			return KeyGEvCode.A;
		case Input.Keys.ALT_LEFT:
			return KeyGEvCode.ALT_LEFT;
		case Input.Keys.ALT_RIGHT:
			return KeyGEvCode.ALT_RIGHT;
		case Input.Keys.APOSTROPHE:
			return KeyGEvCode.APOSTROPHE;
		case Input.Keys.AT:
			return KeyGEvCode.AT;
		case Input.Keys.B:
			return KeyGEvCode.B;
		case Input.Keys.BACK:
			return KeyGEvCode.BACK;
		case Input.Keys.BACKSLASH:
			return KeyGEvCode.BACKSLASH;
		case Input.Keys.C:
			return KeyGEvCode.C;
		case Input.Keys.CALL:
			return KeyGEvCode.CALL;
		case Input.Keys.CAMERA:
			return KeyGEvCode.CAMERA;
		case Input.Keys.CLEAR:
			return KeyGEvCode.CLEAR;
		case Input.Keys.COMMA:
			return KeyGEvCode.COMMA;
		case Input.Keys.D:
			return KeyGEvCode.D;
		case Input.Keys.BACKSPACE:
			return KeyGEvCode.BACKSPACE;
		case Input.Keys.FORWARD_DEL:
			return KeyGEvCode.FORWARD_DEL;
		case Input.Keys.CENTER:
			return KeyGEvCode.CENTER;
		case Input.Keys.DOWN:
			return KeyGEvCode.DOWN;
		case Input.Keys.LEFT:
			return KeyGEvCode.LEFT;
		case Input.Keys.RIGHT:
			return KeyGEvCode.RIGHT;
		case Input.Keys.UP:
			return KeyGEvCode.UP;
		case Input.Keys.E:
			return KeyGEvCode.E;
		case Input.Keys.ENDCALL:
			return KeyGEvCode.ENDCALL;
		case Input.Keys.ENTER:
			return KeyGEvCode.ENTER;
		case Input.Keys.ENVELOPE:
			return KeyGEvCode.ENVELOPE;
		case Input.Keys.EQUALS:
			return KeyGEvCode.EQUALS;
		case Input.Keys.EXPLORER:
			return KeyGEvCode.EXPLORER;
		case Input.Keys.F:
			return KeyGEvCode.F;
		case Input.Keys.F1:
			return KeyGEvCode.F1;
		case Input.Keys.F2:
			return KeyGEvCode.F2;
		case Input.Keys.F3:
			return KeyGEvCode.F3;
		case Input.Keys.F4:
			return KeyGEvCode.F4;
		case Input.Keys.F5:
			return KeyGEvCode.F5;
		case Input.Keys.F6:
			return KeyGEvCode.F6;
		case Input.Keys.F7:
			return KeyGEvCode.F7;
		case Input.Keys.F8:
			return KeyGEvCode.F8;
		case Input.Keys.F9:
			return KeyGEvCode.F9;
		case Input.Keys.F10:
			return KeyGEvCode.F10;
		case Input.Keys.F11:
			return KeyGEvCode.F11;
		case Input.Keys.F12:
			return KeyGEvCode.F12;
		case Input.Keys.FOCUS:
			return KeyGEvCode.FOCUS;
		case Input.Keys.G:
			return KeyGEvCode.G;
		case Input.Keys.GRAVE:
			return KeyGEvCode.GRAVE;
		case Input.Keys.H:
			return KeyGEvCode.H;
		case Input.Keys.HEADSETHOOK:
			return KeyGEvCode.HEADSETHOOK;
		case Input.Keys.HOME:
			return KeyGEvCode.HOME;
		case Input.Keys.I:
			return KeyGEvCode.I;
		case Input.Keys.J:
			return KeyGEvCode.J;
		case Input.Keys.K:
			return KeyGEvCode.K;
		case Input.Keys.L:
			return KeyGEvCode.L;
		case Input.Keys.LEFT_BRACKET:
			return KeyGEvCode.LEFT_BRACKET;
		case Input.Keys.M:
			return KeyGEvCode.M;
		case Input.Keys.MEDIA_FAST_FORWARD:
			return KeyGEvCode.MEDIA_FAST_FORWARD;
		case Input.Keys.MEDIA_NEXT:
			return KeyGEvCode.MEDIA_NEXT;
		case Input.Keys.MEDIA_PLAY_PAUSE:
			return KeyGEvCode.MEDIA_PLAY_PAUSE;
		case Input.Keys.MEDIA_PREVIOUS:
			return KeyGEvCode.MEDIA_PREVIOUS;
		case Input.Keys.MEDIA_REWIND:
			return KeyGEvCode.MEDIA_REWIND;
		case Input.Keys.MEDIA_STOP:
			return KeyGEvCode.MEDIA_STOP;
		case Input.Keys.MENU:
			return KeyGEvCode.MENU;
		case Input.Keys.MINUS:
			return KeyGEvCode.MINUS;
		case Input.Keys.MUTE:
			return KeyGEvCode.MUTE;
		case Input.Keys.N:
			return KeyGEvCode.N;
		case Input.Keys.NOTIFICATION:
			return KeyGEvCode.NOTIFICATION;
		case Input.Keys.NUM:
			return KeyGEvCode.NUM;
		case Input.Keys.O:
			return KeyGEvCode.O;
		case Input.Keys.P:
			return KeyGEvCode.P;
		case Input.Keys.PERIOD:
			return KeyGEvCode.PERIOD;
		case Input.Keys.PLUS:
			return KeyGEvCode.PLUS;
		case Input.Keys.POUND:
			return KeyGEvCode.POUND;
		case Input.Keys.POWER:
			return KeyGEvCode.POWER;
		case Input.Keys.Q:
			return KeyGEvCode.Q;
		case Input.Keys.R:
			return KeyGEvCode.R;
		case Input.Keys.RIGHT_BRACKET:
			return KeyGEvCode.RIGHT_BRACKET;
		case Input.Keys.S:
			return KeyGEvCode.S;
		case Input.Keys.SEARCH:
			return KeyGEvCode.SEARCH;
		case Input.Keys.SEMICOLON:
			return KeyGEvCode.SEMICOLON;
		case Input.Keys.SHIFT_LEFT:
			return KeyGEvCode.SHIFT_LEFT;
		case Input.Keys.SHIFT_RIGHT:
			return KeyGEvCode.SHIFT_RIGHT;
		case Input.Keys.SLASH:
			return KeyGEvCode.SLASH;
		case Input.Keys.SOFT_LEFT:
			return KeyGEvCode.SOFT_LEFT;
		case Input.Keys.SOFT_RIGHT:
			return KeyGEvCode.SOFT_RIGHT;
		case Input.Keys.SPACE:
			return KeyGEvCode.SPACE;
		case Input.Keys.STAR:
			return KeyGEvCode.STAR;
		case Input.Keys.SYM:
			return KeyGEvCode.SYM;
		case Input.Keys.T:
			return KeyGEvCode.T;
		case Input.Keys.TAB:
			return KeyGEvCode.TAB;
		case Input.Keys.U:
			return KeyGEvCode.U;
		case Input.Keys.UNKNOWN:
			return KeyGEvCode.UNKNOWN;
		case Input.Keys.V:
			return KeyGEvCode.V;
		case Input.Keys.VOLUME_DOWN:
			return KeyGEvCode.VOLUME_DOWN;
		case Input.Keys.VOLUME_UP:
			return KeyGEvCode.VOLUME_UP;
		case Input.Keys.W:
			return KeyGEvCode.W;
		case Input.Keys.X:
			return KeyGEvCode.X;
		case Input.Keys.Y:
			return KeyGEvCode.Y;
		case Input.Keys.Z:
			return KeyGEvCode.Z;
		case Input.Keys.CONTROL_LEFT:
			return KeyGEvCode.CONTROL_LEFT;
		case Input.Keys.CONTROL_RIGHT:
			return KeyGEvCode.CONTROL_RIGHT;
		case Input.Keys.ESCAPE:
			return KeyGEvCode.ESCAPE;
		case Input.Keys.END:
			return KeyGEvCode.END;
		case Input.Keys.INSERT:
			return KeyGEvCode.INSERT;
		case Input.Keys.PAGE_UP:
			return KeyGEvCode.PAGE_UP;
		case Input.Keys.PAGE_DOWN:
			return KeyGEvCode.PAGE_DOWN;
		case Input.Keys.COLON:
			return KeyGEvCode.COLON;

		default:
			return KeyGEvCode.ANY_KEY;
		}
	}
}

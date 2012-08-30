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
		inputHandler.addAction(new KeyInputAction(KeyEventType.KEY_PRESSED,
				character));
		inputHandler.addAction(new KeyInputAction(KeyEventType.KEY_RELEASED,
				character));
		inputHandler.addAction(new KeyInputAction(KeyEventType.KEY_TYPED,
				character));
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
		case Input.Keys.UP:
			return KeyGEvCode.ARROW_UP;
		case Input.Keys.DOWN:
			return KeyGEvCode.ARROW_DOWN;
		case Input.Keys.LEFT:
			return KeyGEvCode.ARROW_LEFT;
		case Input.Keys.RIGHT:
			return KeyGEvCode.ARROW_RIGHT;
		case Input.Keys.ENTER:
			return KeyGEvCode.RETURN;
		case Input.Keys.ESCAPE:
			return KeyGEvCode.ESC;
		case Input.Keys.F5:
			return KeyGEvCode.F5;
		case Input.Keys.F6:
			return KeyGEvCode.F6;
		default:
			// TODO more keys
			return null;
		}
	}
}

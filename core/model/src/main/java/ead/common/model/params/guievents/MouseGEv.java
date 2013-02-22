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

package ead.common.model.params.guievents;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.params.AbstractParam;
import ead.common.model.params.guievents.enums.MouseGEvButtonType;
import ead.common.model.params.guievents.enums.MouseGEvType;

@Element
public class MouseGEv extends AbstractParam implements EAdGUIEvent {

	public static final MouseGEv MOUSE_RIGHT_PRESSED = new MouseGEv(
			MouseGEvType.PRESSED, MouseGEvButtonType.BUTTON_3);
	public static final MouseGEv MOUSE_ENTERED = new MouseGEv(
			MouseGEvType.ENTERED, MouseGEvButtonType.NO_BUTTON);
	public static final MouseGEv MOUSE_EXITED = new MouseGEv(
			MouseGEvType.EXITED, MouseGEvButtonType.NO_BUTTON);
	public static final MouseGEv MOUSE_DRAG = new MouseGEv(MouseGEvType.DRAG,
			MouseGEvButtonType.NO_BUTTON);
	public static final MouseGEv MOUSE_START_DRAG = new MouseGEv(
			MouseGEvType.START_DRAG, MouseGEvButtonType.NO_BUTTON);
	public static final MouseGEv MOUSE_MOVED = new MouseGEv(MouseGEvType.MOVED,
			MouseGEvButtonType.NO_BUTTON);
	public static final MouseGEv MOUSE_LEFT_PRESSED = new MouseGEv(
			MouseGEvType.PRESSED, MouseGEvButtonType.BUTTON_1);
	public static final MouseGEv MOUSE_LEFT_RELEASED = new MouseGEv(
			MouseGEvType.RELEASED, MouseGEvButtonType.BUTTON_1);
	public static final MouseGEv MOUSE_RIGHT_RELEASED = new MouseGEv(
			MouseGEvType.RELEASED, MouseGEvButtonType.BUTTON_3);
	public static final MouseGEv MOUSE_MIDDLE_RELEASED = new MouseGEv(
			MouseGEvType.RELEASED, MouseGEvButtonType.BUTTON_2);
	public static final MouseGEv MOUSE_DROP = new MouseGEv(MouseGEvType.DROP,
			MouseGEvButtonType.NO_BUTTON);
	public static final MouseGEv MOUSE_SWIPE_RIGHT = new MouseGEv(
			MouseGEvType.SWIPE_RIGHT, MouseGEvButtonType.NO_BUTTON);
	public static final MouseGEv MOUSE_SWIPE_LEFT = new MouseGEv(
			MouseGEvType.SWIPE_LEFT, MouseGEvButtonType.NO_BUTTON);
	public static final EAdGUIEvent MOUSE_MIDDLE_PRESSED = new MouseGEv(
			MouseGEvType.PRESSED, MouseGEvButtonType.BUTTON_2);

	@Param
	private MouseGEvType type;

	@Param
	private MouseGEvButtonType button;

	/**
	 * Constructs a mouse event from its string representation
	 * 
	 * @param data
	 */
	public MouseGEv(String data) {
		parse(data);
	}

	public MouseGEv() {

	}

	/**
	 * Returns an {@link MouseGEv} with the given type
	 * 
	 * @param type
	 *            event's type
	 * @param draggingGameObject
	 * @return an {@link MouseGEv} with the given type
	 */
	public static MouseGEv getMouseEvent(MouseGEvType type,
			MouseGEvButtonType button) {
		switch (type) {
		case ENTERED:
			return MOUSE_ENTERED;
		case EXITED:
			return MOUSE_EXITED;
		case DRAG:
			if (button == MouseGEvButtonType.BUTTON_1)
				return MOUSE_DRAG;
			break;
		case MOVED:
			return MOUSE_MOVED;
		case PRESSED:
			if (button == MouseGEvButtonType.BUTTON_1)
				return MOUSE_LEFT_PRESSED;
			break;
		case RELEASED:
			if (button == MouseGEvButtonType.BUTTON_1)
				return MOUSE_LEFT_RELEASED;
			break;
		case DROP:
			return MOUSE_DROP;
		case START_DRAG:
			return MOUSE_START_DRAG;
		case SWIPE_LEFT:
			return MOUSE_SWIPE_LEFT;
		case SWIPE_RIGHT:
			return MOUSE_SWIPE_RIGHT;
		}
		return new MouseGEv(type, button);
	}

	/**
	 * Constructs a mouse event with the given type
	 * 
	 * @param type
	 *            the type
	 */
	public MouseGEv(MouseGEvType type, MouseGEvButtonType button) {
		this.type = type;
		this.button = button;
	}

	public MouseGEvType getType() {
		return type;
	}

	public MouseGEvButtonType getButton() {
		return button;
	}

	public boolean equals(Object o) {
		return (o != null && o.getClass().equals(this.getClass()) && this
				.toString().equals(o.toString()));
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public String toString() {
		return type.toString() + ";" + button.toString();
	}

	public void setType(MouseGEvType type) {
		this.type = type;
	}

	public void setButton(MouseGEvButtonType button) {
		this.button = button;
	}

	@Override
	public String toStringData() {
		return toString();
	}

	@Override
	public boolean parse(String data) {
		String[] values = data.split(";");
		if (values.length == 2) {
			try {
				this.type = MouseGEvType.valueOf(values[0]);
				this.button = MouseGEvButtonType.valueOf(values[1]);
				return true;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}
		return false;
	}

}

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

package es.eucm.eadventure.common.model.guievents.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.guievents.enums.MouseActionType;
import es.eucm.eadventure.common.model.guievents.enums.MouseButton;
import es.eucm.eadventure.common.model.impl.EAdElementImpl;

@Element(runtime = EAdMouseEventImpl.class, detailed = EAdMouseEventImpl.class)
public class EAdMouseEventImpl extends EAdElementImpl implements EAdMouseEvent {

	public static final EAdMouseEvent MOUSE_RIGHT_CLICK = new EAdMouseEventImpl(
			MouseActionType.CLICK, MouseButton.BUTTON_3);
	public static final EAdMouseEvent MOUSE_LEFT_CLICK = new EAdMouseEventImpl(
			MouseActionType.CLICK, MouseButton.BUTTON_1);
	public static final EAdMouseEvent MOUSE_RIGHT_DOUBLE_CLICK = new EAdMouseEventImpl(
			MouseActionType.DOUBLE_CLICK, MouseButton.BUTTON_3);
	public static final EAdMouseEvent MOUSE_ENTERED = new EAdMouseEventImpl(
			MouseActionType.ENTERED, MouseButton.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_EXITED = new EAdMouseEventImpl(
			MouseActionType.EXITED, MouseButton.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_DRAG = new EAdMouseEventImpl(
			MouseActionType.DRAG, MouseButton.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_START_DRAG = new EAdMouseEventImpl(
			MouseActionType.START_DRAG, MouseButton.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_MOVED = new EAdMouseEventImpl(
			MouseActionType.MOVED, MouseButton.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_LEFT_PRESSED = new EAdMouseEventImpl(
			MouseActionType.PRESSED, MouseButton.BUTTON_1);
	public static final EAdMouseEvent MOUSE_LEFT_RELEASED = new EAdMouseEventImpl(
			MouseActionType.RELEASED, MouseButton.BUTTON_1);
	public static final EAdMouseEvent MOUSE_DROP = new EAdMouseEventImpl(
			MouseActionType.DROP, MouseButton.NO_BUTTON);

	@Param("type")
	private MouseActionType type;

	@Param("button")
	private MouseButton button;

	public EAdMouseEventImpl() {

	}

	/**
	 * Returns an {@link EAdMouseEvent} with the given type
	 * 
	 * @param type
	 *            event's type
	 * @param draggingGameObject
	 * @return an {@link EAdMouseEvent} with the given type
	 */
	public static EAdMouseEvent getMouseEvent(MouseActionType type,
			MouseButton button) {
		switch (type) {
		case DOUBLE_CLICK:
			if (button == MouseButton.BUTTON_3)
				return MOUSE_RIGHT_DOUBLE_CLICK;
			break;
		case CLICK:
			if (button == MouseButton.BUTTON_3)
				return MOUSE_RIGHT_CLICK;
			else if (button == MouseButton.BUTTON_1)
				return MOUSE_LEFT_CLICK;
			break;
		case ENTERED:
			return MOUSE_ENTERED;
		case EXITED:
			return MOUSE_EXITED;
		case DRAG:
			if (button == MouseButton.BUTTON_1)
				return MOUSE_DRAG;
			break;
		case MOVED:
			return MOUSE_MOVED;
		case PRESSED:
			if (button == MouseButton.BUTTON_1)
				return MOUSE_LEFT_PRESSED;
			break;
		case RELEASED:
			if (button == MouseButton.BUTTON_1)
				return MOUSE_LEFT_RELEASED;
			break;
		}
		return new EAdMouseEventImpl(type, button);
	}

	/**
	 * Constructs a mouse event with the given type
	 * 
	 * @param type
	 *            the type
	 */
	public EAdMouseEventImpl(MouseActionType type, MouseButton button) {
		this.type = type;
		this.button = button;
	}

	@Override
	public MouseActionType getType() {
		return type;
	}

	@Override
	public MouseButton getButton() {
		return button;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EAdMouseEventImpl) {
			EAdMouseEventImpl e = (EAdMouseEventImpl) o;
			return (e.type.equals(type)) && (e.button.equals(button));
		}
		return false;
	}

	public String toString() {
		return type.toString() + ";" + button.toString();
	}

	@Override
	public EAdElement copy() {
		return this;
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		return this;
	}

	public int hashCode() {
		return type.hashCode() + button.hashCode();
	}

	public void setType(MouseActionType type) {
		this.type = type;
	}

	public void setButton(MouseButton button) {
		this.button = button;
	}
	
	
	

}

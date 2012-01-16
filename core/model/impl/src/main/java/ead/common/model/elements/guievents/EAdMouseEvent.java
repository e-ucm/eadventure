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

package ead.common.model.elements.guievents;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdElementImpl;
import ead.common.model.elements.guievents.EAdGUIEvent;
import ead.common.model.elements.guievents.enums.MouseButtonType;
import ead.common.model.elements.guievents.enums.MouseEventType;

@Element(runtime = EAdMouseEvent.class, detailed = EAdMouseEvent.class)
public class EAdMouseEvent extends EAdElementImpl implements EAdGUIEvent {

	public static final EAdMouseEvent MOUSE_RIGHT_CLICK = new EAdMouseEvent(
			MouseEventType.CLICK, MouseButtonType.BUTTON_3);
	public static final EAdMouseEvent MOUSE_LEFT_CLICK = new EAdMouseEvent(
			MouseEventType.CLICK, MouseButtonType.BUTTON_1);
	public static final EAdMouseEvent MOUSE_RIGHT_DOUBLE_CLICK = new EAdMouseEvent(
			MouseEventType.DOUBLE_CLICK, MouseButtonType.BUTTON_3);
	public static final EAdMouseEvent MOUSE_ENTERED = new EAdMouseEvent(
			MouseEventType.ENTERED, MouseButtonType.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_EXITED = new EAdMouseEvent(
			MouseEventType.EXITED, MouseButtonType.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_DRAG = new EAdMouseEvent(
			MouseEventType.DRAG, MouseButtonType.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_START_DRAG = new EAdMouseEvent(
			MouseEventType.START_DRAG, MouseButtonType.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_MOVED = new EAdMouseEvent(
			MouseEventType.MOVED, MouseButtonType.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_LEFT_PRESSED = new EAdMouseEvent(
			MouseEventType.PRESSED, MouseButtonType.BUTTON_1);
	public static final EAdMouseEvent MOUSE_LEFT_RELEASED = new EAdMouseEvent(
			MouseEventType.RELEASED, MouseButtonType.BUTTON_1);
	public static final EAdMouseEvent MOUSE_DROP = new EAdMouseEvent(
			MouseEventType.DROP, MouseButtonType.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_SWIPE_RIGHT = new EAdMouseEvent(
			MouseEventType.SWIPE_RIGHT, MouseButtonType.NO_BUTTON);
	public static final EAdMouseEvent MOUSE_SWIPE_LEFT = new EAdMouseEvent(
			MouseEventType.SWIPE_LEFT, MouseButtonType.NO_BUTTON);

	@Param("type")
	private MouseEventType type;

	@Param("button")
	private MouseButtonType button;

	public EAdMouseEvent() {

	}

	/**
	 * Returns an {@link EAdMouseEvent} with the given type
	 * 
	 * @param type
	 *            event's type
	 * @param draggingGameObject
	 * @return an {@link EAdMouseEvent} with the given type
	 */
	public static EAdMouseEvent getMouseEvent(MouseEventType type,
			MouseButtonType button) {
		switch (type) {
		case DOUBLE_CLICK:
			if (button == MouseButtonType.BUTTON_3)
				return MOUSE_RIGHT_DOUBLE_CLICK;
			break;
		case CLICK:
			if (button == MouseButtonType.BUTTON_3)
				return MOUSE_RIGHT_CLICK;
			else if (button == MouseButtonType.BUTTON_1)
				return MOUSE_LEFT_CLICK;
			break;
		case ENTERED:
			return MOUSE_ENTERED;
		case EXITED:
			return MOUSE_EXITED;
		case DRAG:
			if (button == MouseButtonType.BUTTON_1)
				return MOUSE_DRAG;
			break;
		case MOVED:
			return MOUSE_MOVED;
		case PRESSED:
			if (button == MouseButtonType.BUTTON_1)
				return MOUSE_LEFT_PRESSED;
			break;
		case RELEASED:
			if (button == MouseButtonType.BUTTON_1)
				return MOUSE_LEFT_RELEASED;
			break;
		}
		return new EAdMouseEvent(type, button);
	}

	/**
	 * Constructs a mouse event with the given type
	 * 
	 * @param type
	 *            the type
	 */
	public EAdMouseEvent(MouseEventType type, MouseButtonType button) {
		this.type = type;
		this.button = button;
	}


	public MouseEventType getType() {
		return type;
	}


	public MouseButtonType getButton() {
		return button;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EAdMouseEvent) {
			EAdMouseEvent e = (EAdMouseEvent) o;
			return (e.type.equals(type)) && (e.button.equals(button));
		}
		return false;
	}

	public String toString() {
		if ( button == null || type == null){
			return "mousevent";
		}
		return type.toString() + ";" + button.toString();
	}

	public int hashCode() {
		return type.hashCode() + button.hashCode();
	}

	public void setType(MouseEventType type) {
		this.type = type;
	}

	public void setButton(MouseButtonType button) {
		this.button = button;
	}
	
	
	

}

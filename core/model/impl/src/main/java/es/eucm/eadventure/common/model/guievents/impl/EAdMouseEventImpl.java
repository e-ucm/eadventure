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
import es.eucm.eadventure.common.model.impl.EAdElementImpl;

@Element(runtime = EAdMouseEventImpl.class, detailed = EAdMouseEventImpl.class)
public class EAdMouseEventImpl extends EAdElementImpl implements EAdMouseEvent {

	@Param("type")
	private MouseActionType type;

	public static final EAdMouseEvent MOUSE_RIGHT_CLICK = new EAdMouseEventImpl(
			EAdMouseEvent.MouseActionType.RIGHT_CLICK);
	public static final EAdMouseEvent MOUSE_LEFT_CLICK = new EAdMouseEventImpl(
			EAdMouseEvent.MouseActionType.LEFT_CLICK);
	public static final EAdMouseEvent MOUSE_DOUBLE_CLICK = new EAdMouseEventImpl(
			EAdMouseEvent.MouseActionType.DOUBLE_CLICK);
	public static final EAdMouseEvent MOUSE_ENTERED = new EAdMouseEventImpl(
			EAdMouseEvent.MouseActionType.ENTERED);
	public static final EAdMouseEvent MOUSE_EXITED = new EAdMouseEventImpl(
			EAdMouseEvent.MouseActionType.EXITED);
	public static final EAdMouseEvent MOUSE_DRAG = new EAdMouseEventImpl(
			EAdMouseEvent.MouseActionType.DRAG);
	public static final EAdMouseEvent MOUSE_MOVED = new EAdMouseEventImpl(
			EAdMouseEvent.MouseActionType.MOVED);
	public static final EAdMouseEvent MOUSE_PRESSED = new EAdMouseEventImpl(
			EAdMouseEvent.MouseActionType.PRESSED);

	/**
	 * Returns an {@link EAdMouseEvent} with the given type
	 * 
	 * @param type
	 *            event's type
	 * @param draggingGameObject 
	 * @return an {@link EAdMouseEvent} with the given type
	 */
	public static EAdMouseEvent getMouseEvent(MouseActionType type) {
		switch (type) {
		case DOUBLE_CLICK:
			return MOUSE_DOUBLE_CLICK;
		case RIGHT_CLICK:
			return MOUSE_RIGHT_CLICK;
		case LEFT_CLICK:
			return MOUSE_LEFT_CLICK;
		case ENTERED:
			return MOUSE_ENTERED;
		case EXITED:
			return MOUSE_EXITED;
		case DRAG:
			return MOUSE_DRAG;
		case MOVED:
			return MOUSE_MOVED;
		case PRESSED:
			return MOUSE_PRESSED;
		}
		return null;
	}

	/**
	 * Constructs a mouse event with the given type
	 * 
	 * @param type
	 *            the type
	 */
	private EAdMouseEventImpl(MouseActionType type) {
		this.type = type;
	}

	@Override
	public MouseActionType getType() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EAdMouseEvent) {
			EAdMouseEvent e = (EAdMouseEvent) o;
			return e.getType() == this.type;
		}
		return false;
	}

	public String toString() {
		return type.toString();
	}

	@Override
	public EAdElement copy() {
		return this;
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		return this;
	}

}

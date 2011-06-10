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
import es.eucm.eadventure.common.model.params.guievents.EAdKeyEvent;

@Element(runtime = EAdKeyEventImpl.class, detailed = EAdKeyEventImpl.class)
public class EAdKeyEventImpl implements EAdKeyEvent {

	public static final EAdKeyEvent KEY_ARROW_DOWN = new EAdKeyEventImpl(
			EAdKeyEvent.KeyActionType.KEY_PRESSED, EAdKeyEvent.KeyCode.ARROW_DOWN);
	public static final EAdKeyEvent KEY_ARROW_LEFT = new EAdKeyEventImpl(
			EAdKeyEvent.KeyActionType.KEY_PRESSED, EAdKeyEvent.KeyCode.ARROW_LEFT);
	public static final EAdKeyEvent KEY_ARROW_RIGHT = new EAdKeyEventImpl(
			EAdKeyEvent.KeyActionType.KEY_PRESSED, EAdKeyEvent.KeyCode.ARROW_RIGHT);
	public static final EAdKeyEvent KEY_ARROW_UP = new EAdKeyEventImpl(
			EAdKeyEvent.KeyActionType.KEY_PRESSED, EAdKeyEvent.KeyCode.ARROW_UP);
	public static final EAdKeyEvent KEY_ESC = new EAdKeyEventImpl(
			EAdKeyEvent.KeyActionType.KEY_PRESSED, EAdKeyEvent.KeyCode.ESC);

	@Param("type")
	private KeyActionType type;

	@Param("keyCode")
	private KeyCode keyCode;

	@Param("char")
	private char c;

	public EAdKeyEventImpl(KeyActionType type, KeyCode keyCode) {
		this.type = type;
		this.keyCode = keyCode;
	}

	public EAdKeyEventImpl(KeyActionType type, char c) {
		this.type = type;
		this.keyCode = KeyCode.LETTER;
		this.c = c;

	}

	@Override
	public KeyActionType getType() {
		return type;
	}

	@Override
	public KeyCode getKeyCode() {
		return keyCode;
	}

	@Override
	public char getChar() {
		return c;
	}

	public boolean equals(Object o) {
		if (o instanceof EAdKeyEvent) {
			EAdKeyEvent e = (EAdKeyEvent) o;
			if (this.type == e.getType() && this.keyCode == e.getKeyCode()) {
				if (keyCode == KeyCode.LETTER) {
					return this.c == e.getChar();
				}
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Sets char for this event
	 * 
	 * @param letter
	 *            the letter
	 */
	public void setChar(char letter) {
		this.c = letter;
	}
	
	public String toString( ){
		return type.toString() + "_" + keyCode.toString() + "_" + c;
	}

}

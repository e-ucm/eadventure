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

package es.eucm.eadventure.common.model.elements.guievents;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdElementImpl;
import es.eucm.eadventure.common.model.elements.guievents.EAdKeyEvent;
import es.eucm.eadventure.common.model.elements.guievents.enums.KeyActionType;
import es.eucm.eadventure.common.model.elements.guievents.enums.KeyCode;

@Element(runtime = KeyEventImpl.class, detailed = KeyEventImpl.class)
public class KeyEventImpl extends EAdElementImpl implements EAdKeyEvent {

	public static final EAdKeyEvent KEY_ARROW_DOWN = new KeyEventImpl(
			KeyActionType.KEY_PRESSED,
			KeyCode.ARROW_DOWN);
	public static final EAdKeyEvent KEY_ARROW_LEFT = new KeyEventImpl(
			KeyActionType.KEY_PRESSED,
			KeyCode.ARROW_LEFT);
	public static final EAdKeyEvent KEY_ARROW_RIGHT = new KeyEventImpl(
			KeyActionType.KEY_PRESSED,
			KeyCode.ARROW_RIGHT);
	public static final EAdKeyEvent KEY_ARROW_UP = new KeyEventImpl(
			KeyActionType.KEY_PRESSED, KeyCode.ARROW_UP);
	public static final EAdKeyEvent KEY_ESC = new KeyEventImpl(
			KeyActionType.KEY_PRESSED, KeyCode.ESC);

	@Param("type")
	private KeyActionType type;

	@Param("keyCode")
	private KeyCode keyCode;

	@Param("char")
	private Character character;
	
	public KeyEventImpl( ){
		
	}

	public KeyEventImpl(KeyActionType type, KeyCode keyCode) {
		super();
		setId("KeyEvent_" + type + "_" + keyCode);
		this.type = type;
		this.keyCode = keyCode;
	}

	public KeyEventImpl(KeyActionType type, char c) {
		super();
		setId("KeyEvent_" + type + "_" + c);
		this.type = type;
		this.keyCode = KeyCode.LETTER;
		this.character = c;

	}

	@Override
	public KeyActionType getType() {
		return type;
	}
	
	public void setType(KeyActionType type) {
		this.type = type;
	}

	@Override
	public KeyCode getKeyCode() {
		return keyCode;
	}
	
	public void setKeyCode(KeyCode keyCode) {
		this.keyCode = keyCode;
	}

	@Override
	public Character getCharacter() {
		return character;
	}

	/**
	 * Sets char for this event
	 * 
	 * @param letter
	 *            the letter
	 */
	public void setCharacter(Character letter) {
		this.character = letter;
	}

	public String toString() {
		return type.toString() + "_" + keyCode.toString() + "_" + character;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EAdKeyEvent) {
			EAdKeyEvent e = (EAdKeyEvent) o;
			if (this.type == e.getType() && this.keyCode == e.getKeyCode()) {
				if (keyCode == KeyCode.LETTER) {
					return this.character == e.getCharacter();
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
	public int hashCode(){
		return (type.toString() + keyCode.toString() + character + "").hashCode();
	}

}

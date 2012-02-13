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
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.guievents.EAdGUIEvent;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.guievents.enums.KeyEventType;

@Element(runtime = KeyGEv.class, detailed = KeyGEv.class)
public class KeyGEv extends BasicElement implements EAdGUIEvent {

	public static final KeyGEv KEY_ARROW_DOWN = new KeyGEv(
			KeyEventType.KEY_PRESSED,
			KeyGEvCode.ARROW_DOWN);
	public static final KeyGEv KEY_ARROW_LEFT = new KeyGEv(
			KeyEventType.KEY_PRESSED,
			KeyGEvCode.ARROW_LEFT);
	public static final KeyGEv KEY_ARROW_RIGHT = new KeyGEv(
			KeyEventType.KEY_PRESSED,
			KeyGEvCode.ARROW_RIGHT);
	public static final KeyGEv KEY_ARROW_UP = new KeyGEv(
			KeyEventType.KEY_PRESSED, KeyGEvCode.ARROW_UP);
	public static final KeyGEv KEY_ESC = new KeyGEv(
			KeyEventType.KEY_PRESSED, KeyGEvCode.ESC);

	@Param("type")
	private KeyEventType type;

	@Param("keyCode")
	private KeyGEvCode keyCode;

	@Param("char")
	private Character character;
	
	public KeyGEv( ){
		
	}

	public KeyGEv(KeyEventType type, KeyGEvCode keyCode) {
		super();
		setId("KeyEvent_" + type + "_" + keyCode);
		this.type = type;
		this.keyCode = keyCode;
	}

	public KeyGEv(KeyEventType type, char c) {
		super();
		setId("KeyEvent_" + type + "_" + c);
		this.type = type;
		this.keyCode = KeyGEvCode.LETTER;
		this.character = c;

	}


	public KeyEventType getType() {
		return type;
	}
	
	public void setType(KeyEventType type) {
		this.type = type;
	}

	public KeyGEvCode getKeyCode() {
		return keyCode;
	}
	
	public void setKeyCode(KeyGEvCode keyCode) {
		this.keyCode = keyCode;
	}


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
		if (o instanceof KeyGEv) {
			KeyGEv e = (KeyGEv) o;
			if (this.type == e.getType() && this.keyCode == e.getKeyCode()) {
				if (keyCode == KeyGEvCode.LETTER) {
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

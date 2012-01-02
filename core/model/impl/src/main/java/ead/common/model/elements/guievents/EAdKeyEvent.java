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
import ead.common.model.elements.guievents.enums.KeyEventCode;
import ead.common.model.elements.guievents.enums.KeyEventType;

@Element(runtime = EAdKeyEvent.class, detailed = EAdKeyEvent.class)
public class EAdKeyEvent extends EAdElementImpl implements EAdGUIEvent {

	public static final EAdKeyEvent KEY_ARROW_DOWN = new EAdKeyEvent(
			KeyEventType.KEY_PRESSED,
			KeyEventCode.ARROW_DOWN);
	public static final EAdKeyEvent KEY_ARROW_LEFT = new EAdKeyEvent(
			KeyEventType.KEY_PRESSED,
			KeyEventCode.ARROW_LEFT);
	public static final EAdKeyEvent KEY_ARROW_RIGHT = new EAdKeyEvent(
			KeyEventType.KEY_PRESSED,
			KeyEventCode.ARROW_RIGHT);
	public static final EAdKeyEvent KEY_ARROW_UP = new EAdKeyEvent(
			KeyEventType.KEY_PRESSED, KeyEventCode.ARROW_UP);
	public static final EAdKeyEvent KEY_ESC = new EAdKeyEvent(
			KeyEventType.KEY_PRESSED, KeyEventCode.ESC);

	@Param("type")
	private KeyEventType type;

	@Param("keyCode")
	private KeyEventCode keyCode;

	@Param("char")
	private Character character;
	
	public EAdKeyEvent( ){
		
	}

	public EAdKeyEvent(KeyEventType type, KeyEventCode keyCode) {
		super();
		setId("KeyEvent_" + type + "_" + keyCode);
		this.type = type;
		this.keyCode = keyCode;
	}

	public EAdKeyEvent(KeyEventType type, char c) {
		super();
		setId("KeyEvent_" + type + "_" + c);
		this.type = type;
		this.keyCode = KeyEventCode.LETTER;
		this.character = c;

	}


	public KeyEventType getType() {
		return type;
	}
	
	public void setType(KeyEventType type) {
		this.type = type;
	}

	public KeyEventCode getKeyCode() {
		return keyCode;
	}
	
	public void setKeyCode(KeyEventCode keyCode) {
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
		if (o instanceof EAdKeyEvent) {
			EAdKeyEvent e = (EAdKeyEvent) o;
			if (this.type == e.getType() && this.keyCode == e.getKeyCode()) {
				if (keyCode == KeyEventCode.LETTER) {
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

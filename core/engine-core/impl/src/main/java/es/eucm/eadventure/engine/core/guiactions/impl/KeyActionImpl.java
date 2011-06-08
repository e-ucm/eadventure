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

package es.eucm.eadventure.engine.core.guiactions.impl;

import es.eucm.eadventure.common.model.guievents.impl.EAdKeyEventImpl;
import es.eucm.eadventure.common.model.params.guievents.EAdGUIEvent;
import es.eucm.eadventure.common.model.params.guievents.EAdKeyEvent.KeyActionType;
import es.eucm.eadventure.common.model.params.guievents.EAdKeyEvent.KeyCode;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;

public class KeyActionImpl implements KeyAction {

	private boolean consumed;

	private EAdKeyEventImpl keyEvent;

	public KeyActionImpl(KeyActionType type, KeyCode code) {
		consumed = false;
		keyEvent = new EAdKeyEventImpl(type, code);
	}

	public KeyActionImpl(KeyActionType type, char letter) {
		this(type, KeyCode.LETTER);
		keyEvent.setChar(letter);
	}

	@Override
	public boolean isConsumed() {
		return consumed;
	}

	@Override
	public void consume() {
		consumed = true;
	}

	@Override
	public KeyActionType getType() {
		return keyEvent.getType();
	}

	@Override
	public KeyCode getKeyCode() {
		return keyEvent.getKeyCode();
	}

	@Override
	public char getLetter() {
		return keyEvent.getChar();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof KeyAction) {
			KeyAction k = (KeyAction) o;
			if (k.getType().equals(this.getType())) {
				if (getType().equals(KeyCode.LETTER)) {
					return this.getLetter() == k.getLetter();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public EAdGUIEvent getGUIEvent() {
		return this.keyEvent;
	}

}

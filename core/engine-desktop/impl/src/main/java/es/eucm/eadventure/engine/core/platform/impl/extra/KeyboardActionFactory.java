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

package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.awt.event.KeyEvent;

import es.eucm.eadventure.common.model.params.guievents.EAdKeyEvent.KeyActionType;
import es.eucm.eadventure.common.model.params.guievents.EAdKeyEvent.KeyCode;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.guiactions.impl.KeyActionImpl;

public class KeyboardActionFactory {

	public static KeyAction getKeyboardAction(KeyActionType actionType, KeyEvent keyEvent) {
		switch (keyEvent.getKeyCode()) {
		case KeyEvent.VK_UP:
			return new KeyActionImpl(actionType, KeyCode.ARROW_UP);
		case KeyEvent.VK_DOWN:
			return new KeyActionImpl(actionType, KeyCode.ARROW_DOWN);
		case KeyEvent.VK_LEFT:
			return new KeyActionImpl(actionType, KeyCode.ARROW_LEFT);
		case KeyEvent.VK_RIGHT:
			return new KeyActionImpl(actionType, KeyCode.ARROW_RIGHT);
		case KeyEvent.VK_ENTER:
			return new KeyActionImpl(actionType, KeyCode.RETURN);
		case KeyEvent.VK_ESCAPE:
			return new KeyActionImpl(actionType, KeyCode.ESC);
		}
		if (keyEvent.getKeyChar() != 0)
			return new KeyActionImpl(actionType, keyEvent.getKeyChar());
		return null;
	}
}

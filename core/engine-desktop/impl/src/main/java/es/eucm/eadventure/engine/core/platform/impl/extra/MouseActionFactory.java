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
import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;

import java.awt.event.MouseEvent;

import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.guiactions.impl.MouseActionImpl;

public class MouseActionFactory {

	public static MouseAction getMouseAction(MouseEvent e, int virtualX, int virtualY, boolean click) {
		if (e.getButton() == MouseEvent.NOBUTTON) {
			return null; //new MouseActionImpl(MouseAction.MouseActionType.MOVED, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1 && !click) {
			return new MouseActionImpl(MouseActionType.PRESSED, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1 && click) {
			return new MouseActionImpl(MouseActionType.LEFT_CLICK, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && click) {
			return new MouseActionImpl(MouseActionType.DOUBLE_CLICK, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1 && click) {
			return new MouseActionImpl(MouseActionType.RIGHT_CLICK, virtualX, virtualY);
		} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 0 && click) {
			return null;
		}
	 	return null;
		
	}
}

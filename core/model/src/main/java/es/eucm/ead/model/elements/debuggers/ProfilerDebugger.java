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

package es.eucm.ead.model.elements.debuggers;

import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.GroupElement;
import es.eucm.ead.model.elements.widgets.Label;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.params.fills.ColorFill;

/**
 * 
 * Debugger to show some statistics of the game engine
 * 
 */
@Element
public class ProfilerDebugger extends GroupElement {

	private ElementField[] fields = new ElementField[] {
			SystemFields.DEBUG_GAME_OBJECTS, SystemFields.DEBUG_ASSETS };

	public ProfilerDebugger() {
		int y = 0;
		int marginY = 40;
		for (ElementField f : fields) {
			Label l = new Label(f.getVarName() + ": [0]   ");
			l.getCaption().setPadding(20);
			l.getCaption().addOperation(f);
			l.setPosition(10, y);
			l.setBgColor(ColorFill.WHITE);
			y += marginY;
			addSceneElement(l);
		}
	}
}

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

package ead.editor.control.elements;

import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.params.text.EAdString;
import ead.editor.view.generics.Panel;
import ead.editor.view.generics.impl.EAdStringOption;
import ead.editor.view.generics.impl.FieldDescriptorImpl;
import ead.editor.view.generics.impl.PanelImpl;

public class EAdSceneElementDefController extends AbstractElementController<EAdSceneElementDef> {

	@Override
	public Panel getPanel(LevelOfDetail view) {
		Panel panel = new PanelImpl(null, Panel.LayoutPolicy.VERTICAL);

		switch (view) {
		case ADVANCED:

			break;
		case SIMPLE:

			break;
		case EXPERT:
		default:
			panel.addElement(new EAdStringOption(
							Messages.name,
							"this is the name",
							new FieldDescriptorImpl<EAdString>(element, "name"),
							EAdStringOption.ExpectedLength.SHORT));

			panel.addElement(
					new EAdStringOption("description",
							"this is the description",
							new FieldDescriptorImpl<EAdString>(element,
									"desc")));

			panel.addElement(
					new EAdStringOption("detailed description",
							"this is the detailed description",
							new FieldDescriptorImpl<EAdString>(element,
									"detailDesc"),
							EAdStringOption.ExpectedLength.LONG));

			panel.addElement(
					new EAdStringOption(Messages.documentation,
							"this is the documentation",
							new FieldDescriptorImpl<EAdString>(element,
									"doc"),
							EAdStringOption.ExpectedLength.LONG));
		
			break;
		}

		return panel;
	}

}

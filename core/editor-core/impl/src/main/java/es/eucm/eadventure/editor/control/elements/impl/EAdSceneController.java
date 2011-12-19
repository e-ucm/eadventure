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

package es.eucm.eadventure.editor.control.elements.impl;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.params.text.EAdString;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.editor.view.generics.impl.ElementOption;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;
import es.eucm.eadventure.editor.view.generics.impl.PanelImpl;
import es.eucm.eadventure.editor.view.generics.scene.impl.PreviewPanelImpl;

public class EAdSceneController extends AbstractElementController<EAdScene> {

	@Override
	public Panel getPanel(View view) {
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
							new FieldDescriptorImpl<EAdString>(element.getDefinition(), "name"),
							EAdStringOption.ExpectedLength.SHORT));
			
			panel.addElement(
					new EAdStringOption(Messages.documentation,
							"this is the documentation",
							new FieldDescriptorImpl<EAdString>(element.getDefinition(),
									"doc"),
							EAdStringOption.ExpectedLength.LONG));
			
			panel.addElement(
					new ElementOption<EAdSceneElement>(Messages.background,
							"this is the scene background",
							new FieldDescriptorImpl<EAdSceneElement>(element,
									"background")));
		
			panel.addElement(
					new PreviewPanelImpl(element));
			break;
		}

		return panel;
	}

}

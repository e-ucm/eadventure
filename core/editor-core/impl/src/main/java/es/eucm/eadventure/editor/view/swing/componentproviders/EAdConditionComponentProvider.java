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

package es.eucm.eadventure.editor.view.swing.componentproviders;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.EAdConditionOption;
import es.eucm.eadventure.gui.EAdBorderedPanel;
import es.eucm.eadventure.gui.EAdButton;
import es.eucm.eadventure.gui.EAdTextField;

public class EAdConditionComponentProvider implements ComponentProvider<EAdConditionOption, JComponent> {

	private FieldValueReader fieldValueReader;
	
	public EAdConditionComponentProvider(FieldValueReader fieldValueReader) {
		this.fieldValueReader = fieldValueReader;
	}

	@Override
	public JComponent getComponent(EAdConditionOption element) {
		if (element.getView() == EAdConditionOption.View.DETAILED) {
			JPanel panel = new EAdBorderedPanel(element.getTitle());
			panel.setLayout(new BorderLayout());
	
			EAdTextField textField = new EAdTextField();
			panel.add(textField, BorderLayout.CENTER);
			EAdCondition condition = fieldValueReader.readValue(element.getFieldDescriptor());
			textField.setText(condition.toString());
			textField.setEnabled(false);
			//TODO should update field after condition edition
			
			EAdButton button = new EAdButton("Edit");
			button.setToolTipText(element.getToolTipText());
			panel.add(button, BorderLayout.EAST);
			//TODO should allow for the edition of conditions
			
			return panel;
		} else {
			EAdButton button = new EAdButton(element.getTitle());
			//TODO should allow for the edition of conditions
			//TODO should show the icons
			

			return button;
		}
	}

}

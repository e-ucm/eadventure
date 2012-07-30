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

package ead.editor.view.generic;

import ead.editor.control.CommandManager;
import ead.editor.control.commands.ChangeFieldValueCommand;
import ead.editor.view.generic.FieldDescriptor;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BooleanOption extends AbstractOption<Boolean> {

	public BooleanOption(String title, String toolTipText, FieldDescriptor<Boolean> fieldDescriptor) {
		super(title, toolTipText, fieldDescriptor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.eucm.eadventure.editor.view.ComponentProvider#getComponent(es.eucm
	 * .eadventure.editor.view.generics.InterfaceElement)
	 */
    @Override
	public JCheckBox getComponent(final CommandManager manager) {
		final JCheckBox checkBox = new JCheckBox(getTitle());
		checkBox.setToolTipText(getToolTipText());
		checkBox.setSelected(read(getFieldDescriptor()));
		checkBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				ChangeFieldValueCommand<Boolean> changeFieldValueCommand;
				changeFieldValueCommand = new ChangeFieldValueCommand<Boolean>(
                        checkBox.isSelected(), getFieldDescriptor());
				manager.performCommand(changeFieldValueCommand);
			}

		});
		return checkBox;
	}
}

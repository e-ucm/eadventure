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
import javax.swing.JCheckBox;

import ead.editor.control.change.ChangeEvent;
import ead.editor.control.change.ChangeListener;

public class BooleanOption extends AbstractOption<Boolean> implements
		ChangeListener<ChangeEvent> {

	private JCheckBox checkBox;

	public BooleanOption(String title, String toolTipText,
			FieldDescriptor<Boolean> fieldDescriptor) {
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
		checkBox = new JCheckBox(getTitle());
		checkBox.setToolTipText(getToolTipText());
		checkBox.setSelected(read(getFieldDescriptor()));
		checkBox.addChangeListener(new javax.swing.event.ChangeListener() {

			@Override
			public void stateChanged(javax.swing.event.ChangeEvent change) {
				ChangeFieldValueCommand<Boolean> changeFieldValueCommand;
				changeFieldValueCommand = new ChangeFieldValueCommand<Boolean>(
						!checkBox.isSelected(), checkBox.isSelected(),
						getFieldDescriptor());
				isUpdating = true;
				manager.performCommand(changeFieldValueCommand);
				isUpdating = false;
			}

		});
		return checkBox;
	}

	@Override
	public void cleanup(CommandManager manager) {
		manager.removeChangeListener(this);
	}

	@Override
	public void processChange(ChangeEvent event) {
		if (!isUpdating && event.hasChanged(fieldDescriptor)) {
			checkBox.setSelected(read(getFieldDescriptor()));
		}
	}
}

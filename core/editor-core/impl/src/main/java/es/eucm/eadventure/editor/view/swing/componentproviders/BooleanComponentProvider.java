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

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.control.commands.impl.ChangeFieldValueCommand;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.BooleanOption;

/**
 * Swing {@link ComponentProvider} for field with {@link Boolean} elements.
 * <p>
 * This provider directly shows and manipulates the value of the boolean field
 * using a JCheckBox.
 * 
 */
public class BooleanComponentProvider implements
		ComponentProvider<BooleanOption, JCheckBox> {

	/**
	 * The {@link BooleanOption} option element
	 */
	private BooleanOption element;

	/**
	 * The JCheckBox where the value is shown and through which it is changed
	 */
	private JCheckBox checkBox;

	/**
	 * The system dependent {@link FieldValueReader}
	 */
	private FieldValueReader fieldValueReader;
	
	/**
	 * The command manager {@link CommandManager}
	 */
	private CommandManager commandManager;

	/**
	 * Constructor.
	 * 
	 * @param fieldValueReader
	 *            the {@link FieldValueReader}
	 * @param commandManager
	 * 			  the {@link CommandManager}
	 */
	public BooleanComponentProvider(FieldValueReader fieldValueReader,
			CommandManager commandManager) {
		this.fieldValueReader = fieldValueReader;
		this.commandManager = commandManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.editor.view.ComponentProvider#getComponent(es.eucm
	 * .eadventure.editor.view.generics.InterfaceElement)
	 */
	@Override
	public JCheckBox getComponent(BooleanOption element2) {
		this.element = element2;
		checkBox = new JCheckBox(element.getTitle());
		checkBox.setToolTipText(element.getToolTipText());
		checkBox.setSelected(fieldValueReader.readValue(element
				.getFieldDescriptor()));
		checkBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				ChangeFieldValueCommand<Boolean> changeFieldValueCommand;
				changeFieldValueCommand = new ChangeFieldValueCommand<Boolean>(new Boolean(checkBox.isSelected()), element
						.getFieldDescriptor());
				commandManager.performCommand(changeFieldValueCommand);
			}
			
		});
		return checkBox;
	}

}

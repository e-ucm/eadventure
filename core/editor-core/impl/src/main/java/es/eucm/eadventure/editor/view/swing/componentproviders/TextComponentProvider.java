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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.control.commands.impl.ChangeEAdStringValueCommand;
import es.eucm.eadventure.editor.control.commands.impl.ChangeFieldValueCommand;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.gui.EAdTextField;

public class TextComponentProvider implements ComponentProvider<TextOption, EAdTextField> {

	private FieldValueReader fieldValueReader;
	
	private CommandManager commandManager;
	
	public TextComponentProvider(FieldValueReader fieldValueReader, CommandManager commandManager) {
		this.fieldValueReader = fieldValueReader;
	}
	
	@Override
	public EAdTextField getComponent(TextOption element) {
		EAdTextField textField = new EAdTextField(element.getTitle(), 20);
		textField.setToolTipText(element.getToolTipText());
		textField.setText(fieldValueReader.readValue(element.getFieldDescriptor()));
		
		textField.getDocument().addDocumentListener(new TextFieldDocumentListener(element, textField));

		return textField;
	}
	
	/**
	 * Document listener for the field, used to detect changes and modify the
	 * value of the string using {@link ChangeEAdStringValueCommand}s.
	 */
	private class TextFieldDocumentListener implements DocumentListener {

		private TextOption element;

		/**
		 * The actual JTextComponent, used to get the current value
		 */
		private JTextComponent textField;

		/**
		 * @param element
		 *            The {@link TextOption}
		 * @param textField
		 *            The JTextComponent
		 */
		public TextFieldDocumentListener(TextOption element, JTextComponent textField) {
			this.element = element;
			this.textField = textField;
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			ChangeFieldValueCommand<String> changeFieldValueCommand;
			changeFieldValueCommand = new ChangeFieldValueCommand<String>(textField.getText(), element
					.getFieldDescriptor());
			commandManager.performCommand(changeFieldValueCommand);
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			ChangeFieldValueCommand<String> changeFieldValueCommand;
			changeFieldValueCommand = new ChangeFieldValueCommand<String>(textField.getText(), element
					.getFieldDescriptor());
			commandManager.performCommand(changeFieldValueCommand);
		}

	}

}

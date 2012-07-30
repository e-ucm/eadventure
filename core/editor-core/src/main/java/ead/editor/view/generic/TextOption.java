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
import ead.editor.control.commands.ChangeEAdStringValueCommand;
import ead.editor.control.commands.ChangeFieldValueCommand;
import ead.editor.view.generic.FieldDescriptor;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class TextOption extends AbstractOption<String> {

	public static enum ExpectedLength {SHORT, NORMAL, LONG }

	private ExpectedLength expectedLength;

	public TextOption(String title, String toolTipText, FieldDescriptor<String> fieldDescriptor, ExpectedLength expectedLength) {
		super(title, toolTipText, fieldDescriptor);
		this.expectedLength = expectedLength;
	}

	public TextOption(String title, String toolTipText,
			FieldDescriptor<String> fieldDescriptor) {
		this(title, toolTipText, fieldDescriptor, ExpectedLength.NORMAL);
	}

	public ExpectedLength getExpectedLength() {
		return expectedLength;
	}

	public void setExpectedLength(ExpectedLength expectedLength) {
		this.expectedLength = expectedLength;
	}

	@Override
	public JTextField getComponent(CommandManager manager) {
		JTextField textField = new JTextField(getTitle(), 20);
		textField.setToolTipText(getToolTipText());
		textField.setText(read(getFieldDescriptor()));

		textField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                manager, textField));

		return textField;
	}

	/**
	 * Document listener for the field, used to detect changes and modify the
	 * value of the string using {@link ChangeEAdStringValueCommand}s.
	 */
	private class TextFieldDocumentListener implements DocumentListener {

        private CommandManager manager;

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
		public TextFieldDocumentListener(CommandManager manager, JTextComponent textField) {
			this.textField = textField;
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			ChangeFieldValueCommand<String> changeFieldValueCommand;
			changeFieldValueCommand = new ChangeFieldValueCommand<String>(textField.getText(),
					getFieldDescriptor());
			manager.performCommand(changeFieldValueCommand);
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			ChangeFieldValueCommand<String> changeFieldValueCommand;
			changeFieldValueCommand = new ChangeFieldValueCommand<String>(textField.getText(),
                    getFieldDescriptor());
			manager.performCommand(changeFieldValueCommand);
		}
	}
}

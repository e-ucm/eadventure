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

package es.eucm.eadventure.editor.view.swing;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.control.commands.impl.ChangeEAdStringValueCommand;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.gui.EAdTextField;

public class EAdStringComponentProvider implements ComponentProvider<EAdStringOption, EAdTextField> {

	private EAdStringOption element;
	
	private StringHandler stringHandler;
	
	private FieldValueReader fieldValueReader;
	
	private CommandManager commandManger;

	public EAdStringComponentProvider(StringHandler stringHandler,
			FieldValueReader fieldValueReader, CommandManager commandManager) {
		this.stringHandler = stringHandler;
		this.fieldValueReader = fieldValueReader;
		this.commandManger = commandManager;
	}
	
	@Override
	public EAdTextField getComponent(EAdStringOption element2) {
		this.element = element2;
		EAdTextField textField;
		switch (element.getExpectedLength()) {
		case LONG:
			textField = new EAdTextField(element.getTitle(), 60);
			break;
		case SHORT:
			textField = new EAdTextField(element.getTitle(), 20);
			break;
		case NORMAL:
		default:
			textField = new EAdTextField(element.getTitle());
		}
		textField.setToolTipText(element.getToolTipText());
		
		textField.setText(stringHandler.getString(fieldValueReader.readValue(element.getFieldDescriptor())));
		
		textField.getDocument().addDocumentListener(new TextFieldDocumentListener(
				fieldValueReader.readValue(element.getFieldDescriptor()), textField));

		return textField;
	}
	
	private class TextFieldDocumentListener implements DocumentListener {

		private EAdString key;
		
		private EAdTextField textField;
		
		public TextFieldDocumentListener(EAdString key, EAdTextField textField) {
			this.key = key;
			this.textField = textField;
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			commandManger.performCommand(
					new ChangeEAdStringValueCommand(key, textField.getText(), stringHandler));
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			commandManger.performCommand(
					new ChangeEAdStringValueCommand(key, textField.getText(), stringHandler));
		}
		
	}

}

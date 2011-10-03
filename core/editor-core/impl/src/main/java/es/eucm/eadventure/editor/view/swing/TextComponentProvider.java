package es.eucm.eadventure.editor.view.swing;

import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.gui.EAdTextField;

public class TextComponentProvider implements ComponentProvider<TextOption, EAdTextField> {

	private TextOption element;
	
	private EAdTextField textField;
	
	private FieldValueReader fieldValueReader;
	
	public TextComponentProvider(FieldValueReader fieldValueReader) {
		this.fieldValueReader = fieldValueReader;
	}
	
	@Override
	public void setElement(TextOption element2) {
		this.element = element2;
		textField = new EAdTextField(element.getTitle(), 20);
		textField.setToolTipText(element.getToolTipText());
		textField.setText(fieldValueReader.readValue(element.getFieldDescriptor()));
	}

	@Override
	public EAdTextField getComponent() {
		return textField;
	}

}

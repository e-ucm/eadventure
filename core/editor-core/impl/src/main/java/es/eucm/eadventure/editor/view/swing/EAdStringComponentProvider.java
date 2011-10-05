package es.eucm.eadventure.editor.view.swing;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.gui.EAdTextField;

public class EAdStringComponentProvider implements ComponentProvider<EAdStringOption, EAdTextField> {

	private EAdStringOption element;
	
	private EAdTextField textField;
	
	private StringHandler stringHandler;
	
	private FieldValueReader fieldValueReader;

	public EAdStringComponentProvider(StringHandler stringHandler, FieldValueReader fieldValueReader) {
		this.stringHandler = stringHandler;
		this.fieldValueReader = fieldValueReader;
	}
	
	@Override
	public EAdTextField getComponent(EAdStringOption element2) {
		this.element = element2;
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
		//TODO String handler in the editor?
		//textField.setText(element.getFieldDescriptor().readValue());

		return textField;
	}

}

package es.eucm.eadventure.editor.view.swing;

import javax.swing.JCheckBox;

import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.BooleanOption;

public class BooleanComponentProvider implements ComponentProvider<BooleanOption, JCheckBox> {

	private BooleanOption element;
	
	private JCheckBox checkBox;
	
	private FieldValueReader fieldValueReader;
	
	public BooleanComponentProvider(FieldValueReader fieldValueReader) {
		this.fieldValueReader = fieldValueReader;
	}
	
	@Override
	public JCheckBox getComponent(BooleanOption element2) {
		this.element = element2;
		checkBox = new JCheckBox(element.getTitle());
		checkBox.setToolTipText(element.getToolTipText());
		checkBox.setSelected(fieldValueReader.readValue(element.getFieldDescriptor()));
		return checkBox;
	}

}

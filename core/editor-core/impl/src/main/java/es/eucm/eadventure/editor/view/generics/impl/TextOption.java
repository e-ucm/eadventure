package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

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

}

package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public class EAdStringOption extends AbstractOption<EAdString> {

	public static enum ExpectedLength {SHORT, NORMAL, LONG }
	
	private ExpectedLength expectedLength;
	
	public EAdStringOption(String title, String toolTipText, FieldDescriptor<EAdString> fieldDescriptor, ExpectedLength expectedLength) {
		super(title, toolTipText, fieldDescriptor);
		this.expectedLength = expectedLength;
	}

	public EAdStringOption(String title, String toolTipText, FieldDescriptor<EAdString> fieldDescriptor) {
		this(title, toolTipText, fieldDescriptor, ExpectedLength.NORMAL);
	}

	public ExpectedLength getExpectedLength() {
		return expectedLength;
	}
	
	public void setExpectedLength(ExpectedLength expectedLength) {
		this.expectedLength = expectedLength;
	}

}

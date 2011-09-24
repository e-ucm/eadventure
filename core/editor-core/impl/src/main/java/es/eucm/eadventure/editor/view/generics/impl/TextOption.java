package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.Option;

public class TextOption extends AbstractOption implements Option {

	public TextOption(String title, String toolTipText, FieldDescriptor fieldDescriptor) {
		super(title, toolTipText, fieldDescriptor);
	}

}

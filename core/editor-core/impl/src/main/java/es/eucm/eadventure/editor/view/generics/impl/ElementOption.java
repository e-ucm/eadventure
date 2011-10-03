package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public class ElementOption extends AbstractOption<EAdElement> {

	public ElementOption(String title, String toolTipText, FieldDescriptor<EAdElement> fieldDescriptor) {
		super(title, toolTipText, fieldDescriptor);
	}

}

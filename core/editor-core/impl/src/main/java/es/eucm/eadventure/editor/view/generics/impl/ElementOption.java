package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public class ElementOption<E extends EAdElement> extends AbstractOption<E> {

	public ElementOption(String title, String toolTipText, FieldDescriptor<E> fieldDescriptor) {
		super(title, toolTipText, fieldDescriptor);
	}

}

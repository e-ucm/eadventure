package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public class EAdStringOption extends AbstractOption<EAdString> {

	public EAdStringOption(String title, String toolTipText, FieldDescriptor<EAdString> fieldDescriptor) {
		super(title, toolTipText, fieldDescriptor);
	}

}

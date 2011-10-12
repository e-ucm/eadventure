package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.editor.view.generics.EAdListFieldDescriptor;
import es.eucm.eadventure.editor.view.generics.Option;

public class EAdListOption<S> extends AbstractOption<EAdList<S>> implements Option<EAdList<S>> {

	public EAdListOption(String label, String toolTipText,
			EAdListFieldDescriptor<S> fieldDescriptor) {
		super(label, toolTipText, fieldDescriptor);
	}

	public EAdListFieldDescriptor<S> getListFieldDescriptor() {
		return (EAdListFieldDescriptor<S>) fieldDescriptor;
	}

	
	
}

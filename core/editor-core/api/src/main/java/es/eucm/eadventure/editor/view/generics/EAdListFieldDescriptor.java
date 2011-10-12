package es.eucm.eadventure.editor.view.generics;

import es.eucm.eadventure.common.model.extra.EAdList;

public interface EAdListFieldDescriptor<S> extends FieldDescriptor<EAdList<S>> {

	int getCount();
	
	S getElementAt(int pos);
	
	Panel getPanel(int pos, boolean selected);

	EAdList<S> getList();
	
	//TODO add, remove, etc.?
	
}

package es.eucm.eadventure.editor.view.generics;

public interface ListFieldDescriptor<S> extends FieldDescriptor<S> {

	int getCount();
	
	Object getElementAt(int pos);
	
	Panel getPanel(int pos, boolean selected);
	
	//TODO add, remove, etc.?
	
}

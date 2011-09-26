package es.eucm.eadventure.editor.view.generics;

public interface Option<S> extends InterfaceElement {

	String getTitle();
	
	String getToolTipText();
	
	FieldDescriptor<S> getFieldDescriptor();

}

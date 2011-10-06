package es.eucm.eadventure.editor.control;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public interface FieldValueReader {

	//TODO use generics?
	<S> S readValue(FieldDescriptor<S> fieldDescriptor);

}

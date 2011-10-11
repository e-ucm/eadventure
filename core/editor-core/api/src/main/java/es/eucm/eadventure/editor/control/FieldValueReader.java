package es.eucm.eadventure.editor.control;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

/**
 * Reader for the value of a field in an element.
 * <p>
 * This class should be extended and implemented for different platforms, and
 * injected through guice.
 */
public interface FieldValueReader {

	// TODO use generics?
	/**
	 * @param fieldDescriptor
	 *            The descriptor of the field
	 * @return The value of the field for the element described by it
	 */
	<S> S readValue(FieldDescriptor<S> fieldDescriptor);

}

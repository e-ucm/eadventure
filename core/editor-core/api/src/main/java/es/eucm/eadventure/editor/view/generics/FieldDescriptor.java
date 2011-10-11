package es.eucm.eadventure.editor.view.generics;

/**
 * Descriptor for the field of an element.
 * <p>
 * The field is identified by a name (used to infer the value though
 * introspection). The descriptor has a method to get the element for which the
 * field is defined.
 * 
 * @param <S>
 *            The type of the field (e.g. String, Boolean, etc)
 */
public interface FieldDescriptor<S> {

	/**
	 * @return the element for which the field is defined
	 */
	Object getElement();

	/**
	 * @return the name of the field in the element (should be of type S)
	 */
	String getFieldName();

}

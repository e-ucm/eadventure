package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

/**
 * Generic implementation of {@link FieldDescriptor}
 * 
 * @param <S>
 */
public class FieldDescriptorImpl<S> implements FieldDescriptor<S> {

	/**
	 * Used for introspection
	 */
	protected String fieldName;

	/**
	 * The element where the value is stored
	 */
	protected Object element;

	/**
	 * @param element
	 *            The element where the value is stored
	 * @param fieldName
	 *            The name of the field
	 */
	public FieldDescriptorImpl(Object element, String fieldName) {
		this.element = element;
		this.fieldName = fieldName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.view.generics.FieldDescriptor#getElement()
	 */
	@Override
	public Object getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.editor.view.generics.FieldDescriptor#getFieldName()
	 */
	@Override
	public String getFieldName() {
		return fieldName;
	}

}

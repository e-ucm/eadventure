package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public class FieldDescriptorImpl<S> implements FieldDescriptor<S> {

	/**
	 * Used for introspection
	 */
	protected String fieldName;
	
	protected Object element;
	
	public FieldDescriptorImpl(Object element, String fieldName) {
		this.element = element;
		this.fieldName = fieldName;
	}
	
	@Override
	public Object getElement() {
		return element;
	}
	
	@Override
	public String getFieldName() {
		return fieldName;
	}

}

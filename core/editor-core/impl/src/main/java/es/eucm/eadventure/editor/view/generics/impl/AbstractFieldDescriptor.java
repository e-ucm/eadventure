package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public abstract class AbstractFieldDescriptor implements FieldDescriptor {

	/**
	 * Used for introspection
	 */
	protected String fieldName;
	
	protected Class<?> element;
	
	public AbstractFieldDescriptor(Class<?> element, String fieldName) {
		this.element = element;
		this.fieldName = fieldName;
	}

}

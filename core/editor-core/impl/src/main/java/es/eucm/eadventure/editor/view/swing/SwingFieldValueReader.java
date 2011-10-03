package es.eucm.eadventure.editor.view.swing;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public class SwingFieldValueReader implements FieldValueReader {

	/**
	 * Descriptor for the corresponding property
	 */
	protected PropertyDescriptor pd;

	/**
	 * reads the value of the model object that is being configured
	 *  
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <S> S readValue(FieldDescriptor<S> fieldDescriptor) {
		try {
			if (pd == null) pd = getPropertyDescriptor(fieldDescriptor.getElement().getClass(), fieldDescriptor.getFieldName());
			return (S) pd.getReadMethod().invoke(fieldDescriptor.getElement());				
		} catch (Exception e) {
			throw new RuntimeException("Error reading field " + fieldDescriptor.getFieldName(), e);
		}
	}
	
	/**
	 * Utility method to find a property descriptor for a single property
	 * 
	 * @param c
	 * @param fieldName
	 * @return
	 */
	private static PropertyDescriptor getPropertyDescriptor(Class<?> c, String fieldName) {
		try {
			for (PropertyDescriptor pd : 
				Introspector.getBeanInfo(c).getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					return pd;
				}
			}
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("Could not find getters or setters for field " 
					+ fieldName + " in class " + c.getCanonicalName());
		}
		return null;
	}

}

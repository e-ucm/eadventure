package es.eucm.eadventure.editor.view.swing;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import es.eucm.eadventure.editor.view.generics.impl.AbstractFieldDescriptor;

public class SwingFieldDescriptor extends AbstractFieldDescriptor {

	/**
	 * Descriptor for the corresponding property
	 */
	protected PropertyDescriptor pd;

	public SwingFieldDescriptor(Class<?> element, String fieldName) {
		super(element, fieldName);
	}

	/**
	 * reads the value of the model object that is being configured
	 *  
	 * @return
	 */
	@Override
	public Object readValue() {
		try {
			if (pd == null) pd = getPropertyDescriptor(element.getClass(), fieldName);
			return pd.getReadMethod().invoke(element);				
		} catch (Exception e) {
			throw new RuntimeException("Error reading field " + fieldName, e);
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

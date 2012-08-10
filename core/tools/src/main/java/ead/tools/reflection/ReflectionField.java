package ead.tools.reflection;

import java.lang.annotation.Annotation;

/**
 * General interface for reflection fields
 * 
 */
public interface ReflectionField {

	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

	/**
	 * Returns the field name
	 * 
	 * @return
	 */
	String getName();

	ReflectionClass<?> getType();

	/**
	 * Returns the value for the field in the given object
	 * 
	 * @param object
	 * @return
	 */
	Object getFieldValue(Object object);

	/**
	 * Sets the value for in this field in the given object 
	 * @param object
	 * @param value
	 */
	void setFieldValue(Object object, Object value);

}

package ead.tools.reflection;

import java.util.Collection;

public interface ReflectionClass<T> {

	ReflectionConstructor<T> getConstructor();

	ReflectionField getField(String name);

	/**
	 * Returns a list with all fields in the class
	 * 
	 * @return
	 */
	Collection<ReflectionField> getFields();

	/**
	 * Returns the superclass of this class
	 * 
	 * @return
	 */
	ReflectionClass<?> getSuperclass();
	
	/**
	 * Return the class contained by this reflectin class
	 * @return
	 */
	Class<?> getType();

}

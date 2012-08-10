package ead.tools.java.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;

public class JavaReflectionField implements ReflectionField {

	private Field field;

	public JavaReflectionField(Field field) {
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public ReflectionClass<?> getType() {
		return ReflectionClassLoader.getReflectionClass(field.getType());
	}

	@Override
	public Object getFieldValue(Object object) {
		try {
			return field.get(object);
		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
		return null;
	}

	@Override
	public void setFieldValue(Object object, Object value) {
		try {
			field.set(object, value);
		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

}

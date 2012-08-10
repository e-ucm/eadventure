package ead.tools.gwt.reflection;

import java.lang.annotation.Annotation;

import com.gwtent.reflection.client.Field;

import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;

public class GwtReflectionField implements ReflectionField {

	private Field field;

	public GwtReflectionField(Field field) {
		this.field = field;
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
		return ReflectionClassLoader.getReflectionClass(field
				.getDeclaringClass());
	}

	@Override
	public Object getFieldValue(Object object) {
		return field.getFieldValue(object);
	}

	@Override
	public void setFieldValue(Object object, Object value) {
		field.setFieldValue(object, value);
	}

}

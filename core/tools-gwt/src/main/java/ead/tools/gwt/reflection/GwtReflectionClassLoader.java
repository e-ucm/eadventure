package ead.tools.gwt.reflection;

import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;

public class GwtReflectionClassLoader extends ReflectionClassLoader {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected ReflectionClass<?> getReflectionClassImpl(Class<?> clazz) {
		return new GwtReflectionClass(clazz);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected ReflectionClass<?> getReflectionClassImpl(String clazz) {
		try {
			return new GwtReflectionClass(Class.forName(clazz));
		} catch (ClassNotFoundException e) {
			logger.error("Class not found {}", clazz);
		}
		return null;
	}

}

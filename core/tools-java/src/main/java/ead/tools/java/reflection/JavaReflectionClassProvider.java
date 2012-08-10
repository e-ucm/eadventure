package ead.tools.java.reflection;

import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;

public class JavaReflectionClassProvider extends ReflectionClassLoader {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected ReflectionClass<?> getReflectionClassImpl(Class<?> clazz) {
		return new JavaReflectionClass(clazz);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected ReflectionClass<?> getReflectionClassImpl(String clazz) {
		try {
			return new JavaReflectionClass(Class.forName(clazz));
		} catch (ClassNotFoundException e) {
			logger.error("Class not found {}", clazz);
		}
		return null;
	}

}

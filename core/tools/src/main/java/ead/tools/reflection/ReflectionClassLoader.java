package ead.tools.reflection;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * General abstract class to transform string representing classes into
 * {@link ReflectionClass}
 * 
 */
public abstract class ReflectionClassLoader {

	protected static final Logger logger = LoggerFactory
			.getLogger("ReflectionClassLoader");

	private static ReflectionClassLoader reflectionClassLoader;

	private static Map<String, ReflectionClass<?>> classes = new HashMap<String, ReflectionClass<?>>();

	protected abstract ReflectionClass<?> getReflectionClassImpl(Class<?> clazz);

	protected abstract ReflectionClass<?> getReflectionClassImpl(String clazz);

	public static void init(ReflectionClassLoader loader) {
		reflectionClassLoader = loader;
	}

	public static ReflectionClass<?> getReflectionClass(Class<?> className) {
		if (!classes.containsKey(className.getName())) {
			if (reflectionClassLoader == null) {
				logger.error("Reflection class loader not initialized. init() method should be called before use getReflectionClass() method");
				return null;
			} else {
				classes.put(className.getName(),
						reflectionClassLoader.getReflectionClassImpl(className));
			}
		}
		return classes.get(className.getName());
	}

	public static ReflectionClass<?> getReflectionClass(String clazz) {
		if (!classes.containsKey(clazz)) {
			if (reflectionClassLoader == null) {
				logger.error("Reflection class loader not initialized. init() method should be called before use getReflectionClass() method");
				return null;
			} else {
				classes.put(clazz,
						reflectionClassLoader.getReflectionClassImpl(clazz));
			}
		}
		return classes.get(clazz);
	}

}

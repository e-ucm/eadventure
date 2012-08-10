package ead.tools.java.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionConstructor;
import ead.tools.reflection.ReflectionField;

public class JavaReflectionClass<T> implements ReflectionClass<T> {

	private Class<T> clazz;

	private ReflectionConstructor<T> constructor;

	private Map<String, ReflectionField> fields;
	
	private ReflectionClass<?> superClass;

	private boolean allFieldsAdded;

	public JavaReflectionClass(Class<T> clazz) {
		this.clazz = clazz;
		this.fields = new HashMap<String, ReflectionField>();
		this.allFieldsAdded = false;
	}

	@Override
	public ReflectionConstructor<T> getConstructor() {
		if (constructor == null) {
			try {
				Constructor<T> c = clazz.getConstructor();
				constructor = new JavaReflectionConstructor<T>(c);
			} catch (SecurityException e) {

			} catch (NoSuchMethodException e) {

			}
		}

		return constructor;
	}

	@Override
	public ReflectionField getField(String name) {
		if (!fields.containsKey(name)) {
			try {
				fields.put(name,
						new JavaReflectionField(clazz.getDeclaredField(name)));
			} catch (SecurityException e) {

			} catch (NoSuchFieldException e) {

			}
		}
		return fields.get(name);
	}

	@Override
	public Collection<ReflectionField> getFields() {
		if (!allFieldsAdded) {
			allFieldsAdded = true;
			for (Field f : clazz.getDeclaredFields()) {
				if (!fields.containsKey(f.getName())) {
					getField(f.getName());
				}
			}
		}
		return fields.values();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ReflectionClass<?> getSuperclass() {
		if ( superClass == null ){
			superClass = new JavaReflectionClass( clazz.getSuperclass() );
		}
		return superClass;
	}

	@Override
	public Class<?> getType() {
		return clazz;
	}

}

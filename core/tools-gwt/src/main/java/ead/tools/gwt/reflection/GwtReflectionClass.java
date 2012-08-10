package ead.tools.gwt.reflection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Constructor;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionConstructor;
import ead.tools.reflection.ReflectionField;

public class GwtReflectionClass<T> implements ReflectionClass<T> {

	private ClassType<T> clazz;

	private ReflectionConstructor<T> constructor;

	private Map<String, ReflectionField> fields;

	private ReflectionClass<?> superClass;

	private boolean allFieldsAdded;

	public GwtReflectionClass(Class<T> clazz) {
		this.clazz = TypeOracle.Instance.getClassType(clazz);
		this.fields = new HashMap<String, ReflectionField>();
		this.allFieldsAdded = false;
	}

	@Override
	public ReflectionConstructor<T> getConstructor() {
		if (constructor == null) {
			Constructor<T> c = clazz.findConstructor();
			constructor = new GwtReflectionConstructor<T>(c);
		}

		return constructor;
	}

	@Override
	public ReflectionField getField(String name) {
		if (!fields.containsKey(name)) {
			fields.put(name, new GwtReflectionField(clazz.getField(name)));
		}
		return fields.get(name);
	}

	@Override
	public Collection<ReflectionField> getFields() {
		if (!allFieldsAdded) {
			allFieldsAdded = true;
			for (Field f : clazz.getFields()) {
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
		if (clazz.getDeclaringClass() == Object.class) {
			return null;
		}

		if (superClass == null) {
			superClass = new GwtReflectionClass(clazz.getSuperclass().getDeclaringClass());
		}
		return superClass;
	}

	@Override
	public Class<?> getType() {
		return clazz.getDeclaringClass();
	}

}

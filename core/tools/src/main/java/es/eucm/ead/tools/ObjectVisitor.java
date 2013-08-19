package es.eucm.ead.tools;

import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ObjectVisitor {

	private Stack<Object> parents;

	private List<ObjectListener> listeners;

	public ObjectVisitor() {
		parents = new Stack<Object>();
		listeners = new ArrayList<ObjectListener>();
	}

	public void addListener(ObjectListener l) {
		listeners.add(l);
	}

	public void visit(Object object) {
		parents.clear();
		visitImpl(object);
	}

	private void visitImpl(Object object) {
		if (parents.contains(object)) {
			return;
		}

		parents.push(object);
		for (ObjectListener l : listeners) {
			l.visit(object);
		}

		if (object instanceof EAdList) {
			for (Object value : (EAdList) object) {
				visitImpl(value);
			}
		} else if (object instanceof EAdMap) {
			EAdMap<?, ?> map = (EAdMap) object;
			for (Map.Entry e : map.entrySet()) {
				parents.push(e);
				visitImpl(e.getKey());
				visitImpl(e.getValue());
				parents.pop();
			}
		} else if (object instanceof Boolean || object instanceof Number
				|| object instanceof String) {
			return;
		} else if (object instanceof Object) {
			ReflectionClass<?> clazz = ReflectionClassLoader
					.getReflectionClass(object.getClass());
			while (clazz != null) {
				for (ReflectionField f : clazz.getFields()) {
					Object value = f.getFieldValue(object);
					if (!parents.contains(value)) {
						visitImpl(value);
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		parents.pop();
	}

	public static interface ObjectListener {
		void visit(Object o);
	}

}

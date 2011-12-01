package es.eucm.eadventure.common.impl.writer;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.resources.EAdResources;

public abstract class FieldParamWriter<T> extends DOMWriter<T> {

	public void processParams(Element node, T data) {
		DOMWriter.depthManager.levelUp();

		Class<?> clazz = data.getClass();

		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				try {
					Param param = field.getAnnotation(Param.class);
					if (param != null) {
						PropertyDescriptor pd = getPropertyDescriptor(data.getClass(), field.getName());
						if (pd == null)
							logger.severe("Missing descriptor for " + field.getName() + " in " + data.getClass());
						Method method = pd.getReadMethod();
						if (method == null)
							logger.severe("Missing read method for " + field.getName() + " in " + data.getClass());
						Object o = method.invoke(data);

						if (!isEmpty(o)) {
							Element newNode = super.initNode(o, null);
							if (!DOMWriter.USE_DEFAULT_VALUES || !newNode.getTextContent().equals(param.defaultValue())) {
								newNode.setAttribute(DOMTags.PARAM_AT, param.value());
								doc.adoptNode(newNode);
								node.appendChild(newNode);
							}
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		}

		DOMWriter.depthManager.levelDown();
	}
	
	
	/**
	 * Utility method to find a property descriptor for a single property
	 * 
	 * @param c
	 * @param fieldName
	 * @return
	 */
	private static PropertyDescriptor getPropertyDescriptor(Class<?> c, String fieldName) {
		try {
			for (PropertyDescriptor pd : 
				Introspector.getBeanInfo(c).getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					return pd;
				}
			}
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("Could not find getters or setters for field " 
					+ fieldName + " in class " + c.getCanonicalName());
		}
		return null;
	}


	/**
	 * Determines if an object is empty (whether it's null or whether is empty,
	 * in case of being a list or a map)
	 * 
	 * @param o
	 *            the object to check
	 * @return
	 */
	private boolean isEmpty(Object o) {
		if (o == null)
			return true;
		if (o instanceof EAdList && ((EAdList<?>) o).size() == 0) {
			return true;
		}

		if (o instanceof EAdMap && ((EAdMap<?, ?>) o).isEmpty()) {
			return true;
		}
		
		if ( o instanceof EAdResources && ((EAdResources) o).isEmpty())
			return true;
		
		return false;
	}

}

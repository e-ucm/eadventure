package es.eucm.eadventure.common.impl.writer;

import java.lang.reflect.Field;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.resources.EAdResources;

public abstract class FieldParamWriter<T> extends DOMWriter<T> {

	public void processParams(Element node, T data) {
		Class<?> clazz = data.getClass();

		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				try {
					Param param = field.getAnnotation(Param.class);
					if (param != null) {
						boolean accessible = field.isAccessible();
						field.setAccessible(true);
						Object o = field.get(data);

						if (!isEmpty(o)) {
							Element newNode = super.initNode(o, null);
							newNode.setAttribute(DOMTags.PARAM_AT, param.value());
							doc.adoptNode(newNode);
							node.appendChild(newNode);
						}

						field.setAccessible(accessible);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		}

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

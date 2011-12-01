package es.eucm.eadventure.common.impl.reader.visitors;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.impl.reader.extra.ObjectFactory;
import es.eucm.eadventure.common.interfaces.Param;

public abstract class NodeVisitor<T> {
	
	protected static final Logger logger = Logger.getLogger("NodeVisitor");

	private static String packageName;
	
	public static Map<String, String> aliasMap = new HashMap<String, String>();
	
	protected static String loaderType;

	public static void init(String packageN) {
		packageName = packageN;
		loaderType = DOMTags.CLASS_AT;
		//loaderType = DOMTags.TYPE_AT;
		aliasMap.clear();
	}

	public abstract T visit(Node node, Field field, Object parent, Class<?> listClass);
	
	public abstract String getNodeType();
	
	protected String translateClass(String clazz) {
		if (aliasMap.containsKey(clazz))
			clazz = aliasMap.get(clazz);
		return clazz != null && clazz.startsWith(".") ? packageName + clazz : clazz;
	}
	
	protected void readFields(Object element, Node node) {
		initilizeDefaultValues(element);

		NodeList nl = node.getChildNodes();
		
		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			Node newNode = nl.item(i);
			Field field = getField(element, newNode.getAttributes().getNamedItem(DOMTags.PARAM_AT).getNodeValue());

			if (VisitorFactory.getVisitor(newNode.getNodeName()).visit(newNode, field, element, null) == null)
				logger.severe("Failed visiting node " + newNode.getNodeName() + " for element " + element.getClass());
		}
	}
	
	private void initilizeDefaultValues(Object element) {
		Class<?> clazz = element.getClass();
		while (clazz != null) {
			for (Field f : clazz.getDeclaredFields()) {
				Param a = f.getAnnotation(Param.class);
				if (a != null && (a.defaultValue() != null && !a.defaultValue().equals(""))) {
					Class<?> type = f.getType();
					String value = a.defaultValue();
					Object object = ObjectFactory.getObject(value, type);
					setValue(f, element, object);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}
	
	public static void setValue(Field field, Object parent, Object object) {
		/*
		if (field != null && parent != null) {
			boolean accessible = field.isAccessible();

			try {
				field.setAccessible(true);
				field.set(parent, object);
			} catch (IllegalArgumentException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} finally {
				field.setAccessible(accessible);
			}
		}*/
		
		if (field != null && parent != null) {
			PropertyDescriptor pd = getPropertyDescriptor(parent.getClass(), field.getName());
			if (pd == null)
				logger.severe("Missing descriptor for " + field.getName() + " in " + parent.getClass());
			Method method = pd.getWriteMethod();
			if (method == null)
				logger.severe("Missing write method for " + field.getName() + " in " + parent.getClass());
			try {
				Object o = method.invoke(parent, object);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Gets the {@link Field} object identified by the given id. It gives
	 * precedence to Fields annotated with the id though the {@link Param}
	 * annotation, if non is found it checks if the id coincides with the name
	 * of a field.
	 * 
	 * @param object
	 *            The object for where the field should be
	 * @param paramValue
	 *            The id of the field
	 * @return The field corresponding to the given id
	 */
	public Field getField(Object object, String paramValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			for (Field f : clazz.getDeclaredFields()) {
				Param a = f.getAnnotation(Param.class);
				if (a != null && a.value().equals(paramValue))
					return f;
			}
			clazz = clazz.getSuperclass();
		}
		return null;
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

}

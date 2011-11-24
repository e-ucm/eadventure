package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import java.util.logging.Logger;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


import es.eucm.eadventure.common.interfaces.Param;

public abstract class NodeVisitor<T> {
	
	protected static final Logger logger = Logger.getLogger("NodeVisitor");

	private static String packageName;
	
	protected static String loaderType;

	public static void init(String packageN) {
		packageName = packageN;
		loaderType = DOMTags.CLASS_AT;
		//loaderType = DOMTags.TYPE_AT;
	}

	public abstract T visit(Node node, Field field, Object parent);
	
	public abstract String getNodeType();
	
	protected String translateClass(String clazz) {
		return clazz != null && clazz.startsWith(".") ? packageName + clazz : clazz;
	}
	
	protected void readFields(Object element, Node node) {
		NodeList nl = node.getChildNodes();
		
		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			Node newNode = nl.item(i);
			if (newNode == null || newNode.getAttributes() == null)
				logger.severe("Unexpected null");

			Field field = getField(element, newNode.getAttributes().getNamedItem(DOMTags.PARAM_AT).getNodeValue());

			if (VisitorFactory.getVisitor(newNode.getNodeName()).visit(newNode, field, element) == null)
				logger.severe("Failed visiting node " + newNode.getNodeName() + " for element " + element.getClass());
		}
	}
	
	protected void setValue(Field field, Object parent, Object object) {
		if (field != null && parent != null) {
			//TODO needs to be accessible?
			try {
				field.setFieldValue(parent, object);
			} catch (ClassCastException c) {
				logger.severe("Class cast exception " + parent + " field " + field.getName());
				c.printStackTrace();
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
		ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);
		
		while (classType != null) {
			for (Field f : classType.getFields()) {
				if (f.getName().equals(paramValue))
					return f;
				if (f.getAnnotation(Param.class) != null) {
					Param p = f.getAnnotation(Param.class);
					if (p.value().equals(paramValue))
						return f;
				}
			}
			classType = classType.getSuperclass();
		}
		
		logger.severe("No field " + paramValue + " in " + clazz);
/*		
		while (clazz != null) {
			for (Field f : clazz.getDeclaredFields()) {
				Param a = f.getAnnotation(Param.class);
				if (a != null && a.value().equals(paramValue))
					return f;
			}
			clazz = clazz.getSuperclass();
		}*/
		return null;
	}
}

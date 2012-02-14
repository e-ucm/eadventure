/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.common.reader.visitors;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ead.common.DOMTags;
import ead.common.interfaces.Param;
import ead.common.reader.extra.ObjectFactory;

public abstract class NodeVisitor<T> {

	private static final Logger logger = LoggerFactory.getLogger("NodeVisitor");

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
		initializeDefaultValues(element);

		NodeList nl = node.getChildNodes();

		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			Node newNode = nl.item(i);
			Field field = getField(element, newNode.getAttributes().getNamedItem(DOMTags.PARAM_AT).getNodeValue());

			if (VisitorFactory.getVisitor(newNode.getNodeName()).visit(newNode, field, element, null) == null)
				logger.error("Failed visiting node " + newNode.getNodeName() + " for element " + element.getClass());
		}
	}

	private void initializeDefaultValues(Object element) {
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
		if (field != null && parent != null) {
			PropertyDescriptor pd = getPropertyDescriptor(parent.getClass(), field.getName());
			if (pd == null) {
				logger.error("Missing descriptor for {} in {}",
                        new Object[]{field.getName(), parent.getClass()});
            }
            Method method = pd.getWriteMethod();
			if (method == null) {
				logger.error("Missing write method for {} in {}",
                        new Object[]{field.getName(), parent.getClass()});
            }
            try {
				method.invoke(parent, object);
			} catch (Exception e) {
				logger.error("Error setting value for {} in {}",
                        new Object[]{field.getName(), parent.getClass()}, e);
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

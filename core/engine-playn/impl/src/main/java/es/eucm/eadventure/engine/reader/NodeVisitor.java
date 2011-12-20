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

package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.DOMTags;

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

	public abstract T visit(Node node, Field field, Object parent, Class<?> class1);
	
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

			Node attNode = newNode.getAttributes().getNamedItem(DOMTags.PARAM_AT);
			String value = attNode.getNodeValue();
			Field field = getField(element, value);

			if (VisitorFactory.getVisitor(newNode.getNodeName()).visit(newNode, field, element, null) == null)
				logger.severe("Failed visiting node " + newNode.getNodeName() + " for element " + element.getClass() + " in field " + field.getName() + " of class " + field.getType());
		}
	}
	
	private void initilizeDefaultValues(Object element) {
		Class<?> clazz = element.getClass();
		ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);
		
		while (classType != null) {
			for (Field f : classType.getFields()) {
				if (f.getAnnotation(Param.class) != null) {
					Param p = f.getAnnotation(Param.class);
					if (p.defaultValue() != null && !p.defaultValue().equals("")) {
						Object object = f.getFieldValue(element);
						Class<?> type = object.getClass();
						String value = p.defaultValue();
						object = ObjectFactory.getObject(value, type);
						setValue(f, element, object);
					}
				}
			}
			classType = classType.getSuperclass();
		}
	}
	
	public static void setValue(Field field, Object parent, Object object) {
		if (field != null && parent != null) {
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
		return null;
	}
}

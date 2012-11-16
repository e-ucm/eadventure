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

package ead.reader.adventure.visitors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.interfaces.Param;
import ead.common.model.EAdElement;
import ead.common.model.elements.BasicElement;
import ead.reader.adventure.DOMTags;
import ead.reader.adventure.ObjectFactory;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

public abstract class NodeVisitor<T> {

	protected static final Logger logger = LoggerFactory
			.getLogger("NodeVisitor");

	private static String packageName;

	public static Map<String, String> aliasMap = new LinkedHashMap<String, String>();

	protected static String loaderType;

	public static void init(String packageN) {
		packageName = packageN;
		loaderType = DOMTags.CLASS_AT;
		// loaderType = DOMTags.TYPE_AT;
		aliasMap.clear();
	}

	public abstract void visit(XMLNode node, ReflectionField field,
			Object parent, Class<?> class1, NodeVisitorListener listener);

	public abstract String getNodeType();

	protected String translateClass(String clazz) {
		if (aliasMap.containsKey(clazz))
			clazz = aliasMap.get(clazz);
		return clazz != null && clazz.startsWith(".") ? packageName + clazz
				: clazz;
	}

	protected void readFields(Object element, XMLNode node) {
		initializeDefaultValues(element);

		XMLNodeList nl = node.getChildNodes();

		for (int i = 0, total = nl.getLength(); i < total; i++) {
			XMLNode newNode = nl.item(i);

			String value = newNode.getAttributes().getValue(DOMTags.PARAM_AT);
			ReflectionField field = getField(element, value);

			VisitorFactory.getVisitor(newNode.getNodeName()).visit(newNode,
					field, element, null,
					new NodeErrorVisitorListener(newNode, element, field));

		}
	}

	private void initializeDefaultValues(Object element) {
		Class<?> clazz = element.getClass();
		ReflectionClass<?> classType = ReflectionClassLoader
				.getReflectionClass(clazz);

		while (classType != null) {
			for (ReflectionField f : classType.getFields()) {
				if (f.getAnnotation(Param.class) != null) {
					Param p = f.getAnnotation(Param.class);
					if (p.defaultValue() != null
							&& !p.defaultValue().equals("")) {
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

	public static void setValue(ReflectionField field, Object parent,
			Object object) {
		if (field != null && parent != null) {
			try {
				if (logger.isDebugEnabled()) {
					String pid = ((parent instanceof EAdElement) ? 
							((EAdElement)parent).getId() : objectToString(parent));
					String oid = ((object instanceof EAdElement) ? 
							((EAdElement)object).getId() : objectToString(object));
					logger.debug("{}.{} <-- {}", new String[] {
						pid, field.getName(), oid
					});
				}
				field.setFieldValue(parent, object);
			} catch (ClassCastException e) {
				logger.error("Failed to cast field {} to type {} in {}",
						new Object[] { field.getName(), object.getClass(),
								parent }, e);
			}
		}
	}

	/**
	 * Gets the {@link Field} object identified by the given id. It gives
	 * precedence to Fields annotated with the id though the {@link Param}
	 * annotation, if none is found it checks if the id coincides with the name
	 * of a field.
	 * 
	 * @param object
	 *            The object for where the field should be
	 * @param paramValue
	 *            The id of the field
	 * @return The field corresponding to the given id
	 */
	public ReflectionField getField(Object object, String paramValue) {
		Class<?> clazz = object.getClass();
		ReflectionClass<?> classType = ReflectionClassLoader
				.getReflectionClass(clazz);

		while (classType != null) {
			for (ReflectionField f : classType.getFields()) {
				if (f.getName().equals(paramValue))
					return f;
				if (f.getAnnotation(Param.class) != null) {
					Param p = f.getAnnotation(Param.class);
					if (p.value().equals(paramValue)) {
						return f;
					}
				}
			}
			classType = classType.getSuperclass();
		}
		return null;
	}

	public static interface NodeVisitorListener {

		void elementRead(Object element);
	}

	public class NodeErrorVisitorListener implements NodeVisitorListener {

		private XMLNode node;

		private Object element;

		private ReflectionField field;

		public NodeErrorVisitorListener(XMLNode node, Object element,
				ReflectionField field) {
			this.node = node;
			this.element = element;
			this.field = field;
		}

		@Override
		public void elementRead(Object e) {
			if (e == null) {
				logger.error("Failed visiting node {} for element {}"
						+ " in field {} of type {}",
						new Object[] { node.getNodeName(), element.getClass(),
								field.getName(), field.getType() });
			}
		}
	};
	
	/**
	 * GWT does not recognize o.getClass().getSimpleName(). This helper method
	 * should be used instead, and can be handy for debugging purposes.
	 * @param o
	 * @return 
	 */
	public static String objectToString(Object o) {
		if (o == null) {
			return "<null>";
		}
		return BasicElement.classToString(o.getClass()) + "@" + o.hashCode();
	}
}

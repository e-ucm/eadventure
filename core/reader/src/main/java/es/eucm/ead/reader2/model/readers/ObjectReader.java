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

package es.eucm.ead.reader2.model.readers;

import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.reader2.model.ObjectsFactory;
import es.eucm.ead.reader2.model.ReaderVisitor;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;
import es.eucm.ead.tools.xml.XMLNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectReader extends AbstractReader<Identified> {
	static private Logger logger = LoggerFactory.getLogger(ObjectReader.class);

	public ObjectReader(ObjectsFactory objectsFactory,
			ReaderVisitor readerVisitor) {
		super(objectsFactory, readerVisitor);
	}

	@Override
	public Identified read(XMLNode node) {

		// If the node has text, it's a reference to an object already defined
		if (!"".equals(node.getNodeText())) {
			return (Identified) objectsFactory
					.getObjectById(node.getNodeText());
		} else {
			Class<?> clazz = this.getNodeClass(node);
			if (clazz != null) {
				String id = node.getAttributeValue(DOMTags.ID_AT);
				Identified object = (Identified) objectsFactory.createObject(
						clazz, id);
				if (node.hasChildNodes()) {
					for (XMLNode child : node.getChildren()) {
						String fieldName = child
								.getAttributeValue(DOMTags.FIELD_AT);
						fieldName = translateField(fieldName);
						ReflectionField field = getField(clazz, fieldName);

						if (field != null) {
							readerVisitor.loadElement(child,
									new ObjectVisitorListener(object, field));
						} else {
							logger
									.warn(
											"{} param is not present in {}. It'll be ignored",
											new Object[] { fieldName, clazz });
						}
					}
				}
				return object;
			}
			logger.warn("Node with no class {}", node);
			return null;
		}
	}

	/**
	 * Returns the class for the element contained in the given node
	 *
	 * @param node
	 * @return
	 */
	public Class<?> getNodeClass(XMLNode node) {
		String clazz = node.getAttributeValue(DOMTags.CLASS_AT);
		return clazz == null ? null : getNodeClass(clazz);
	}

	public ReflectionField getField(Class<?> clazz, String fieldName) {
		ReflectionClass<?> reflectionClass = ReflectionClassLoader
				.getReflectionClass(clazz);
		while (reflectionClass != null) {
			ReflectionField f = reflectionClass.getField(fieldName);
			if (f != null) {
				Param p = f.getAnnotation(Param.class);
				if (p != null) {
					return f;
				}
			}
			reflectionClass = reflectionClass.getSuperclass();
		}
		return null;
	}

	public Class<?> getNodeClass(String clazz) {
		clazz = translateClass(clazz);
		Class<?> c = null;
		try {
			c = objectsFactory.getClassFromName(clazz);
		} catch (NullPointerException e) {
			logger.error("Error resolving class {}", clazz, e);
		}
		return c;
	}

	public static class ObjectVisitorListener implements
			ReaderVisitor.VisitorListener {

		private Object parent;

		private ReflectionField field;

		public ObjectVisitorListener(Object parent, ReflectionField field) {
			this.parent = parent;
			this.field = field;
		}

		@Override
		public boolean loaded(XMLNode node, Object object,
				boolean isNullInOrigin) {
			if (object != null || isNullInOrigin) {
				field.setFieldValue(parent, object);
				return true;
			}
			return false;
		}

	}
}

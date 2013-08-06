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

package es.eucm.ead.reader.model.readers;

import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.reader.model.ObjectsFactory;
import es.eucm.ead.reader.model.XMLVisitor;
import es.eucm.ead.reader.model.XMLVisitor.VisitorListener;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.tools.xml.XMLNodeList;

public class ObjectReader extends AbstractReader<Identified> {

	public boolean asset;

	public ObjectReader(ObjectsFactory elementsFactory, XMLVisitor xmlVisitor) {
		super(elementsFactory, xmlVisitor);
	}

	public void setAsset(boolean asset) {
		this.asset = asset;
	}

	@Override
	public Identified read(XMLNode node) {
		Identified element = null;

		if (node.getAttributesLength() < 2) {
			if (asset) {
				element = elementsFactory.getAsset(node.getNodeText());
			} else {
				element = elementsFactory.getEAdElement(node.getNodeText());
			}

			return element;

		} else {
			Class<?> clazz = this.getNodeClass(node);
			if (clazz != null) {
				element = (Identified) elementsFactory.createObject(clazz);
				String id = node.getAttributeValue(DOMTags.ID_AT);
				element.setId(id);

				if (asset) {
					elementsFactory.putAsset(id, element);
				} else {
					elementsFactory.putEAdElement(id, element);
				}

				if (node.hasChildNodes()) {
					XMLNodeList children = node.getChildNodes();
					for (int i = 0; i < children.getLength(); i++) {
						XMLNode child = children.item(i);
						String fieldName = child
								.getAttributeValue(DOMTags.FIELD_AT);
						fieldName = translateField(fieldName);
						ReflectionField field = getField(clazz, fieldName);

						if (field != null) {
							xmlVisitor.loadElement(child,
									new ObjectVisitorListener(element, field));
						} else {
							logger
									.warn(
											"{} param is not present in {}. It'll be ignored",
											new Object[] { fieldName, clazz });
						}
					}
				}
			}
			return element;
		}

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

	public static class ObjectVisitorListener implements VisitorListener {

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

	public void clear() {
		asset = false;
	}

}

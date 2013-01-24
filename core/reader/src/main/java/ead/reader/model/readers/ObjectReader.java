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

package ead.reader.model.readers;

import java.util.ArrayList;

import ead.common.interfaces.Param;
import ead.common.interfaces.features.Identified;
import ead.reader.DOMTags;
import ead.reader.model.ObjectsFactory;
import ead.reader.model.XMLVisitor;
import ead.reader.model.XMLVisitor.VisitorListener;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

public class ObjectReader extends AbstractReader<Identified> {

	public boolean asset;

	private ArrayList<String> ids;

	public ObjectReader(ObjectsFactory elementsFactory, XMLVisitor xmlVisitor) {
		super(elementsFactory, xmlVisitor);
		ids = new ArrayList<String>();
	}

	public void setAsset(boolean asset) {
		this.asset = asset;
	}

	@Override
	public Identified read(XMLNode node) {
		Identified element = null;

		if (node.getAttributes().getLength() < 2) {
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
				String id = node.getAttributes().getValue(DOMTags.ID_AT);
				element.setId(id);
				if (ids.contains(id)) {
					logger
							.warn(
									"Id {} is duplicated. Game won't work properly",
									id);
				} else {
					ids.add(id);
				}

				if (asset) {
					elementsFactory.putAsset(id, element);
				} else {
					elementsFactory.putEAdElement(id, element);
				}

				XMLNodeList children = node.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					XMLNode child = children.item(i);
					String fieldName = child.getAttributes().getValue(
							DOMTags.FIELD_AT);
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
		ids.clear();
	}

}

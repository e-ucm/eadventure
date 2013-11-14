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

package es.eucm.ead.writer.model.writers;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer.model.WriterVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ObjectWriter implements Writer<Identified> {

	static private Logger logger = LoggerFactory.getLogger(ObjectWriter.class);

	private WriterVisitor modelVisitor;

	private Map<String, Object> pointers;

	public ObjectWriter(WriterVisitor modelVisitor) {
		this.modelVisitor = modelVisitor;
		this.pointers = new HashMap<String, Object>();
	}

	@Override
	public XMLNode write(Identified object, WriterContext context) {
		if (object == null) {
			return null;
		}

		XMLNode node = new XMLNode(DOMTags.ELEMENT_TAG);

		if (object.getId() != null && context.containsId(object.getId())) {
			node.setText(object.getId());
		} else {
			// Set id if necessary
			if (object.getId() == null) {
				object.setId(context.generateNewId());
			}

			object = (Identified) context.process(object, node);
			// If it's a reference
			if (object.getClass() == BasicElement.class) {
				node.setText(object.getId());
			} else {
				node.setAttribute(DOMTags.ID_AT, object.getId());
				ReflectionClass<?> clazz = ReflectionClassLoader
						.getReflectionClass(object.getClass());
				node.setAttribute(DOMTags.CLASS_AT, context
						.translateClass(clazz.getType()));
				while (clazz != null) {
					for (ReflectionField f : clazz.getFields()) {
						// Only store fields annotated with param
						if (f.getAnnotation(Param.class) != null) {
							Object value = f.getFieldValue(object);
							if (value != null) {
								modelVisitor.writeElement(value, object,
										new ObjectWriterListener(context
												.translateField(f.getName()),
												node));
							}
						}
					}
					clazz = clazz.getSuperclass();
				}
			}
		}
		// Detect duplicated ids
		Object o = pointers.get(object.getId());
		if (o != null && o != object) {
			if (o.getClass() != BasicElement.class
					&& object.getClass() != BasicElement.class) {
				logger.error("Duplicated id {}", object.getId());
			}
		} else {
			// We don't add references to the pointers list
			if (object.getClass() != BasicElement.class) {
				pointers.put(object.getId(), object);
			}
		}
		return node;

	}

	public void clear() {
		pointers.clear();
	}

	public static class ObjectWriterListener implements
			WriterVisitor.VisitorListener {

		private String fieldName;

		private XMLNode parent;

		public ObjectWriterListener(String fieldName, XMLNode parent) {
			this.fieldName = fieldName;
			this.parent = parent;
		}

		@Override
		public void load(XMLNode node, Object object) {
			node.setAttribute(DOMTags.FIELD_AT, fieldName);
			parent.append(node);
		}

	}
}

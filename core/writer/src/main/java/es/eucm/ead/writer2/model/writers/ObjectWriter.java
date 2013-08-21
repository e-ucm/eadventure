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

package es.eucm.ead.writer2.model.writers;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer2.model.WriterContext;
import es.eucm.ead.writer2.model.WriterVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectWriter implements Writer<Identified> {

	private static final Logger logger = LoggerFactory
			.getLogger("ObjectWriter");

	private WriterVisitor modelVisitor;

	public ObjectWriter(WriterVisitor modelVisitor) {
		this.modelVisitor = modelVisitor;
	}

	@Override
	public XMLNode write(Identified object, WriterContext context) {
		if (object == null) {
			return null;
		}

		XMLNode node = new XMLNode(DOMTags.ELEMENT_TAG);

		// Set id
		if (object.getId() == null) {
			object.setId(context.generateNewId());
		} else if (context.containsId(object.getId())) {
			node.setText(object.getId());
			return node;
		}
		object = (Identified) context.process(object, node);
		if (object.getClass() == BasicElement.class) {
			node.setText(object.getId());
			return node;
		}

		node.setAttribute(DOMTags.ID_AT, object.getId());
		ReflectionClass<?> clazz = ReflectionClassLoader
				.getReflectionClass(object.getClass());
		node.setAttribute(DOMTags.CLASS_AT, context.translateClass(clazz
				.getType()));
		while (clazz != null) {
			for (ReflectionField f : clazz.getFields()) {
				// Only store fields annotated with param
				if (f.getAnnotation(Param.class) != null) {
					Object value = f.getFieldValue(object);
					if (value != null) {
						modelVisitor.writeElement(value, object,
								new ObjectWriterListener(context
										.translateField(f.getName()), node));
					}
				}
			}
			clazz = clazz.getSuperclass();
		}

		return node;

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
			if (fieldName.equals("elementToAdd")) {
				logger.debug("h");
			}
			node.setAttribute(DOMTags.FIELD_AT, fieldName);
			parent.append(node);
		}

	}
}

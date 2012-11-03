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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.EAdElement;
import ead.reader.adventure.DOMTags;
import ead.reader.adventure.ObjectFactory;
import ead.reader.adventure.ProxyElement;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;
import ead.tools.xml.XMLNode;

/**
 * Visitor for elements. The element should be 
 * {@code <e [uid="UID"] [p="PARAM_NAME"] id="ID" t="ENGINE_TYPE"> uid | content </e>}.
 */
public class ElementNodeVisitor extends NodeVisitor<EAdElement> {

	protected static final Logger logger = LoggerFactory
			.getLogger("ElementNodeVisitor");

	@Override
	public void visit(XMLNode node, ReflectionField field, Object parent,
			Class<?> listClass, NodeVisitorListener listener ) {
		EAdElement element = null;

		if (node == null) {
			logger.error("Null node: {} parent: {}", field.getName(),
					(parent != null ? parent.getClass().getName() : "NULL"));
		}

		// single elements like <e p="xyz">uid</e> can be looked up and substituted
		if (node.getChildNodes() != null
				&& node.getChildNodes().getLength() == 1
				&& !node.getChildNodes().item(0).hasChildNodes()) {
			element = (EAdElement) ObjectFactory
				.getObject(node.getNodeText(), EAdElement.class);
			if (element != null) {
				if (element instanceof ProxyElement) {				
					((ProxyElement) element).setField(field);
					((ProxyElement) element).setParent(parent);
					logger.debug("Short-circuited read of proxy-element {}({}) into {} of {}", 
							new String[] {
								element.getId(), ((ProxyElement)element).getValue(),
								field != null ? field.getName() : "?", 
								simpleName(parent.getClass())});
				} else {
					setValue(field, parent, element);
					logger.debug("Short-circuited read of element {} into {} of {}", 
							new String[] {
								element.getId(), 
								field != null ? field.getName() : "?", 
								simpleName(parent.getClass())});
				}
				listener.elementRead(element);
				return;
			}
		}

		// 
		if (node.getAttributes() == null) {
			logger.error("Null attributes for element {}", node.getClass());
		}

		String uniqueId = node.getAttributes().getValue(DOMTags.UNIQUE_ID_AT);
		String id = node.getAttributes().getValue(DOMTags.ID_AT);

		String clazz = node.getAttributes().getValue(loaderType);
		if (clazz != null) {
			clazz = translateClass(clazz);
		} else {
			logger.info("Null element for: "
					+ (parent != null ? parent.getClass() : node.getNodeName()));
		}

		if (clazz != null) {
			ReflectionClass<?> classType = ReflectionClassLoader
					.getReflectionClass(clazz);
			logger.debug("Reading element with uid '{}' and id '{}'  of type {}",
					new Object[] {uniqueId, id, clazz});
			if (classType.getConstructor() != null) {
				element = (EAdElement) classType.getConstructor().newInstance();
				element.setId(id);
			}
		} else {
			
		}

		if (element != null) {
			ObjectFactory.addElement(uniqueId, element);
		}
		
		setValue(field, parent, element);

		if (element != null) {
			readFields(element, node);
		}

		listener.elementRead(element);
	}

	@Override
	public String getNodeType() {
		return DOMTags.ELEMENT_AT;
	}

}

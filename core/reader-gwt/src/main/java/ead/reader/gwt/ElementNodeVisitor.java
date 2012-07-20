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

package ead.reader.gwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.xml.client.Node;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import ead.common.model.EAdElement;
import ead.reader.DOMTags;

/**
 * Visitor for the element. The element should be {@code <element id="ID"
 *  type="ENGINE_TYPE"
 *  class="EDITOR_TYPE"></element>}.
 */
public class ElementNodeVisitor extends NodeVisitor<EAdElement> {

	protected static final Logger logger = LoggerFactory
			.getLogger("ElementNodeVisitor");

	@Override
	public EAdElement visit(Node node, Field field, Object parent,
			Class<?> listClass) {
		EAdElement element = null;
		if (node == null)
			logger.error("Null node: {} parent: {}", field.getName(),
					(parent != null ? parent.getClass().getName() : "NULL"));

		if (node.getChildNodes() != null
				&& node.getChildNodes().getLength() == 1
				&& !node.getChildNodes().item(0).hasChildNodes()) {
			element = (EAdElement) ObjectFactory.getObject(
					GWTReader.getNodeText(node), EAdElement.class);
			if (element != null && !(element instanceof ProxyElement)) {
				setValue(field, parent, element);
				return element;
			} else if (element != null) {
				((ProxyElement) element).setField(field);
				((ProxyElement) element).setParent(parent);
				return element;
			}
		}

		if (node.getAttributes() == null) {
			logger.error("Null attributes for element {}", node.getClass());
		}

		Node n = node.getAttributes().getNamedItem(DOMTags.UNIQUE_ID_AT);
		String uniqueId = n != null ? n.getNodeValue() : null;
		n = node.getAttributes().getNamedItem(DOMTags.ID_AT);
		String id = n != null ? n.getNodeValue() : null;

		n = node.getAttributes().getNamedItem(loaderType);
		String clazz = null;
		if (n != null) {
			clazz = n.getNodeValue();
			clazz = translateClass(clazz);
		} else {
			logger.info("Null element for: "
					+ (parent != null ? parent.getClass() : node.getNodeName()));
		}

		if (clazz != null) {
			ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);
			logger.debug("Reading element '{}' of type {}", uniqueId, clazz);
			if (classType.findConstructor() != null) {
				element = (EAdElement)classType.findConstructor().newInstance();
				element.setId(id);
			}
		}

		if (element != null)
			ObjectFactory.addElement(uniqueId, element);

		setValue(field, parent, element);

		try {
			if (element != null)
				readFields(element, node);
		} catch (Exception e) {
			logger.error("Error reading fields from {}", element.getClass());
		}

		return element;
	}

	@Override
	public String getNodeType() {
		return DOMTags.ELEMENT_AT;
	}

}

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

package es.eucm.eadventure.common.impl.reader.visitors;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.impl.reader.ProxyElement;
import es.eucm.eadventure.common.impl.reader.extra.ObjectFactory;
import es.eucm.eadventure.common.model.EAdElement;

/**
 * Visitor for the element. The element should be {@code <element id="ID"
 *  type="ENGINE_TYPE"
 *  class="EDITOR_TYPE"></element>}.
 */
public class ElementNodeVisitor extends NodeVisitor<EAdElement> {
	
	protected static final Logger logger = Logger.getLogger("ElementNodeVisitor");

	@Override
	public EAdElement visit(Node node, Field field, Object parent, Class<?> listClass) {
		EAdElement element = null;
		if (node.getChildNodes().getLength() == 1 && !node.getChildNodes().item(0).hasChildNodes()) {
			element = (EAdElement) ObjectFactory.getObject(node.getTextContent(), EAdElement.class);
			if (element != null && !(element instanceof ProxyElement)) {
				setValue(field, parent, element);
				return element;
			} else if (element != null) {
				((ProxyElement) element).setField(field);
				((ProxyElement) element).setParent(parent);
				return element;
			}
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
			logger.info("Null element for: " + (parent != null ? parent.getClass() : node.getNodeName()));
		}

		Class<?> c = null;
		
		if (clazz != null) {
			try {
				c = ClassLoader.getSystemClassLoader().loadClass(clazz);
				element = (EAdElement) c.newInstance();
				element.setId(id);
			} catch (Exception e1) {
				logger.log(Level.SEVERE, e1.getMessage(), e1);
			}
		}
		
		if (element != null)
			ObjectFactory.addElement(uniqueId, element);

		setValue(field, parent, element);

		if (element != null)
			readFields(element, node);
		
		return element;
	}

	@Override
	public String getNodeType() {
		return DOMTags.ELEMENT_AT;
	}

}

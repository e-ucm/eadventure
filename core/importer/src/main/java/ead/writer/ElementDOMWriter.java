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

package ead.writer;

import org.w3c.dom.Element;

import ead.common.model.EAdElement;
import ead.reader.adventure.DOMTags;

public class ElementDOMWriter extends FieldParamWriter<EAdElement> {

	@Override
	public Element buildNode(EAdElement element, Class<?> listClass) {
		Element node = doc.createElement(DOMTags.ELEMENT_AT);
		try {
			// Check if the element is new
			if (!elementMap.containsKey(element)) {
				elementMap.put(element, DOMTags.ELEMENT_AT
						+ DOMWriter.convertToCode("e", mappedElement.size()));
				mappedElement.add(element);

				if (depthManager.isStored(element)) {
					EAdElement conflicting = depthManager.getInstanceOfElement(element);
					logger.error("Type {} has differing equals and hashcodes ({}_{} != {})",
							new Object[]{element.getClass(), element.equals(conflicting),
								conflicting.hashCode(), element.hashCode()});
				}
			}

			if (depthManager.inPreviousList(element) || depthManager.isStored(element)) {
				// write a reference instead of the node
				node.setTextContent(elementMap.get(element));
				return node;
			}
			depthManager.setStored(element);

			// Set id and unique id
			node.setAttribute(DOMTags.ID_AT, element.getId());
			node.setAttribute(DOMTags.UNIQUE_ID_AT, elementMap.get(element));

			// Look for Element annotation
			Class<?> clazz = element.getClass();
			ead.common.interfaces.Element annotation = null;
			while (clazz != null) {
				annotation = clazz.getAnnotation(ead.common.interfaces.Element.class);
				if (annotation != null) {
					break;
				}
				clazz = clazz.getSuperclass();
			}

			if (annotation != null) {
				node.setAttribute(DOMTags.CLASS_AT, shortClass(clazz.getName()));

				// Add Param fields
				super.processParams(node, element);
			} else {
				logger.error("No Element annotation in class {}",
						element.getClass());
			}

		} catch (Exception e) {
			logger.error("Exception writing element {}", element.getClass(), e);
			error = true;
		}

		return node;
	}
}

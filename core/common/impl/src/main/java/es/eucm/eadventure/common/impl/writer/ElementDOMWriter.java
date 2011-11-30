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

package es.eucm.eadventure.common.impl.writer;

import java.util.logging.Level;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.interfaces.EAdRuntimeException;
import es.eucm.eadventure.common.model.EAdElement;

public class ElementDOMWriter extends FieldParamWriter<EAdElement> {

	@Override
	public Element buildNode(EAdElement element, Class<?> listClass) {
		Element node = doc.createElement(DOMTags.ELEMENT_AT);
		try {
			// Check if the element is new
			if (!elementMap.containsKey(element)) {
				elementMap.put(element, DOMTags.ELEMENT_AT + DOMWriter.convertToCode(mappedElement.size()));
				mappedElement.add(element);
				if (depthManager.isStored(element)) {
					EAdElement conflicting = depthManager.getInstanceOfElement(element);
					logger.severe("Type " + element.getClass() + " has differing equals and hashcodes ("+ element.equals(conflicting) + "_" + conflicting.hashCode() + " != " + element.hashCode() + ")");
				}
			}
			
			if (depthManager.inPreviousList(element) || depthManager.isStored(element)) {
				node.setTextContent(elementMap.get(element));
				return node;
			} 
			depthManager.setStored(element);

			// Set id and unique id
			node.setAttribute(DOMTags.ID_AT, element.getId());
			node.setAttribute(DOMTags.UNIQUE_ID_AT, elementMap.get(element));

			// Look for Element annotation
			Class<?> clazz = element.getClass();
			es.eucm.eadventure.common.interfaces.Element annotation = null;

			while (annotation == null && clazz != null) {
				annotation = clazz
						.getAnnotation(es.eucm.eadventure.common.interfaces.Element.class);
				clazz = clazz.getSuperclass();
			}

			if (annotation == null) {
				logger.log(Level.SEVERE, "No Element annotation in class "
						+ element.getClass());
				throw new EAdRuntimeException("No Element annotation in class "
						+ element.getClass());
			} else {

				node.setAttribute(DOMTags.TYPE_AT, shortClass(annotation.detailed()
						.getName()));
				node.setAttribute(DOMTags.CLASS_AT, shortClass(annotation.runtime()
						.getName()));
			}

			clazz = element.getClass();

			// Add Param fields
			super.processParams(node, element);

		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return node;
	}

}

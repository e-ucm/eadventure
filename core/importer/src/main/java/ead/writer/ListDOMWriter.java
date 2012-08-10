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
import ead.common.model.elements.extra.EAdList;
import ead.common.util.EAdPosition;
import ead.reader.adventure.DOMTags;

public class ListDOMWriter extends DOMWriter<EAdList<?>> {

	@SuppressWarnings("unchecked")
	@Override
	public Element buildNode(EAdList<?> list, Class<?> listClass) {
		Element node = doc.createElement(DOMTags.LIST_TAG);
		node.setAttribute(DOMTags.CLASS_AT, shortClass(list.getValueClass().getName()));

		if (EAdElement.class.isAssignableFrom(list.getValueClass()))
			depthManager.addList((EAdList<Object>) list);
		
		
		if (list.getValueClass() == Integer.class ||
				list.getValueClass() == Float.class ||
				list.getValueClass() == EAdPosition.class || list.getValueClass() == EAdPosition.class) {
			String value = null;
			for (Object o : list) {
				if (o != null) {
					if (value == null){
						value = o.toString();
					}
					else{
						value += "|" + o.toString();
					}
				}
			}
			node.setTextContent(value);
		} else {
			DOMWriter.depthManager.levelUp();
			
			for (int i = 0; i < list.size(); i++) {
				Object o = list.get(i);
				if (o != null) {
					Element newNode = super.initNode(o, list.getValueClass());
					doc.adoptNode(newNode);
					node.appendChild(newNode);
				}
			}
			
			DOMWriter.depthManager.levelDown();
		}
		
		depthManager.removeList((EAdList<Object>) list);

		return node;
	}

}

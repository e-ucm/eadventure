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

import ead.common.model.elements.extra.EAdMap;
import ead.reader.elements.DOMTags;

public class MapDOMWriter extends DOMWriter<EAdMap<?, ?>> {

	@Override
	public Element buildNode(EAdMap<?, ?> map, Class<?> listClass) {
		Element node = doc.createElement(DOMTags.MAP_TAG);

		if (map != null) {

			node.setAttribute(DOMTags.KEY_CLASS_AT, shortClass(map
					.getKeyClass().getName()));
			node.setAttribute(DOMTags.VALUE_CLASS_AT, shortClass(map
					.getValueClass().getName()));

			DOMWriter.depthManager.levelDown();

			for (Object o : map.keySet()) {
				Element key = super.initNode(o, map.getKeyClass());
				doc.adoptNode(key);
				node.appendChild(key);

				Element value = super.initNode(map.get(o), map.getValueClass());
				doc.adoptNode(value);
				node.appendChild(value);
			}

			DOMWriter.depthManager.levelUp();

		}

		return node;
	}

}

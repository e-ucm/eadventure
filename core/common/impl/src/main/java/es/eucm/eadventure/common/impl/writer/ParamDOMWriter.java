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

import org.w3c.dom.Element;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.params.EAdParam;

/**
 * Writer for {@link EAdParam}
 * 
 */
public class ParamDOMWriter extends DOMWriter<Object> {

	@Override
	public Element buildNode(Object data, Class<?> listClass) {
		Element node = doc.createElement(DOMTags.PARAM_AT);
		
		String value = null;
		if (data == null)
			logger.warning("Null data");
		else {
			if (data instanceof EAdParam)
				value = ((EAdParam) data).toStringData();
			else if (data instanceof Class)
				value = ((Class<?>) data).getName();
			else
				value = data.toString();
		}
		
		String compressedValue = paramsMap.get(data);
		if (compressedValue == null) {
			if (DOMWriter.USE_PARAM_IDS) {
				String key = DOMTags.PARAM_AT + DOMWriter.convertToCode(paramsMap.keySet().size());
				if (key.length() < value.length()) {
					paramsMap.put(data, key);
					node.setAttribute(DOMTags.UNIQUE_ID_AT, key);
				}
			}
		} else {
			value = compressedValue;
		}
		
		if ( listClass == null || listClass != data.getClass() )
			node.setAttribute(DOMTags.CLASS_AT, shortClass(data.getClass().getName()));
		node.setTextContent(value);
		return node;
	}

}

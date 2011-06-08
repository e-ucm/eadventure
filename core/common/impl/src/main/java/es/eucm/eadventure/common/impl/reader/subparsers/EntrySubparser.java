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

package es.eucm.eadventure.common.impl.reader.subparsers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.EAdMap;

/**
 * Parser for the entry element. The entry element should be 
 * {@code <entry key="KEY">value</entry>}.
 */
public class EntrySubparser extends Subparser {

	/**
	 * Current string being parsed 
	 */
	private StringBuffer currentString;
	
	/**
	 * Key of the param
	 */
	private Object key;
	
	/**
	 * The object where to set the param
	 */
	@SuppressWarnings("rawtypes")
	private EAdMap map;
	
	@SuppressWarnings("rawtypes")
	private EAdElementList list;
	
	/**
	 * The element possibly used as the value for the param
	 */
	private EAdElement element;
	
	private static final Logger logger = LoggerFactory.getLogger("ParamSubparser");
	
	public EntrySubparser(EAdMap<?,?> object, Attributes attributes) {
		currentString = new StringBuffer();
		this.key = ObjectFactory.getObject(attributes.getValue("key"), object.getKeyClass());
		this.map = object;
	}

	public EntrySubparser(EAdElementList<?> peek, Attributes attributes) {
		currentString = new StringBuffer();
		this.list = peek;
	}

	@Override
    public void characters( char[] buf, int offset, int len ) {
        currentString.append( new String( buf, offset, len ) );
    }

	@SuppressWarnings("unchecked")
	@Override
	public void endElement() {
		String value = currentString.toString().trim();
		try {
			if (map != null) {
				if (element != null)
					map.put(key, element);
				else
					map.put(key, ObjectFactory.getObject(value, map.getValueClass()));
			} else if (list != null) {
				if (element != null)
					list.add(element);
				else
					list.add(ObjectFactory.getObject(value, list.getValueClass()));
			}
		} catch (NullPointerException e) {
			logger.error(e.getMessage(), e);
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} 
	}

	/**
	 * Sets the element that is the value of the param
	 * 
	 * @param element
	 */
	@Override
	public void addChild(Object element) {
		if (element instanceof EAdElement)
			this.element = (EAdElement) element;
		else
			logger.error("Tried to add wrong type of element " + element);
	}
}

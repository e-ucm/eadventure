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

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.EAdMap;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;

/**
 * Subparser for the list element.
 */
public class ListSubparser extends Subparser {

	private static final Logger logger = LoggerFactory
			.getLogger("ListSubparser");

	/**
	 * The list of elements.
	 */
	protected EAdElementList<Object> elementList;

	public ListSubparser(EAdElement peek, Attributes attributes) {
		init(peek, attributes);
	}
	
	@SuppressWarnings("unchecked")
	protected void init(EAdElement peek, Attributes attributes) {
		String name = attributes.getValue("name");
		if (peek instanceof EAdMap) {
			elementList = new EAdElementListImpl<Object>();
		} else { 
			Field field = getField(peek, name);
			try {
				field.setAccessible(true);
				elementList = (EAdElementList<Object>) field.get(peek);
				field.setAccessible(false);
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.impl.reader.subparsers.Subparser#endElement()
	 */
	@Override
	public void endElement() {
		// DO NOTHING
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.impl.reader.subparsers.Subparser#addElement
	 * (es.eucm.eadventure.common.model.EAdElement)
	 */
	@Override
	public void addChild(Object element) {
		elementList.add(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.impl.reader.subparsers.Subparser#characters
	 * (char[], int, int)
	 */
	@Override
	public void characters(char[] buf, int offset, int len) {
		// DO NOTHING
	}

	public Object getList() {
		return elementList;
	}

}

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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.model.extra.EAdList;

/**
 * Parser for the param element. The param element should be
 * {@code <param name="RESOURCE_NAME">value</param>}.
 */
public class ObjectSubparser extends Subparser {

	/**
	 * Current string being parsed
	 */
	private StringBuffer currentString;

	/**
	 * Name of the param
	 */
	private String paramValue;

	/**
	 * The object where to set the param
	 */
	private Object parent;

	private Object element;

	private static final Logger logger = Logger.getLogger("ParamSubparser");

	public ObjectSubparser(Object object, Attributes attributes) {
		currentString = new StringBuffer();
		this.paramValue = attributes.getValue("param");
		this.parent = object;
	}

	@Override
	public void characters(char[] buf, int offset, int len) {
		currentString.append(new String(buf, offset, len));
	}

	@Override
	public void endElement() {

		String value = currentString.toString().trim();
		if (paramValue != null)
			try {
				Field field = getField(parent, paramValue);

				if (field != null) {
					boolean accessible = field.isAccessible();
					field.setAccessible(true);
					this.element = ObjectFactory.getObject(value,
							field.getType());
					field.set(parent, element);
					field.setAccessible(accessible);
				}

			} catch (NullPointerException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} catch (SecurityException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		else {
			if ( parent instanceof EAdList ){
				this.element = ObjectFactory.getObject(value, ((EAdList<?>) parent).getValueClass());
			}
		}
	}

	/**
	 * Sets the element that is the value of the param
	 * 
	 * @param element
	 */
	@Override
	public void addChild(Object element) {

	}

	@Override
	public Object getObject() {
		return element;
	}
}

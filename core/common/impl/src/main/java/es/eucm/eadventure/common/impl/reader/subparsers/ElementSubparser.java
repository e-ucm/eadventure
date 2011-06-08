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

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.model.EAdElement;

/**
 * Parser for the element. The element should be 
 * {@code <element id="ID"
 *  type="ENGINE_TYPE"
 *  class="EDITOR_TYPE"></element>}.
 */
public class ElementSubparser extends Subparser {

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger("ElementSubparser");
	
	/**
	 * Element
	 */
	private EAdElement element;
	
	public ElementSubparser(EAdElement element, Attributes attributes, String classParam) {
		String className = attributes.getValue(classParam);
		String id = attributes.getValue("id");
		String uniqueId = attributes.getValue("uniqueId");
		if (ObjectFactory.getObject(uniqueId, EAdElement.class) == null ) {
			try {
				Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(className);
				Constructor<?> con = clazz.getConstructor(EAdElement.class, String.class);
				this.element = (EAdElement) con.newInstance(new Object[]{element, id});
				ObjectFactory.put(uniqueId, this.element);
			} catch (NullPointerException e) {
				logger.error("Null pointer, className:" + className + "; uniqueId:" + uniqueId, e);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			try {
				this.element = (EAdElement) ObjectFactory.getObject(uniqueId, EAdElement.class);
			} catch (ClassCastException e) {
				logger.error("Class cast excpetion, uniqueId:" + uniqueId, e);
			}
		}
	}

	@Override
	public void endElement() {
	}
	
	@Override
	public void addChild(Object element) {
		if (element instanceof EAdElement)
			this.element = (EAdElement) element;
		else
			logger.error("Tried to add element of wrong type " + element);
	}

	public EAdElement getElement() {
		return element;
	}

	@Override
	public void characters(char[] buf, int offset, int len) {
	}

}

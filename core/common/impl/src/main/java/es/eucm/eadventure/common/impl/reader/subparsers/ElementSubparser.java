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

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.impl.writer.DOMWriter;
import es.eucm.eadventure.common.model.EAdElement;

/**
 * Parser for the element. The element should be {@code <element id="ID"
 *  type="ENGINE_TYPE"
 *  class="EDITOR_TYPE"></element>}.
 */
public class ElementSubparser extends Subparser<EAdElement> {

	public ElementSubparser(Object o, Attributes attributes) {
		super(o, attributes, EAdElement.class);
		// If element is new
		if ( attributes.getIndex(DOMWriter.ID_AT) != -1) {
			String id = attributes.getValue(DOMWriter.ID_AT);
			String uniqueId = attributes.getValue(DOMWriter.UNIQUE_ID_AT);

			Class<?> c = null;
			try {
				c = ClassLoader.getSystemClassLoader().loadClass(clazz);
				Constructor<?> con = c.getConstructor(String.class);
				element = (EAdElement) con
						.newInstance(new Object[] { id });

				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				Constructor<?> con;
				try {
					con = c.getConstructor();
					this.element = (EAdElement) con.newInstance();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {

			}
			if ( element != null )
				ObjectFactory.addElement(uniqueId, element);
		}

	}

}

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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.model.EAdElement;

/**
 * Parser for the element. The element should be {@code <element id="ID"
 *  type="ENGINE_TYPE"
 *  class="EDITOR_TYPE"></element>}.
 */
public class ElementSubparser extends Subparser {

	private static Map<String, EAdElement> elementMap = new HashMap<String, EAdElement>();

	private StringBuffer string;

	/**
	 * Element
	 */
	private EAdElement element;
	
	private String paramValue;
	
	private Object parent;

	public ElementSubparser(Object o, Attributes attributes, String classParam) {
		parent = o;
		paramValue = attributes.getValue("param");
		// If element is new
		if (attributes.getIndex("id") != -1) {
			String className = translateClass(attributes.getValue(classParam));
			String id = attributes.getValue("id");
			String uniqueId = attributes.getValue("uniqueId");

			Class<?> clazz = null;
			try {
				clazz = ClassLoader.getSystemClassLoader().loadClass(className);
				Constructor<?> con = clazz.getConstructor(String.class);
				this.element = (EAdElement) con
						.newInstance(new Object[] { id });

				elementMap.put(uniqueId, element);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				Constructor<?> con;
				try {
					con = clazz.getConstructor();
					this.element = (EAdElement) con.newInstance();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {

			}

			// If element is repeated
		} else {
			string = new StringBuffer();
		}

	}

	@Override
	public void endElement() {
		if (string != null) {
			element = elementMap.get(string.toString());
		}
		
		if ( paramValue != null && parent != null ){
			try {
				Field field = getField(parent, paramValue);

				if (field != null) {
					boolean accessible = field.isAccessible();
					field.setAccessible(true);
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
		}
	}

	@Override
	public void addChild(Object element) {

	}

	public Object getObject() {
		return element;
	}

	@Override
	public void characters(char[] buf, int offset, int len) {
		string.append(buf, offset, len);
	}

}

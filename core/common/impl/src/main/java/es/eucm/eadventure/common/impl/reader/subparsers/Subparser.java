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
import java.util.logging.Logger;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.interfaces.Param;

/**
 * Abstract subparser for the eAdventure model
 */
public abstract class Subparser<T> {

	protected static final Logger logger = Logger.getLogger("Subparser");

	private static String packageName;
	
	private static String loaderType;

	public static void init(String packageN) {
		packageName = packageN;
		loaderType = DOMTags.CLASS_AT;
		//loaderType = DOMTags.TYPE_AT;
	}

	protected String clazz;

	protected String param;

	protected StringBuffer stringBuffer;

	protected Object parent;

	protected Field field;

	/**
	 * Parsed element
	 */
	protected T element;

	protected Class<T> defaultClass;

	@SuppressWarnings("unchecked")
	public Subparser(Object parent, Attributes attributes, Class<T> defaultClass) {
		this.parent = parent;
		this.defaultClass = defaultClass;
		clazz = translateClass(attributes.getValue(loaderType));
		if (clazz == null)
			clazz = translateClass(attributes.getValue(DOMTags.CLASS_AT));
		param = attributes.getValue(DOMTags.PARAM_AT);
		if (param != null) {
			field = getField(parent, param);
			if (field != null) {
				boolean accesible = field.isAccessible();
				field.setAccessible(true);
				try {
					element = (T) field.get(parent);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				field.setAccessible(accesible);
			}
		}
	}

	/**
	 * Gets the {@link Field} object identified by the given id. It gives
	 * precedence to Fields annotated with the id though the {@link Param}
	 * annotation, if non is found it checks if the id coincides with the name
	 * of a field.
	 * 
	 * @param object
	 *            The object for where the field should be
	 * @param paramValue
	 *            The id of the field
	 * @return The field corresponding to the given id
	 */
	public Field getField(Object object, String paramValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			for (Field f : clazz.getDeclaredFields()) {
				Param a = f.getAnnotation(Param.class);
				if (a != null && a.value().equals(paramValue))
					return f;
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	public void characters(char[] buf, int offset, int len) {
		if (stringBuffer == null) {
			stringBuffer = new StringBuffer();
		}
		stringBuffer.append(buf, offset, len);
	}

	/**
	 * End the subparsing of the element
	 */
	@SuppressWarnings("unchecked")
	public void endElement() {

		try {
			if (stringBuffer != null) {
				String value = stringBuffer.toString().trim();
				Class<?> c = defaultClass;
				if (clazz != null) {
					c = ClassLoader.getSystemClassLoader().loadClass(clazz);
					
				} else if (param != null) {
					c = getField(parent, param).getType();
				}
				element = (T) ObjectFactory.getObject(value, c);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			if (element != null && field != null) {
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.set(parent, element);
				field.setAccessible(accessible);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	public void addChild(Object element) {

	}

	/**
	 * Returns the object parsed by this parser
	 * 
	 * @return
	 */
	public T getObject() {
		return element;
	}

	public String translateClass(String clazz) {
		return clazz != null && clazz.startsWith(".") ? packageName + clazz : clazz;
	}

}

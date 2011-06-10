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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.eucm.eadventure.common.interfaces.Param;

/**
 * Abstract subparser for the eAdventure model
 */
public abstract class Subparser {

	private static final Logger logger = Logger.getLogger("Subparser");
	
	/**
	 * End the subparsing of the element
	 */
	public abstract void endElement();

	/**
	 * Gets the {@link Field} object identified by the given id. It gives
	 * precedence to Fields annotated with the id though the {@link Param}
	 * annotation, if non is found it checks if the id coincides with
	 * the name of a field.
	 * 
	 * @param object The object for where the field should be
	 * @param id The id of the field
	 * @return The field corresponding to the given id
	 */
	public Field getField(Object object, String id) {
		Field field = null;
		Field[] fields = object.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length && field == null; i++) {
			Field f = fields[i];
			Annotation a = f.getAnnotation(Param.class);
			if (a != null && ((Param) a).value().equals(id))
				field = f;
		}

		Class<?> clazz = object.getClass();
		while (field == null && clazz != Object.class) {
			try {
				field = clazz.getDeclaredField(id);
			} catch (NoSuchFieldException e) {
				try {
					clazz = clazz.getSuperclass();
				} catch (SecurityException e1) {
					logger.log(Level.SEVERE, "Security excpetion " + object + " " + id, e);
				} 
			} catch (NullPointerException e) {
				logger.log(Level.SEVERE, "Null pointer " + object + " " + id, e);
			}
		}
		if (field == null) 
			logger.log(Level.SEVERE, "No such field " + object + " " + id);
		
		return field;
	}

	public abstract void characters(char[] buf, int offset, int len);

	public abstract void addChild(Object element);

}

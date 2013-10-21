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

package es.eucm.ead.model.elements;

import com.gwtent.reflection.client.Reflectable;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;

/**
 * Implementation of a basic element. Most of the model elements
 * inherits from this basis class.
 * 
 * They can also be used as reference of other elements
 */
@Element
@Reflectable(relationTypes = true)
public class BasicElement implements Identified {

	private String id;

	@Param
	private EAdMap<Object> properties;

	/**
	 * Creates a reference to an element
	 * 
	 * @param reference
	 */
	public BasicElement(String reference) {
		this.id = reference;
	}

	public BasicElement() {

	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	/**
	 *
	 * @param propertyId
	 * @param defaultValue the default value in case the property is not defined for the {@link EAdElement}
	 * @param <T>
	 * @return
	 */
	public <T> T getProperty(String propertyId, T defaultValue) {
		return (T) (properties == null ? defaultValue : properties
				.containsKey(propertyId) ? properties.get(propertyId)
				: defaultValue);
	}

	/**
	 * Puts a property
	 * @param propertyId the parameter identifier
	 * @param value   the parameter value
	 */
	public void putProperty(String propertyId, Object value) {
		if (properties == null) {
			properties = new EAdMap<Object>();
		}
		properties.put(propertyId, value);
	}

	public String toString() {
		return classToString(this.getClass()) + (id != null ? id : "");
	}

	/**
	 * GWT does not recognize clazz.getSimpleName(). This helper method should
	 * be used instead, and can be handy for debugging purposes
	 * Default-generated ids use this, for instance.
	 * 
	 * @param c
	 * @return
	 */
	public static String classToString(Class<?> c) {
		String n = c.getName();
		return n.substring(n.lastIndexOf('.') + 1);
	}

	// Required for GWT

	public EAdMap<Object> getProperties() {
		return properties;
	}

	public void setProperties(EAdMap<Object> properties) {
		this.properties = properties;
	}
}

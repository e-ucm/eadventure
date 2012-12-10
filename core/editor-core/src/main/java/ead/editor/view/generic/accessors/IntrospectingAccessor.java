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

package ead.editor.view.generic.accessors;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Generic implementation of {@link Accessor}
 *
 * @param <S>
 */
public class IntrospectingAccessor<S> implements Accessor<S> {

	/**
	 * fieldName to introspect getters and setters for
	 */
	protected String fieldName;

	/**
	 * element where the getters and setters live
	 */
	protected Object element;

	/**
	 * @param element The element where the value is stored
	 * @param fieldName The name of the field
	 */
	public IntrospectingAccessor(Object element, String fieldName) {
		this.element = element;
		this.fieldName = fieldName;
	}

	/**
	 * Returns a PropertyDescriptor to get/set the property
	 *
	 * @param c
	 * @param fieldName
	 * @return
	 */
	private PropertyDescriptor getPropertyDescriptor() {
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(
					element.getClass()).getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					return pd;
				}
			}
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(
					"Could not find getters or setters for field " + fieldName
							+ " in class "
							+ element.getClass().getCanonicalName());
		}
		return null;
	}

	/**
	 * Writes the field
	 */
	@Override
	public void write(S data) {
		try {
			Method method = getPropertyDescriptor().getWriteMethod();
			method.invoke(element, data);
		} catch (Exception e) {
			throw new RuntimeException("Error writing field " + fieldName, e);
		}
	}

	/**
	 * Reads the field
	 */
	@Override
	@SuppressWarnings("unchecked")
	public S read() {
		try {
			Method method = getPropertyDescriptor().getReadMethod();
			return (S) method.invoke(element);
		} catch (Exception e) {
			throw new RuntimeException("Error reading field " + fieldName, e);
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash
				+ (this.fieldName != null ? this.fieldName.hashCode() : 0);
		hash = 53 * hash + (this.element != null ? this.element.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final IntrospectingAccessor<S> other = (IntrospectingAccessor<S>) obj;
		if ((this.fieldName == null) ? (other.fieldName != null)
				: !this.fieldName.equals(other.fieldName)) {
			return false;
		}
		if (this.element != other.element
				&& (this.element == null || !this.element.equals(other.element))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "IntroFD{" + element.getClass().getSimpleName() + "@"
				+ element.hashCode() + "::" + element + '}';
	}
}

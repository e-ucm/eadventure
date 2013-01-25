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

package ead.common.model.elements;

/**
 * Implementation of a basic {@link EAdElement}. Most of the model elements
 * inherits from this basis class
 * 
 * 
 */
public abstract class BasicElement implements EAdElement {

	private String id;

	private static int lastId = 0;

	public static void initLastId() {
		lastId = 0;
	}

	public static String randomSuffix() {
		return "" + lastId++;
	}

	public BasicElement() {
		this.id = classToString(this.getClass()) + randomSuffix();
	}

	@Override
	public String getId() {
		if (id == null) {
			throw new IllegalStateException(
					"EAdElement with no id - Broken contract!");
		}
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return id;
	}

	public boolean equals(Object o) {
		return o != null && getClass().equals(o.getClass())
				&& getId().equals(((EAdElement) o).getId());
	}

	public int hashCode() {
		return getId().hashCode() * 43 ^ getClass().hashCode();
	}

	/**
	 * GWT does not recognize clazz.getSimpleName(). This helper method should
	 * be used instead, and can be handy for debugging purposes
	 * Default-generated ids use this, for instance.
	 * 
	 * @param o
	 * @return
	 */
	public static String classToString(Class<?> c) {
		String n = c.getName();
		return n.substring(n.lastIndexOf('.') + 1);
	}
}

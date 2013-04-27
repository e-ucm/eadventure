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

import ead.common.interfaces.Element;

/**
 * Implementation of a basic {@link EAdElement}. Most of the model elements
 * inherits from this basis class.
 * 
 * They can also be used as reference of other elements
 */
@Element
public class BasicElement implements EAdElement {

	public static final char[] ID_CHARS = new char[] { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', /*
										 * '.', '_', '\'', '?', '¿', '¡', '!', 'ñ',
										 * 'Ñ', 'ç', '+', '-', ' ', '@', '^', '#',
										 * '$', '%', '(', ')',';', ',', '{', '}',
										 * '*', '·', '[', ']', '`', '´'
										 */};
	// XXX Some kind of error happen with the other characters

	private String id;

	public static int lastId = 0;

	public static String idPrefix = "";

	public static void initLastId() {
		lastId = 0;
	}

	public static String randomSuffix() {
		String id = idPrefix;
		int id2 = lastId;
		boolean oneZero = false;
		while (!oneZero) {
			id = ID_CHARS[(id2 % ID_CHARS.length)] + id;
			id2 /= ID_CHARS.length;
			if (id2 == 0) {
				oneZero = true;
			}
		}
		lastId++;
		return id;
	}

	/**
	 * Creates a reference to an element
	 * 
	 * @param reference
	 */
	public BasicElement(String reference) {
		this.id = reference;
	}

	public BasicElement() {
		this.id = randomSuffix();
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
		return classToString(this.getClass()) + id;
	}

	public boolean equals(Object o) {
		return (o instanceof EAdElement && ((EAdElement) o).getId().equals(
				getId()));
	}

	public int hashCode() {
		return getId().hashCode();
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

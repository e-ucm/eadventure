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

package ead.common.params.text;

import ead.common.model.elements.BasicElement;
import ead.common.params.EAdParam;

/**
 * General internationalized string asset interface.
 */
public class EAdString implements EAdParam {

	/**
	 * The id
	 */
	private String id;

	public static final String LITERAL_PREFIX = "#txt#";

	/**
	 * Construct a new string with the given id
	 * 
	 * @param id
	 *            The id of the EAdString
	 */
	public EAdString(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EAdString) {
			return ((EAdString) o).id.equals(id);
		}
		return false;
	}

	@Override
	public String toStringData() {
		return id;
	}

	@Override
	public boolean parse(String data) {
		this.id = data;
		return id != null;
	}

	public static EAdString newRandomEAdString(String string) {
		return new EAdString(string + BasicElement.randomSuffix());
	}

	public static EAdString newEAdString(String string) {
		return new EAdString(string);
	}

	/**
	 * Returns a string that won't be internationalized
	 * 
	 * @param text
	 *            the text to be shown
	 * @return
	 */
	public static EAdString newLiteralString(String text) {
		return new EAdString(LITERAL_PREFIX + text);
	}

}

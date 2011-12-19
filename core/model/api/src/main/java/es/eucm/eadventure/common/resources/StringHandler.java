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

package es.eucm.eadventure.common.resources;

import java.util.Map;

import es.eucm.eadventure.common.params.text.EAdString;


/**
 * A handler to translate {@link EAdString} to readable text in a given language
 * 
 * 
 */
public interface StringHandler {
	
	/**
	 * Strings id starting with this prefix, will return the id without the prefix
	 */
	public static String TEXTUAL_STRING_PREFIX = "#txt#";

	/**
	 * Get the text associated to an {@link EAdString} in the configured
	 * language
	 * 
	 * @param string
	 *            the {@link EAdString}
	 * @return the readable text
	 */
	String getString(EAdString string);

	/**
	 * Assigns the given {@link EAdString} with the given human readable string
	 * 
	 * @param eAdString
	 *            the {@link EAdstring} representing the text
	 * @param string
	 *            the human-readable string
	 */
	void setString(EAdString eAdString, String string);

	/**
	 * Sets the {@link EAdString} dictionary, deleting current entries
	 * 
	 * @param strings
	 *            the new dictionary
	 */
	void setStrings(Map<EAdString, String> strings);

	/**
	 * Adds the given dictionary to the current entries
	 * 
	 * @param strings
	 *            the translation to be added
	 */
	void addStrings(Map<EAdString, String> strings);

	/**
	 * Returns the current dictionary
	 * 
	 * @return
	 */
	Map<EAdString, String> getStrings();

}

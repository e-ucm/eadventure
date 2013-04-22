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

package ead.converter;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import ead.common.model.params.text.EAdString;

@Singleton
public class StringsConverter {

	private static final String PREFIX = "converter.string";

	private Map<EAdString, String> strings;
	private Map<String, EAdString> reverse;

	public StringsConverter() {
		strings = new HashMap<EAdString, String>();
		reverse = new HashMap<String, EAdString>();
	}

	/**
	 * Converts a string to an EAdString
	 * 
	 * @param text
	 * @return
	 */
	public EAdString convert(String text) {
		EAdString string = reverse.get(text);
		if (string == null) {
			string = new EAdString(PREFIX + strings.size());
			strings.put(string, text);
			reverse.put(text, string);
		}
		return string;
	}

	/**
	 * Returns the strings contained by this converter
	 * 
	 * @return
	 */
	public Map<EAdString, String> getStrings() {
		return strings;
	}

}

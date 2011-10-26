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

package es.eucm.eadventure.common.elementfactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.EAdString;

/**
 * A factory providing {@link EAdString}s for testing
 * 
 */
@Singleton
public class StringFactory {

	public enum StringType {
		VERY_LONG_STRING, LONG_STRING, MEDIUM_STRING, SHORT_STRING;

		/**
		 * Returns the string associated to this {@link StringType}
		 * 
		 * @return
		 */
		public String getString() {
			switch (this) {
			case VERY_LONG_STRING:
				String s = "";
				for ( int i = 0; i < 5; i++ ){
					s += LONG_STRING.getString() + MEDIUM_STRING.getString() + SHORT_STRING.getString();
				}
				return s;
			case LONG_STRING:
				return "This is a very looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong string, to test show text effects and other eventso or effectes related to text. Just to make sure that evvvvvveryyyyyyyythinnnngggggg is OKKKKKKK!!!!!!!!!!!!!";
			case MEDIUM_STRING:
				return "Hi there! It's everythin OK or what?";
			case SHORT_STRING:
				return "OK.";
			default:
				return "The default string. Never mind.";

			}
		}
	}

	private static ArrayList<EAdString> strings;

	private Map<EAdString, String> userStrings;

	public StringFactory() {
		userStrings = new HashMap<EAdString, String>();
		if (strings == null) {
			strings = new ArrayList<EAdString>();
			for (StringType type : StringType.values()) {
				EAdString string = EAdString.newEAdString(type.toString());
				strings.add(string);
			}
		}
	}

	public Map<EAdString, String> getStrings() {
		Map<EAdString, String> shstrings = new HashMap<EAdString, String>();
		int i = 0;
		for (EAdString string : strings) {
			shstrings.put(string,
					StringType.values()[i++].getString());
		}

		shstrings.putAll(userStrings);
		return shstrings;
	}

	/**
	 * Returns the {@EAdString} associated to given string type
	 * 
	 * @param stringType
	 *            the string type
	 * @return the {@EAdString} associated to given string type
	 */
	public EAdString getString(StringType stringType) {
		return strings.get(stringType.ordinal());
	}

	public EAdString getString(String string) {
		EAdString eAdString = EAdString.newEAdString(string);
		setString(eAdString, string);
		return eAdString;
	}
	
	public void setString(EAdString string, String value) {
		userStrings.put(string, value);
	}
}

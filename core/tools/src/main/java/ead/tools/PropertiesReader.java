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

package ead.tools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to read properties files in the following form
 * 
 * <pre>
 * [section1]
 * key1=value1
 * key2=value2
 * key3=value3
 * key4=value4
 * [section1]
 * key5=value5
 * ...
 * </pre>
 * 
 * 
 * 
 */
public class PropertiesReader {

	private static final Logger logger = LoggerFactory
			.getLogger("PropertiesReader");

	/**
	 * Parse a string with properties
	 * 
	 * @param propertiesIdentifier
	 *            an identifier to be used in possible logger messages
	 * @param properties
	 *            string containing the properties
	 * @param handler
	 *            a handler to process properties found
	 */
	public static Map<String, Map<String, String>> parse(
			String propertiesIdentifier, String properties) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		if (properties == null) {
			return map;
		}

		String section = null;
		String lines[] = properties.split("\n");
		for (String l : lines) {
			int start = l.indexOf('[');
			int end = l.indexOf(']');
			if (start != -1 && end != -1 && start < end) {
				section = l.substring(start + 1, end);
			} else {
				String pair[] = l.split("=");
				if (pair.length != 2) {
					logger.warn("Error reading {} while processing {}",
							new Object[] { l, propertiesIdentifier });
				} else {
					if (section == null) {
						logger
								.warn("Found property out with no section. This is not an error, but it is recommended to use sections in properties files.");
					}
					Map<String, String> m = map.get(section);
					if (m == null) {
						m = new HashMap<String, String>();
						map.put(section, m);
					}
					m.put(pair[0], pair[1]);
				}

			}
		}
		return map;
	}

}

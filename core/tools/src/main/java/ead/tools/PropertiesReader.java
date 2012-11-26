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
		
		if ( properties == null ){
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
					logger.warn(
							"Error reading {} while processing {}",
							new Object[] { l, propertiesIdentifier });
				} else {
					if (section == null) {
						logger.warn("Found property out with no section. This is not an error, but it is recommended to use sections in properties files.");
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

package es.eucm.eadventure.common;

import es.eucm.eadventure.common.resources.EAdString;

/**
 * A handler to translate {@link EAdString} to readable text in a given language
 * 
 * 
 */
public interface StringsReader {

	/**
	 * Get the text associated to an {@link EAdString} in the configured
	 * language
	 * 
	 * @param string
	 *            the {@link EAdString}
	 * @return the readable text
	 */
	String getString(EAdString string);

}

package es.eucm.eadventure.common;

import java.util.Map;

import es.eucm.eadventure.common.resources.EAdString;

/**
 * A handler to translate {@link EAdString} to readable text in a given language
 * 
 * 
 */
public interface StringHandler {

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
	 * Adds a new string value to the string handler and returns a new
	 * {@link EAdString} associated with it
	 * 
	 * @param string
	 *            the string
	 * @return a new {@link EAdString}
	 */
	EAdString addString(String string);

	/**
	 * Returns the current dictionary
	 * 
	 * @return
	 */
	Map<EAdString, String> getStrings();

}

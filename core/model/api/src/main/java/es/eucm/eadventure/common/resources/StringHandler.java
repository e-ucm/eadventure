package es.eucm.eadventure.common.resources;

import java.util.Map;

import es.eucm.eadventure.common.params.EAdString;


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

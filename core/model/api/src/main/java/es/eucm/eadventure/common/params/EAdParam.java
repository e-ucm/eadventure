package es.eucm.eadventure.common.params;

/**
 * An eAdventure parameter is data that it's used by the {@link EAdElement}s. A
 * parameter is able to store itself in a string, and is able to reconstruct it
 * from it
 * 
 */
public interface EAdParam {

	/**
	 * Return a string representing the parameter
	 * 
	 * @return
	 */
	String toStringData();

	/**
	 * Parses a string created with {@link EAdParam#toStringData()} and sets the
	 * parameter to the proper values
	 * 
	 * @param data
	 *            the string to be parsed
	 */
	void parse(String data);

}

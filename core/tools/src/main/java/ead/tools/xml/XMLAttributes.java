package ead.tools.xml;

/**
 * General interface for XML attributes
 * 
 */
public interface XMLAttributes {

	/**
	 * Returns the value for a given attribute
	 * 
	 * @param atttributeName
	 *            the attribute name
	 * @return
	 */
	String getValue(String atttributeName);
}

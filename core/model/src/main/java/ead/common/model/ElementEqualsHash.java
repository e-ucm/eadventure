package ead.common.model;

/**
 * Interface to generate hash codes and implements equals for {@EAdElement
 * 
 * }. An object implementing this interface must be set through
 * {@BasicElement#}
 * 
 */
public interface ElementEqualsHash {

	/**
	 * Returns if the two given elements represent the same element
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	boolean equals(EAdElement e1, EAdElement e2);

	/**
	 * Returns a hash code for the given element
	 * 
	 * @param e
	 *            the element
	 * @return
	 */
	int hashCode(EAdElement e);

}

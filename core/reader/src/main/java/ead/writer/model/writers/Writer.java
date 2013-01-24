package ead.writer.model.writers;

import ead.tools.xml.XMLNode;

/**
 * Basic class for writing any type of element in eAdventure
 *
 * @param <T>
 */
public interface Writer<T> {
	/**
	 * Writes the element in a node
	 * @param object
	 * @return
	 */
	XMLNode write(T object);
}

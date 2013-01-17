package ead.reader.elements.readers;

import ead.tools.xml.XMLNode;

/**
 * Basic class for reading any type of element in eAdventure
 *
 * @param <T>
 */
public interface ElementReader<T> {

	/**
	 * Reads the element contained by the node
	 * @param node
	 * @return
	 */
	T read(XMLNode node);

}

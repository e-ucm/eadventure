package ead.tools.xml;

/**
 * General interface for a list of nodes
 * 
 */
public interface XMLNodeList {

	/**
	 * Returns the node at the given index
	 * 
	 * @param index
	 * @return
	 */
	XMLNode item(int index);

	/**
	 * 
	 * @return the length of the list
	 */
	int getLength();

}

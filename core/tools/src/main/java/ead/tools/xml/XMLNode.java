package ead.tools.xml;

/**
 * General interface for XML nodes
 * 
 */
public interface XMLNode {

	/**
	 * 
	 * @return the node's attributes
	 */
	XMLAttributes getAttributes();

	/**
	 * @return the text contained by this node
	 */
	String getNodeText();

	/**
	 * 
	 * @return a list childs nodes from this node
	 */
	XMLNodeList getChildNodes();

	/**
	 * @return the node name
	 */
	String getNodeName();

	/**
	 * 
	 * @return whether this node has child nodes or not
	 */
	boolean hasChildNodes();

	/**
	 * Returns the first child node
	 * @return
	 */
	XMLNode getFirstChild();

}

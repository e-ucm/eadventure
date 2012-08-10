package ead.tools.xml;

/**
 * General interface for XML parsers
 * 
 * @author anserran
 * 
 */
public interface XMLParser {

	/**
	 * Parses xml and returns a document
	 * 
	 * @param xml
	 * @return
	 */
	XMLDocument parse(String xml);

}

package ead.tools.java.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import ead.tools.xml.XMLAttributes;

public class JavaXMLAttributes implements XMLAttributes {

	private NamedNodeMap map;

	public JavaXMLAttributes(NamedNodeMap map) {
		this.map = map;
	}

	@Override
	public String getValue(String atttributeName) {
		Node n = map.getNamedItem(atttributeName);
		return n == null ? null : n.getNodeValue();
	}

}

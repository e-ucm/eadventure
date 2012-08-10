package ead.tools.gwt.xml;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;

import ead.tools.xml.XMLAttributes;

public class GwtXMLAttributes implements XMLAttributes {

	private NamedNodeMap map;

	public GwtXMLAttributes(NamedNodeMap map) {
		this.map = map;
	}

	@Override
	public String getValue(String atttributeName) {
		Node n = map.getNamedItem(atttributeName);
		return n == null ? null : n.getNodeValue();
	}

}

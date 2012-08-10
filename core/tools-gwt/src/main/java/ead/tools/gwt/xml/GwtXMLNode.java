package ead.tools.gwt.xml;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import ead.tools.xml.XMLAttributes;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

public class GwtXMLNode implements XMLNode {

	private Node node;

	private XMLAttributes attributes;

	private XMLNodeList childNodes;

	public GwtXMLNode(Node node) {
		this.node = node;
		attributes = new GwtXMLAttributes(node.getAttributes());
		childNodes = new GwtXMLNodeList(node.getChildNodes());
	}

	@Override
	public XMLAttributes getAttributes() {
		return attributes;
	}

	@Override
	public String getNodeText() {
		if (node == null)
			return "";
		NodeList nodes = node.getChildNodes();
		String result = "";
		try {
			for (int i = 0; i < nodes.getLength(); i++) {
				String value = nodes.item(i).getNodeValue();
				if (value != null)
					result += (value.equals("null") ? "" : value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public XMLNodeList getChildNodes() {
		return childNodes;
	}

	@Override
	public String getNodeName() {
		return node.getNodeName();
	}

	@Override
	public boolean hasChildNodes() {
		return node.hasChildNodes();
	}

	@Override
	public XMLNode getFirstChild() {
		if (hasChildNodes()) {
			return getChildNodes().item(0);
		} else {
			return null;
		}

	}

}

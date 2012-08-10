package ead.tools.java.xml;

import org.w3c.dom.Node;

import ead.tools.xml.XMLAttributes;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

public class JavaXMLNode implements XMLNode {

	private Node node;

	private XMLAttributes attributes;

	private XMLNodeList childNodes;

	public JavaXMLNode(Node node) {
		this.node = node;
		attributes = new JavaXMLAttributes(node.getAttributes());
		childNodes = new JavaXMLNodeList(node.getChildNodes());
	}

	@Override
	public XMLAttributes getAttributes() {
		return attributes;
	}

	@Override
	public String getNodeText() {
		return node.getTextContent();
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

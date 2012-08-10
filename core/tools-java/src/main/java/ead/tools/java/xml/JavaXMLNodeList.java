package ead.tools.java.xml;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

public class JavaXMLNodeList implements XMLNodeList {

	private ArrayList<XMLNode> nodes;

	public JavaXMLNodeList(NodeList nodeList) {
		nodes = new ArrayList<XMLNode>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			nodes.add(new JavaXMLNode(nodeList.item(i)));
		}

	}

	@Override
	public XMLNode item(int index) {
		return nodes.get(index);
	}

	@Override
	public int getLength() {
		return nodes.size();
	}

}

package ead.tools.gwt.xml;

import java.util.ArrayList;

import com.google.gwt.xml.client.NodeList;

import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

public class GwtXMLNodeList implements XMLNodeList {

	private ArrayList<XMLNode> nodes;

	public GwtXMLNodeList(NodeList nodeList) {
		nodes = new ArrayList<XMLNode>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			nodes.add(new GwtXMLNode(nodeList.item(i)));
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

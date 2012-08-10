package ead.tools.gwt.xml;


import com.google.gwt.xml.client.Document;

import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLNode;

public class GwtXMLDocument implements XMLDocument {

	private XMLNode firstChild;

	public GwtXMLDocument(Document document) {
		this.firstChild = new GwtXMLNode(document.getFirstChild());
	}

	@Override
	public XMLNode getFirstChild() {
		return firstChild;
	}

}

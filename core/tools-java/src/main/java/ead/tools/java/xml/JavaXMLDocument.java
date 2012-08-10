package ead.tools.java.xml;

import org.w3c.dom.Document;

import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLNode;

public class JavaXMLDocument implements XMLDocument {

	private XMLNode firstChild;

	public JavaXMLDocument(Document document) {
		this.firstChild = new JavaXMLNode(document.getFirstChild());
	}

	@Override
	public XMLNode getFirstChild() {
		return firstChild;
	}

}

package ead.tools.gwt.xml;

import com.google.gwt.xml.client.Document;

import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLParser;

public class GwtXMLParser implements XMLParser {

	@Override
	public XMLDocument parse(String xml) {
		Document doc = com.google.gwt.xml.client.XMLParser.parse(xml);
		doc.getDocumentElement().normalize();
		return new GwtXMLDocument( doc );
	}

}

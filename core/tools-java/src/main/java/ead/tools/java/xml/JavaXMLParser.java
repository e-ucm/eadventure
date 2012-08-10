package ead.tools.java.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLParser;

public class JavaXMLParser implements XMLParser {

	private DocumentBuilder dBuilder;

	public JavaXMLParser() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {

		}
	}

	@Override
	public XMLDocument parse(String xml) {

		InputStream is = new ByteArrayInputStream(xml.getBytes());

		try {
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			return new JavaXMLDocument(doc);

		} catch (SAXException e) {

		} catch (IOException e) {

		}

		return null;
	}

}

/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.tools.java.xml.sax;

import es.eucm.ead.tools.xml.XMLDocument;
import es.eucm.ead.tools.xml.XMLParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.Map;
import java.util.Stack;

public class SaxXMLParser extends DefaultHandler implements XMLParser {

	private SAXParser saxParser;

	private SaxXMLDocument currentDocument;

	private Stack<SaxXMLNode> nodes;

	private StringBuffer text;

	public SaxXMLParser() {
		nodes = new Stack<SaxXMLNode>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {

		} catch (SAXException e) {
		}
	}

	@Override
	public XMLDocument parse(String xml) {
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		try {
			saxParser.parse(is, this);
		} catch (IOException e) {

		} catch (SAXException e) {

		} finally {
			try {
				is.close();
			} catch (IOException e) {

			}
		}
		return currentDocument;
	}

	@Override
	public XMLDocument createDocument() {
		return new SaxXMLDocument();
	}

	@Override
	public void startDocument() throws SAXException {
		nodes.clear();
		currentDocument = new SaxXMLDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		SaxXMLNode node = new SaxXMLNode(qName, attributes);
		nodes.push(node);
		text = new StringBuffer();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		SaxXMLNode node = nodes.pop();
		if (!"".equals(text.toString())) {
			node.setText(text.toString());
		}
		if (!nodes.empty()) {
			nodes.peek().append(node);
		} else {
			currentDocument.appendChild(node);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		text.append(ch, start, length);
	}

	@Override
	public void writeToFile(XMLDocument document, String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer
					.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			SaxXMLNode node = (SaxXMLNode) document.getFirstChild();
			writeNode(node, writer);
		} catch (Exception e) {

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {

				}
			}
		}
	}

	private void writeNode(SaxXMLNode node, BufferedWriter writer)
			throws IOException {
		writer.write("<" + node.getNodeName());
		// write attributes
		for (Map.Entry<String, String> entry : node.getAttributes().entrySet()) {
			writer
					.write(" " + entry.getKey() + "=\"" + entry.getValue()
							+ "\"");
		}
		if (node.hasChildNodes() || !"".equals(node.getNodeText())) {
			writer.write(">");
			writer.write(node.getNodeText());
			for (SaxXMLNode n : node.getNodes()) {
				writeNode(n, writer);
			}
			writer.write("</" + node.getNodeName() + ">");
		} else {
			writer.write("/>");
		}
	}
}

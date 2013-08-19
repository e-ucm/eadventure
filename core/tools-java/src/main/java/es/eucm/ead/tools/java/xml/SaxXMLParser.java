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

package es.eucm.ead.tools.java.xml;

import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.tools.xml.XMLParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class SaxXMLParser extends DefaultHandler implements XMLParser {

	private SAXParser saxParser;

	private XMLNode currentDocument;

	private Stack<XMLNode> nodes;

	private StringBuffer text;

	public SaxXMLParser() {
		nodes = new Stack<XMLNode>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {

		} catch (SAXException e) {
		}
	}

	@Override
	public XMLNode parse(String xml) {
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
	public void startDocument() throws SAXException {
		nodes.clear();
		currentDocument = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		XMLNode node = new XMLNode(qName, attributes);
		if (currentDocument == null) {
			currentDocument = node;
		}
		nodes.push(node);
		text = new StringBuffer();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		XMLNode node = nodes.pop();
		if (text != null && !"".equals(text.toString())) {
			node.setText(text.toString());
			text = new StringBuffer();
		}
		if (!nodes.empty()) {
			nodes.peek().append(node);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		text.append(ch, start, length);
	}

}

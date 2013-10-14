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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DomXMLParser implements XMLParser {

	static private Logger logger = LoggerFactory.getLogger(DomXMLParser.class);

	private DocumentBuilder dBuilder;
	private TransformerFactory tf;

	public DomXMLParser() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		tf = TransformerFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {

		}
	}

	@Override
	public XMLNode parse(String xml) {
		return parse(xml, null);
	}

	@Override
	public XMLNode parse(String xml, ErrorHandler errorHandler) {
		InputStream is = new ByteArrayInputStream(xml.getBytes());

		try {
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			return generateNode((Element) doc.getFirstChild());
		} catch (SAXException e) {
			if (errorHandler != null) {
				errorHandler.error(e.getMessage());
			} else {
				logger.error("Error reading xml: {}", e);
			}
		} catch (IOException e) {
			logger.error("Error reading xml: {}", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}
		return null;
	}

	public XMLNode generateNode(Element n) {
		XMLNode node = new XMLNode(n.getTagName());
		node.setText(n.getTextContent());
		NamedNodeMap atts = n.getAttributes();
		for (int i = 0; i < atts.getLength(); i++) {
			node.setAttribute(atts.item(i).getNodeName(), atts.item(i)
					.getNodeValue());
		}
		NodeList nl = n.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			XMLNode child = generateNode((Element) nl.item(i));
			node.append(child);
		}
		return node;
	}

}

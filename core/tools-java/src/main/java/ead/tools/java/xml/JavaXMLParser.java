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

package ead.tools.java.xml;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLParser;

public class JavaXMLParser implements XMLParser {

	private static final Logger logger = LoggerFactory
			.getLogger("JavaXMLParser");

	private DocumentBuilder dBuilder;
	private TransformerFactory tf;

	public JavaXMLParser() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		tf = TransformerFactory.newInstance();
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

	@Override
	public XMLDocument createDocument() {
		Document doc = dBuilder.newDocument();
		return new JavaXMLDocument(doc);
	}

	@Override
	public void writeToFile(XMLDocument document, String file) {
		OutputStreamWriter outputStreamWriter = null;
		try {
			Transformer transformer = tf.newTransformer();
			outputStreamWriter = new OutputStreamWriter(new FileOutputStream(
					file), "UTF-8");
			transformer.transform(new DOMSource(((JavaXMLDocument) document)
					.getDocument()), new StreamResult(outputStreamWriter));
		} catch (Exception e) {
			logger.error("{}", e);
		} finally {
			if (outputStreamWriter != null)
				try {
					outputStreamWriter.close();
				} catch (IOException e) {

				}
		}

	}

}

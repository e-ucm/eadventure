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

package es.eucm.eadventure.common.impl.strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.StringFileHandler;
import es.eucm.eadventure.common.params.EAdString;

@Singleton
public class DefaultStringFileHandler implements StringFileHandler {
	
	public Map<EAdString, String> read( InputStream inputStream ){
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);

			SAXParser saxParser = factory.newSAXParser();
			StringXMLHandler handler = new StringXMLHandler();
			saxParser.parse(inputStream, handler);

			return handler.getStrings();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean write(OutputStream outputStream,
			Map<EAdString, String> strings) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			TransformerFactory tf = TransformerFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();

			Element mainNode = doc.createElement("resources");

			for (Entry<EAdString, String> entry : strings.entrySet()) {
				Element newElement = doc.createElement("string");
				newElement.setAttribute("name", entry.getKey().toString());
				newElement.setTextContent(entry.getValue());
				Node newNode = doc.adoptNode(newElement);
				mainNode.appendChild(newNode);
			}
			doc.adoptNode(mainNode);
			doc.appendChild(mainNode);

			Transformer transformer = tf.newTransformer();

			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					outputStream, "UTF-8");
			transformer.transform(new DOMSource(doc), new StreamResult(
					outputStreamWriter));
			outputStreamWriter.close();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

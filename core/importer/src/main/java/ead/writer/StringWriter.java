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

package ead.writer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.inject.Singleton;

import ead.common.params.text.EAdString;

@Singleton
public class StringWriter  {

	private static final Logger logger = LoggerFactory
			.getLogger("DefaultStringFileHandler");

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
			logger.error("Could not write strings file", e);
			return false;
		}
	}

	
	public boolean write(String file, Map<EAdString, String> strings) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			write(out, strings);
		} catch (FileNotFoundException e) {

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

}

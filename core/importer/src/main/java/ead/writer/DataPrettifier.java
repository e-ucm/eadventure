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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ead.writer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import ead.reader.elements.DOMTags;

/**
 * Utility class that prettifies data.xml files so that they become easier to
 * compare or read.
 *
 * @author mfreire
 */
public class DataPrettifier {

	private static Logger logger = LoggerFactory.getLogger("DataPrettifier");

	static final public HashSet<String> translatedAttributes = new HashSet<String>();
	static final public String keymapElement = "keyMap";
	static final public String keymapEntry = "entry";

	static {
		translatedAttributes.add(DOMTags.CLASS_AT);
		translatedAttributes.add(DOMTags.KEY_CLASS_AT); // "kC"
		translatedAttributes.add(DOMTags.VALUE_CLASS_AT); // "vC"
	}

	/**
	 * Prettify an input into an output.
	 * @param input file; requires two passes (the first one reads the mappings)
	 * @param output file; created in a single pass
	 * @throws IOException
	 */
	public static void prettify(File input, File output) throws IOException {

		TreeMap<String, String> mappings = new TreeMap<String, String>();

		// fist pass: build map
		FileInputStream in = null;
		try {
			in = new FileInputStream(input);
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader reader = inputFactory.createXMLStreamReader(in);

			boolean inMappings = false;
			while (true) {
				int event = reader.next();
				if (event == XMLStreamConstants.END_DOCUMENT) {
					reader.close();
					break;
				}
				if (event == XMLStreamConstants.START_ELEMENT) {
					if (reader.getLocalName().equals(keymapElement)) {
						inMappings = true;
					}

					if (inMappings && reader.getLocalName().equals(keymapEntry)) {
						// in mapping list - now we can build our mappings
						mappings.put(reader.getAttributeValue(0), reader
								.getAttributeValue(1));
						logger.debug("Mapped {} to {}", new String[] {
								reader.getAttributeValue(0),
								reader.getAttributeValue(1) });
					}
				}

				if (event == XMLStreamConstants.END_ELEMENT
						&& reader.getLocalName().equals(keymapElement)) {
					inMappings = false;
				}
			}
		} catch (Exception e) {
			logger.error("Unable to prettify (step 1)", e);
		} finally {
			if (in != null) {
				in.close();
			}
		}

		// second pass: read while translating with map
		in = null;
		try {
			ByteArrayOutputStream cache = new ByteArrayOutputStream();

			in = new FileInputStream(input);
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader reader = inputFactory.createXMLStreamReader(in);
			XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
			logger.debug("Output factory is {}", outputFactory.getClass()
					.getName());
			XMLStreamWriter writer = outputFactory.createXMLStreamWriter(cache);
			writer.writeStartDocument();

			while (true) {
				int event = reader.next();

				if (event == XMLStreamConstants.END_DOCUMENT) {
					reader.close();
					writer.writeEndDocument();
					writer.close();
					break;
				}

				if ((reader.isStartElement() || reader.isEndElement())
						&& (reader.getLocalName().equals(keymapElement) || reader
								.getLocalName().equals(keymapEntry))) {
					// ignore the keymap and its entries
					continue;
				}

				if (event == XMLStreamConstants.START_ELEMENT) {
					writer.writeStartElement(reader.getLocalName());
					logger.debug("Started {}", reader.getLocalName());

					// not in mappings list - copy element
					for (int i = reader.getAttributeCount() - 1; i >= 0; i--) {
						String name = reader.getAttributeLocalName(i);
						String value = reader.getAttributeValue(i);
						if (translatedAttributes.contains(name)
								&& mappings.containsKey(value)) {
							logger.debug("\t(switch {}  ::= {}", new String[] {
									value, mappings.get(value) });
							value = mappings.get(value);
						}
						writer.writeAttribute(name, value);
						logger.debug("\tattr: {} --> {}", new String[] { name,
								value });
					}
				}
				switch (event) {
				case XMLStreamConstants.CDATA:
					writer.writeCData(reader.getText());
					break;
				case XMLStreamConstants.CHARACTERS:
				case XMLStreamConstants.SPACE:
					writer.writeCharacters(reader.getText());
					break;
				case XMLStreamConstants.COMMENT:
					writer.writeComment(reader.getText());
					break;
				case XMLStreamConstants.END_ELEMENT:
					writer.writeEndElement();
					break;
				default:
					break;
				}
			}

			// now, prettify it (using mojo from http://stackoverflow.com/a/4472580/15472)
			Transformer serializer = SAXTransformerFactory.newInstance()
					.newTransformer();
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			//serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			serializer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			//serializer.setOutputProperty("{http://xml.customer.org/xslt}indent-amount", "2");
			Source xmlSource = new SAXSource(new InputSource(
					new ByteArrayInputStream(cache.toByteArray())));
			StreamResult res = new StreamResult(new FileOutputStream(output));
			serializer.transform(xmlSource, res);

		} catch (Exception e) {
			throw new IOException("Unable to prettify (step 2)", e);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String s1 = args.length > 0 ? args[0] : "/tmp/y1/data.xml";
		String s2 = args.length > 1 ? args[1] : "/tmp/y1/d1.xml";
		File f1 = new File(s1);
		File f11 = new File(s2);
		DataPrettifier.prettify(f1, f11);
		//File f2 = new File("/tmp/y2/data.xml");
		//File f21 = new File("/tmp/y2/d1.xml");
		//DataPrettifier.prettify(f2, f21);
	}
}

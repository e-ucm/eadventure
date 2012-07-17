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
package ead.editor.model;

// FIXME: fully aware that this is ugly
import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

import java.io.*;
import java.util.TreeMap;
import javax.xml.stream.*;

/**
 * Utility class that prettifies data.xml files so that they become easier to
 * compare or read.
 *
 * @author mfreire
 */
public class DataPrettifier {

    /**
     * Prettifies an input into an output.
     * @param input file; requires two passes (the first one reads the mappings)
     * @param output file; created in a single pass
     * @throws IOException
     */
    public static void prettify(File input, File output) throws IOException {

        TreeMap<String, String> mappings = new TreeMap<String, String>();

        // fist pass: build map
        try {
            FileInputStream in = new FileInputStream(input);
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
                    if (reader.getLocalName().equals("keyMap")) {
                        inMappings = true;
                    }

                    if (inMappings && reader.getLocalName().equals("entry")) {
                        // in mapping list - now we can build our mappings
                        mappings.put(
                                reader.getAttributeValue(0),
                                reader.getAttributeValue(1));
//						System.err.println("Mapped " + reader.getAttributeValue(0)
//								+ " to " + reader.getAttributeValue(1));
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT
                        && reader.getLocalName().equals("keyMap")) {
                    inMappings = false;
                }
            }
        } catch (Exception e) {
            throw new IOException("Unable to prettify (step 1)", e);
        }

        // second pass: read while translating with map
        try {
            FileInputStream in = new FileInputStream(input);
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(in);
            OutputStream out = new FileOutputStream(output);
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(out);
            writer = new IndentingXMLStreamWriter(writer);
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
                        && (reader.getLocalName().equals("keyMap") || reader.getLocalName().equals("entry"))) {
                    // ignore the keymap and its entries
                    continue;
                }

                if (event == XMLStreamConstants.START_ELEMENT) {
                    writer.writeStartElement(reader.getLocalName());
//					System.err.println("Started " + reader.getLocalName());

                    // not in mappings list - copy element
                    for (int i = reader.getAttributeCount() - 1; i >= 0; i--) {
                        String name = reader.getAttributeLocalName(i);
                        String value = reader.getAttributeValue(i);
                        if (mappings.containsKey(value)) {
//							System.err.println("\t(switch " + value + " ::= " + mappings.get(value));
                            value = mappings.get(value);
                        }
                        writer.writeAttribute(name, value);
//						System.err.println("\tattr: " + name + " --> " + value);
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
        } catch (Exception e) {
            throw new IOException("Unable to prettify (step 2)", e);
        }
    }

    public static void main(String[] args) throws Exception {
        File f1 = new File("/tmp/y1/data.xml");
        File f11 = new File("/tmp/y1/d1.xml");
        DataPrettifier.prettify(f1, f11);
        File f2 = new File("/tmp/y2/data.xml");
        File f21 = new File("/tmp/y2/d1.xml");
        DataPrettifier.prettify(f2, f21);
    }
}

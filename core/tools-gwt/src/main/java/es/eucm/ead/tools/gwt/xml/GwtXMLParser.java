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

package es.eucm.ead.tools.gwt.xml;

import com.google.gwt.xml.client.*;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.tools.xml.XMLParser;

public class GwtXMLParser implements XMLParser {

	@Override
	public XMLNode parse(String xml) {
		Document doc = com.google.gwt.xml.client.XMLParser.parse(xml);
		doc.getDocumentElement().normalize();
		return generateNode((Element) doc.getFirstChild());
	}

	public XMLNode generateNode(Element n) {
		XMLNode node = new XMLNode(n.getTagName());
		// Extract text
		NodeList nodes = n.getChildNodes();
		String result = "";
		try {
			for (int i = 0; i < nodes.getLength(); i++) {
				String value = nodes.item(i).getNodeValue();
				if (value != null) {
					result += (value.equals("null") ? "" : value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		node.setText(result);
		// Extract attributes
		NamedNodeMap atts = n.getAttributes();
		for (int i = 0; i < atts.getLength(); i++) {
			node.setAttribute(atts.item(i).getNodeName(), atts.item(i)
					.getNodeValue());
		}
		NodeList nl = n.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node item = nl.item(i);
			if (item instanceof Element) {
				XMLNode child = generateNode((Element) item);
				node.append(child);
			}
		}
		return node;
	}
}

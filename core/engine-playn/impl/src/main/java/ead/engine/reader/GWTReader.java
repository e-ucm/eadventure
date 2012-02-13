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

package ead.engine.reader;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import ead.common.DOMTags;
import ead.common.model.elements.EAdAdventureModelImpl;
import ead.engine.core.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GWTReader {

	private static Logger logger = LoggerFactory.getLogger("GWTReader");

	private String xml;

	public void readXML(String fileName, final Game game) {
		logger.info("Reading " + fileName);
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				fileName);

		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					// throw exception;
					logger.info("Error: " + exception);
				}

				public void onResponseReceived(Request request,
						Response response) {
					xml = response.getText();
					logger.info("Parsing XML...");
					Document doc = XMLParser.parse(xml);
					logger.info("Parsed.");
					logger.info("Building the game.");
					ElementNodeVisitor env = new ElementNodeVisitor();
					NodeVisitor.init(doc.getFirstChild().getAttributes()
							.getNamedItem(DOMTags.PACKAGE_AT).getNodeValue());
					getAliasMap(doc);
					EAdAdventureModelImpl data = (EAdAdventureModelImpl) env
							.visit(doc.getFirstChild().getFirstChild(), null,
									null, null);
					
					logger.info("Built.");
					
					if (data == null)
						logger.info("Data is null");
					else{
						data.getDepthControlList().clear();
						logger.info("Setting the game");
					}
					game.setGame(data, data.getChapters().get(0));
				}
			});
		} catch (RequestException ex) {
			// requestFailed(ex);
		}
	}

	static String getNodeText(com.google.gwt.xml.client.Node xmlNode) {
		if (xmlNode == null)
			return "";
		NodeList nodes = xmlNode.getChildNodes();
		String result = "";
		try {
			for (int i = 0; i < nodes.getLength(); i++) {
				String value = nodes.item(i).getNodeValue();
				if (value != null)
					result += (value.equals("null") ? "" : value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void getAliasMap(Document doc) {
		NodeList nl = doc.getFirstChild().getChildNodes();

		for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
			logger.info(nl.item(i).getNodeName());
			if (nl.item(i).getNodeName().equals("keyMap")) {
				NodeList nl2 = nl.item(i).getChildNodes();

				for (int j = 0, cnt2 = nl2.getLength(); j < cnt2; j++) {
					Node n = nl2.item(j);
					NodeVisitor.aliasMap.put(
							n.getAttributes().getNamedItem("key")
									.getNodeValue(), n.getAttributes()
									.getNamedItem("value").getNodeValue());
				}

			}
		}

	}
}

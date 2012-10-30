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

package ead.reader.adventure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.EAdAdventureModel;
import ead.reader.adventure.visitors.ElementNodeVisitor;
import ead.reader.adventure.visitors.NodeVisitor;
import ead.reader.adventure.visitors.NodeVisitor.NodeVisitorListener;
import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;
import ead.tools.xml.XMLParser;

public class AdventureReader {

	private static Logger logger = LoggerFactory.getLogger("AdventureReader");

	private XMLParser xmlParser;
	
	private BasicAdventureModel data; 

	public AdventureReader(XMLParser xmlParser) {
		this.xmlParser = xmlParser;
	}

	public EAdAdventureModel readXML(String xml) {
		logger.info("Parsing XML...");
		XMLDocument doc = xmlParser.parse(xml);
		logger.info("Parsed.");
		logger.info("Building the game.");
		ElementNodeVisitor env = new ElementNodeVisitor();
		NodeVisitor.init(doc.getFirstChild().getAttributes()
				.getValue(DOMTags.PACKAGE_AT));
		getAliasMap(doc);
		

		env.visit(doc.getFirstChild().getFirstChild(), null, null, null,
				new NodeVisitorListener() {

					@Override
					public void elementRead(Object element) {
						data = (BasicAdventureModel) element;

					}

				}

		);

		logger.info("Built.");

		if (data == null) {
			logger.info("Data is null");
		} else {
			data.getDepthControlList().clear();
			logger.info("Setting the game");
		}

		return data;
	}

	private void getAliasMap(XMLDocument doc) {
		XMLNodeList nl = doc.getFirstChild().getChildNodes();

		for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
			logger.info("At alias {}: {}", new String[] {""+i, nl.item(i).getNodeName()});
			if (nl.item(i).getNodeName().equals("keyMap")) {
				XMLNodeList nl2 = nl.item(i).getChildNodes();

				for (int j = 0, cnt2 = nl2.getLength(); j < cnt2; j++) {
					XMLNode n = nl2.item(j);
					NodeVisitor.aliasMap.put(n.getAttributes().getValue("key"),
							n.getAttributes().getValue("value"));
				}

			}
		}

	}

	public static class EAdAventureModelProxy {

		private EAdAdventureModel model;

		public void setModel(EAdAdventureModel model) {
			this.model = model;
		}

		public EAdAdventureModel getModel() {
			return model;
		}

	}
}

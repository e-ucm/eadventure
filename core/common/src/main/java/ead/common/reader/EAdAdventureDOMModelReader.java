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

package ead.common.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ead.common.DOMTags;
import ead.common.Reader;
import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.reader.extra.ObjectFactory;
import ead.common.reader.visitors.ElementNodeVisitor;
import ead.common.reader.visitors.NodeVisitor;
/**
 * The reader for the XML representation of the model
 */
public class EAdAdventureDOMModelReader implements Reader<EAdAdventureModel> {

	private static final Logger logger = LoggerFactory.getLogger("EAdReader");
	
	@Override
	public EAdAdventureModel read(URI fileURI) {
		try {
			File file = new File(fileURI);
			FileInputStream fileInputStream = new FileInputStream(file);
			EAdAdventureModel data = read(fileInputStream);
			return data;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public EAdAdventureModel read(InputStream inputStream) {
		BasicAdventureModel data = null;
		try {
			ObjectFactory.initialize();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
			ElementNodeVisitor env = new ElementNodeVisitor();
			NodeVisitor.init(doc.getFirstChild().getAttributes().getNamedItem(DOMTags.PACKAGE_AT).getNodeValue());
			getAliasMap(doc);
			data = (BasicAdventureModel) env.visit(doc.getFirstChild().getFirstChild(), null, null, null);
			data.getDepthControlList().clear();

			return data;
		} catch(Exception e) {
			logger.error("Error reading model", e);
        }
        return null;
	}

	private void getAliasMap(Document doc) {
		NodeList nl = doc.getFirstChild().getChildNodes();

		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			if (nl.item(i).getNodeName().equals("keyMap")) {
				NodeList nl2 = nl.item(i).getChildNodes();

				for(int j=0, cnt2=nl2.getLength(); j<cnt2; j++)
				{
					Node n = nl2.item(j);
					NodeVisitor.aliasMap.put(n.getAttributes().getNamedItem("key").getNodeValue(),
							n.getAttributes().getNamedItem("value").getNodeValue());
				}

			}
		}

	}

	public void visit(Node node, int level)
	{
		NodeList nl = node.getChildNodes();

		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			System.out.println(level + " ["+nl.item(i).getNodeName() + (nl.item(i).getNodeValue() != null ? nl.item(i).getNodeValue() : "") + "]");
			visit(nl.item(i), level+1);
		}
	}

	public EAdAdventureModel read(String fileURI) {
		try {
			File file = new File(fileURI);
			FileInputStream fileInputStream = new FileInputStream(file);
			EAdAdventureModel data = read(fileInputStream);
			return data;
		} catch (FileNotFoundException e) {
			return null;
		}
	}


}

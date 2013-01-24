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

package ead.reader;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdAdventureModel;
import ead.reader.model.XMLVisitor;
import ead.reader.model.XMLVisitor.VisitorListener;
import ead.reader.model.translators.MapClassTranslator;
import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;
import ead.tools.xml.XMLParser;

@Singleton
public class AdventureReader implements VisitorListener {

	private XMLParser xmlParser;
	private XMLVisitor visitor;
	private EAdAdventureModel model;

	@Inject
	public AdventureReader(XMLParser parser,
			ReflectionProvider reflectionProvider) {
		this.xmlParser = parser;
		this.visitor = new XMLVisitor(reflectionProvider);
	}

	public EAdAdventureModel readXML(String xml) {
		readXML(xml, this);
		boolean done = false;
		while (!done) {
			done = visitor.step();
		}
		return model;
	}

	public void readXML(String xml, VisitorListener listener) {
		XMLDocument document = xmlParser.parse(xml);
		visitor.init();

		// Load classes keys
		Map<String, String> classes = new HashMap<String, String>();
		XMLNode node = document.getFirstChild();
		XMLNode keyMap = node.getChildNodes().item(0);
		XMLNodeList entries = keyMap.getChildNodes();
		for (int i = 0; i < entries.getLength(); i++) {
			XMLNode n = entries.item(i);
			String className = n.getAttributes().getValue(DOMTags.VALUE_AT);
			classes.put(n.getAttributes().getValue(DOMTags.KEY_AT), className);
		}
		visitor.addTranslator(new MapClassTranslator(classes));

		XMLNode adventure = node.getChildNodes().item(1);
		visitor.loadElement(adventure, listener);
	}

	public boolean step() {
		return visitor.step();
	}

	@Override
	public boolean loaded(XMLNode node, Object object, boolean isNullInOrigin) {
		this.model = (EAdAdventureModel) object;
		return true;
	}

}

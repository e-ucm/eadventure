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

package es.eucm.eadventure.common.impl.reader.subparsers;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import es.eucm.eadventure.common.impl.reader.subparsers.extra.EntryData;
import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.interfaces.features.Resourced;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.resources.EAdResources;

public class AdventureHandler extends DefaultHandler {

	private EAdAdventureModel adventureModel;

	private Stack<Object> elementStack;

	private Stack<Subparser> subparserStack;

	private String classParam;

	@Inject
	public AdventureHandler(EAdAdventureModel adventureModel,
			@Named("classParam") String classParam) {
		this.adventureModel = adventureModel;
		subparserStack = new Stack<Subparser>();
		elementStack = new Stack<Object>();
		elementStack.push(adventureModel);
		ObjectFactory.initilize();
		this.classParam = classParam;
	}

	public EAdAdventureModel getAdventureModel() {
		return adventureModel;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		if (qName.equals("param")) {
			subparserStack.push(new ParamSubparser(elementStack.peek(),
					attributes));
		} else if (qName.equals("resources")) {
			subparserStack.push(new ResourcesSubparser(
					(EAdElement) elementStack.peek(), attributes));
		} else if (qName.equals("bundle")) {
			if (elementStack.peek() instanceof Resourced) {
				EAdResources resources = ((Resourced) elementStack.peek())
						.getResources();
				BundleSubparser bundleSubparser = new BundleSubparser(
						resources, attributes);
				subparserStack.push(bundleSubparser);
			}
		} else if (qName.equals("asset")) {
			AssetSubparser assetSubparser = new AssetSubparser(attributes);
			subparserStack.peek().addChild(
					new EntryData(attributes.getValue("id"), assetSubparser
							.getAsset()));
			subparserStack.push(assetSubparser);
			elementStack.push(assetSubparser.getAsset());
		} else if (qName.equals("element")) {
			ElementSubparser elementSubparser = new ElementSubparser(
					(EAdElement) elementStack.peek(), attributes, classParam);
			EAdElement element = elementSubparser.getElement();

			subparserStack.peek().addChild(element);

			subparserStack.push(elementSubparser);
			elementStack.push(element);
		} else if (qName.equals("element-ref")) {
			// EAdElement element = elementMap.get(attributes.getValue("id"));
			ElementSubparser elementSubparser = new ElementSubparser(
					(EAdElement) elementStack.peek(), attributes, classParam);
			EAdElement element = elementSubparser.getElement();

			subparserStack.peek().addChild(element);

			subparserStack.push(elementSubparser);
			elementStack.push(element);
		} else if (qName.equals("list")) {
			ListSubparser listSubparser = new ListSubparser(
					(EAdElement) elementStack.peek(), attributes);
			if (subparserStack.size() > 0)
				subparserStack.peek().addChild(listSubparser.getList());
			subparserStack.push(listSubparser);
			elementStack.push(listSubparser.getList());
		} else if (qName.equals("map")) {
			MapSubparser mapSubparser = new MapSubparser(
					(EAdElement) elementStack.peek(), attributes);
			elementStack.push(mapSubparser.getMap());
			subparserStack.push(mapSubparser);
		} else if (qName.equals("entry")
				&& elementStack.peek() instanceof EAdMap) {
			subparserStack.push(new EntrySubparser((EAdMap<?, ?>) elementStack
					.peek(), attributes));
		} else if (qName.equals("entry")
				&& elementStack.peek() instanceof EAdList) {
			subparserStack.push(new EntrySubparser((EAdList<?>) elementStack
					.peek(), attributes));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if (qName.equals("element") || qName.equals("element-ref")
				|| qName.equals("map") || qName.equals("asset")
				|| qName.equals("list"))
			elementStack.pop();
		if (!subparserStack.isEmpty())
			subparserStack.pop().endElement();
	}

	@Override
	public void characters(char[] buf, int offset, int len) {
		if (!subparserStack.isEmpty())
			subparserStack.peek().characters(buf, offset, len);
	}

}

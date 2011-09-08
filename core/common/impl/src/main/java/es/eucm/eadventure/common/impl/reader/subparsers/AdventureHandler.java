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

import es.eucm.eadventure.common.impl.reader.subparsers.extra.AssetId;
import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.resources.EAdResources;

public class AdventureHandler extends DefaultHandler {

	private EAdAdventureModel adventureModel;

	private Stack<Subparser> subparserStack;

	private String classParam;

	@Inject
	public AdventureHandler(EAdAdventureModel adventureModel,
			@Named("classParam") String classParam) {
		this.adventureModel = adventureModel;
		subparserStack = new Stack<Subparser>();
		ObjectFactory.initilize();
		this.classParam = classParam;
	}

	public EAdAdventureModel getAdventureModel() {
		return adventureModel;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		if (qName.equals("adventure")) {
			String packageName = attributes.getValue("package");
			Subparser.init(packageName);
		} else if (qName.equals("element")) {
			Object o = null;
			if ( !subparserStack.isEmpty() ){
				o = subparserStack.peek().getObject();
			}
			subparserStack.push(new ElementSubparser(o, attributes, classParam));
		} else if (qName.equals("list")) {
			subparserStack.push(new ListSubparser(subparserStack.peek()
					.getObject(), attributes));
		} else if (qName.equals("map")) {
			subparserStack.push(new MapSubparser(subparserStack.peek()
					.getObject(), attributes));
		} else if (qName.equals("object") || qName.equals("param")) {
			subparserStack.push(new ObjectSubparser(subparserStack.peek()
					.getObject(), attributes));
		} else if (qName.equals("resources")) {
			subparserStack
					.push(new ResourcesSubparser((EAdElement) subparserStack
							.peek().getObject(), attributes));
		} else if (qName.equals("bundle")) {
			if (subparserStack.peek().getObject() instanceof EAdResources) {
				EAdResources resources = (EAdResources) subparserStack.peek()
						.getObject();
				BundleSubparser bundleSubparser = new BundleSubparser(
						resources, attributes);
				subparserStack.push(bundleSubparser);
			}
		} else if (qName.equals("asset")) {
			AssetSubparser assetSubparser = new AssetSubparser(
					(AssetId) subparserStack.peek(), attributes);
			subparserStack.push(assetSubparser);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {

		if (subparserStack.size() == 1)
			adventureModel = (EAdAdventureModel) subparserStack.peek()
					.getObject();

		if (subparserStack.size() >= 2) {
			Subparser parser = subparserStack.pop();
			parser.endElement();
			subparserStack.peek().addChild(parser.getObject());

		}
	}

	@Override
	public void characters(char[] buf, int offset, int len) {
		if (!subparserStack.isEmpty())
			subparserStack.peek().characters(buf, offset, len);
	}

}

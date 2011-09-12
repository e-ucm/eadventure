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

import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.impl.writer.AssetDOMWriter;
import es.eucm.eadventure.common.impl.writer.DOMWriter;
import es.eucm.eadventure.common.impl.writer.DefaultDOMWriter;
import es.eucm.eadventure.common.impl.writer.ElementDOMWriter;
import es.eucm.eadventure.common.impl.writer.ListDOMWriter;
import es.eucm.eadventure.common.impl.writer.MapDOMWriter;
import es.eucm.eadventure.common.impl.writer.ParamDOMWriter;
import es.eucm.eadventure.common.impl.writer.ResourcesDOMWriter;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.resources.EAdResources;

public class AdventureHandler extends DefaultHandler {

	private EAdAdventureModel adventureModel;

	private Stack<Subparser<?>> parserStack;

	public AdventureHandler() {
		parserStack = new Stack<Subparser<?>>();
		ObjectFactory.initilize();
	}

	public EAdAdventureModel getAdventureModel() {
		return adventureModel;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		if (qName.equals(DOMWriter.ROOT_TAG)) {
			String packageName = attributes.getValue(DOMWriter.PACKAGE_AT);
			Subparser.init(packageName);
		} else if (qName.equals(ElementDOMWriter.TAG)) {
			Object o = null;
			if (!parserStack.isEmpty()) {
				o = parserStack.peek().getObject();
			}
			parserStack.push(new ElementSubparser(o, attributes));
		} else if (qName.equals(ListDOMWriter.TAG)) {
			parserStack.push(new ListSubparser(parserStack.peek().getObject(),
					attributes));
		} else if (qName.equals(MapDOMWriter.TAG)) {
			parserStack.push(new MapSubparser(parserStack.peek().getObject(),
					attributes));
		} else if (qName.equals(DefaultDOMWriter.TAG)
				|| qName.equals(ParamDOMWriter.TAG)) {
			parserStack.push(new ObjectSubparser(
					parserStack.peek().getObject(), attributes));
		} else if (qName.equals(ResourcesDOMWriter.TAG_RESOURCES)) {
			parserStack.push(new ResourcesSubparser((EAdElement) parserStack
					.peek().getObject(), attributes));
		} else if (qName.equals(ResourcesDOMWriter.TAG_BUNDLE)) {
			if (parserStack.peek().getObject() instanceof EAdResources) {
				EAdResources resources = (EAdResources) parserStack.peek()
						.getObject();
				BundleSubparser bundleSubparser = new BundleSubparser(
						resources, attributes);
				parserStack.push(bundleSubparser);
			}
		} else if (qName.equals(AssetDOMWriter.TAG)) {
			AssetSubparser assetSubparser = new AssetSubparser(parserStack
					.peek().getObject(), parserStack.peek(), attributes);
			parserStack.push(assetSubparser);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {

		if (parserStack.size() == 1)
			adventureModel = (EAdAdventureModel) parserStack.peek().getObject();

		if (parserStack.size() >= 2) {
			Subparser<?> parser = parserStack.pop();
			parser.endElement();
			parserStack.peek().addChild(parser.getObject());

		}
	}

	@Override
	public void characters(char[] buf, int offset, int len) {
		if (!parserStack.isEmpty())
			parserStack.peek().characters(buf, offset, len);
	}

}

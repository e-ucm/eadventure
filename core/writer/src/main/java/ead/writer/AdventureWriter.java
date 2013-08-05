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

package ead.writer;

import com.google.inject.Inject;

import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLDocument;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.tools.xml.XMLParser;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ModelVisitor.VisitorListener;

public class AdventureWriter {

	private ModelVisitor visitor;

	private XMLParser xmlParser;

	@Inject
	public AdventureWriter(ReflectionProvider reflectionProvider,
			XMLParser parser) {
		this.visitor = new ModelVisitor(reflectionProvider, parser);
		this.xmlParser = parser;
	}

	public XMLDocument write(EAdAdventureModel model) {
		write(model, new VisitorListener() {

			@Override
			public void load(XMLNode node, Object object) {
				// Do nothing
			}

		});
		boolean done = false;
		while (!done) {
			done = visitor.step();
		}
		return visitor.getDocument();
	}

	public void write(EAdAdventureModel model, VisitorListener listener) {
		visitor.clear();
		visitor.writeElement(model, null, listener);
	}

	public void write(EAdAdventureModel model, String string) {
		XMLDocument doc = write(model);
		xmlParser.writeToFile(doc, string);
	}

	/**
	 * Sets if simplifications of the model must be performed while writing (it
	 * can slow the writing process. Just a bit. Trust me, better slow it now
	 * than while reading. You do NOT want that. You do not. That's why
	 * simplification is enable by default)
	 * 
	 * @param enable
	 */
	public void setEnableSimplifications(boolean enable) {
		visitor.setEnableSimplifcations(enable);
	}

}

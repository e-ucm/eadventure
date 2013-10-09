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
package es.eucm.ead.editor.model.nodes;

import es.eucm.ead.editor.R;
import es.eucm.ead.editor.model.EditorModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * This node represents a query.
 * It is not persisted as an "editor-node", as it can be regenerated on-demand
 * by repeating the query.
 *
 * @author mfreire
 */
public class LogNode extends EditorNode {

	private static Logger logger = LoggerFactory.getLogger(LogNode.class);

	public LogNode(int id) {
		super(id);
	}

	@Override
	public String getLinkIcon() {
		return R.Drawable.assets__log_png;
	}

	/**
	 * Writes inner contents to an XML snippet.
	 * @param sb
	 */
	@Override
	public void writeInner(StringBuilder sb) {
	}

	/**
	 * Reads inner contents from an XML snippet.
	 * @param e
	 */
	@Override
	public void restoreInner(Element e, EditorModel em) {
	}

	/**
	 * Generates a one-line description with as much information as possible.
	 * @return a human-readable description of this node
	 */
	@Override
	public String getTextualDescription(EditorModel m) {
		return "Message log";
	}
}

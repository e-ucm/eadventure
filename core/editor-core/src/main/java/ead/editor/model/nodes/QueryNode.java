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
package ead.editor.model.nodes;

import ead.editor.model.EditorModel;
import ead.editor.model.nodes.EditorNode;
import ead.editor.model.nodes.DependencyNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 *
 * @author mfreire
 */
public class QueryNode extends EditorNode {

	private static final Logger logger = LoggerFactory.getLogger("QueryNode");

	private String queryString;

	public QueryNode(EditorModel m, String queryString) {
		super(m.generateId());
		this.queryString = queryString;
		logger.debug("Query node for '{}'", queryString);

		for (DependencyNode d : m.searchAll(queryString)) {
			logger.debug("\tadding query match: {}", d.getId());
			this.addChild(d);
		}

        try {
            DependencyNode d = m.getNode(Integer.valueOf(queryString));
			logger.debug("\tadding direct-id match: {}", d.getId());
			this.addChild(d);
		} catch (Exception e) {
            logger.debug("No direct match found for {}", queryString);
        }
	}

	/**
	 * Writes inner contents to an XML snippet.
	 * @param sb
	 */
	@Override
	public void writeInner(StringBuilder sb) {
		sb.append("<queryString>")
				.append(queryString).append("</queryString>");
	}

	/**
	 * Reads inner contents from an XML snippet.
	 * @param e
	 */
	@Override
	public void restoreInner(Element e) {
		this.queryString = e.getChildNodes().item(0).getTextContent();
	}

	/**
	 * Generates a one-line description with as much information as possible.
	 * @return a human-readable description of this node
	 */
	@Override
	public String getTextualDescription(EditorModel m) {
		StringBuilder sb = new StringBuilder();
		sb.append("Query: '").append(queryString).append("'");
		for (DependencyNode n : getContents()) {
			sb.append("\n").append(n.getTextualDescription(m));
		}
		return sb.toString();
	}
}

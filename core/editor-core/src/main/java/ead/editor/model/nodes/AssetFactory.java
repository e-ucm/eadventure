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

package ead.editor.model.nodes;

import ead.common.model.EAdElement;
import ead.common.resources.assets.AssetDescriptor;
import ead.editor.model.EditorAnnotator;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.editor.model.EditorModelImpl;
import java.util.HashSet;

/**
 * A factory that recognizes attrezzo-nodes
 * @author mfreire
 */
public class AssetFactory implements EditorNodeFactory {
	private static final Logger logger = LoggerFactory
			.getLogger("AssetFactory");

	/**
	 * Find and create EditorNodes for actors
	 * @param annotator annotations for nodes (by ID)
	 * @param model where the nodes should be inserted, via registerEditorNode
	 */
	@Override
	public void createNodes(EditorModelImpl model, EditorAnnotator annotator) {

		ArrayList<EditorNode> newNodes = new ArrayList<EditorNode>();

		for (DependencyNode n : model.getNodesById().values()) {
			if (!(n instanceof EngineNode)
					|| !(n.getContent() instanceof AssetDescriptor)) {
				continue;
			}
			EditorNode an = new AssetNode(model.generateId(null));
			n.setManager(an);
			an.addChild(n);
			newNodes.add(an);
		}

		// now, register them
		for (EditorNode en : newNodes) {
			logger.info("Registered {} as an AssetNode", en.getId());
			model.registerEditorNodeWithGraph(en);
		}
	}
}

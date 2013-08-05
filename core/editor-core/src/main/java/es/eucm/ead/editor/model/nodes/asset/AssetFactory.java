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

package es.eucm.ead.editor.model.nodes.asset;

import java.util.ArrayList;
import java.util.List;

import es.eucm.ead.editor.model.EditorAnnotator;
import es.eucm.ead.editor.model.EditorModelImpl;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.EditorNodeFactory;
import es.eucm.ead.editor.model.nodes.EngineNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.Frame;
import es.eucm.ead.model.assets.multimedia.Video;
import es.eucm.ead.editor.model.nodes.EditorNode;

/**
 * A factory that creates asset nodes
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
		ArrayList<DependencyNode> frames = new ArrayList<DependencyNode>();

		for (DependencyNode n : model.getNodesById().values()) {
			if (!(n instanceof EngineNode)
					|| !(n.getContent() instanceof AssetDescriptor)) {
				continue;
			}

			AssetDescriptor d = (AssetDescriptor) n.getContent();
			EditorNode an;
			if (d instanceof Frame) {
				frames.add(n);
				continue;
			} else if (d instanceof Image) {
				an = new ImageAssetNode(model.generateId(null));
			} else if (d instanceof Video) {
				an = new VideoAssetNode(model.generateId(null));
			} else if (d instanceof Caption) {
				an = new CaptionAssetNode(model.generateId(null));
			} else {
				an = new AssetNode(model.generateId(null));
			}

			n.setManager(an);
			an.addChild(n);
			newNodes.add(an);
		}

		// now, register them
		for (EditorNode en : newNodes) {
			logger
					.info(
							"Registered {} as asset-node of type {} (first entry is {})",
							new Object[] { en.getId(),
									en.getClass().getSimpleName(),
									en.getFirst().getId() });
			model.registerEditorNodeWithGraph(en);
		}

		// and resolve frames to their rightful parents
		for (DependencyNode f : frames) {
			List<DependencyNode> ins = model.incomingDependencies(f);
			if (ins.size() != 1) {
				logger
						.warn(
								"Expected exactly 1 incoming dep. into frame {}, got {}",
								new Object[] { f.getId(), ins.size() });
				continue;
			} else {
				DependencyNode first = ins.iterator().next();
				logger.debug("Associated {} with {}, managed by {}",
						new Object[] { f, first, first.getManager() });
				f.setManager(first.getManager());
			}
		}
	}
}

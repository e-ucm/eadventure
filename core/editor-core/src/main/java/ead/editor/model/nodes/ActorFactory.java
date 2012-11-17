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
import ead.editor.model.EditorAnnotator;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.editor.model.EditorModelImpl;

/**
 * A factory that recognizes attrezzo-nodes
 * @author mfreire
 */
public class ActorFactory implements EditorNodeFactory {
    private static final Logger logger = LoggerFactory.getLogger("ActorFactory");

    /**
     * Find and create EditorNodes for actors
     * @param annotator annotations for nodes (by ID)
     * @param model where the nodes should be inserted, via registerEditorNode
     */
    @Override
    public void createNodes(EditorModelImpl model, EditorAnnotator annotator) {

        ArrayList<EditorNode> newNodes = new ArrayList<EditorNode>();

        for (DependencyNode n : model.getNodesById().values()) {
            if ( ! (n instanceof EngineNode)
                    || ! (n.getContent() instanceof EAdElement)) {
                continue;
            }
            EAdElement e = (EAdElement)n.getContent();
            for (EditorAnnotator.Annotation a : annotator.get(e)) {
                if (a.getKey().equals("type") && a.getValue().equals("actor")) {
                    ActorNode an = new ActorNode(model.generateId(null));
                    an.addChild(n);
                    logger.debug("A star is born! actor {} {}", new Object[] {an.getId(), an.getTextualDescription(model)});
//                    for (DependencyEdge de : g.outgoingEdgesOf(n)) {
//                        logger.debug("Outgoing dep: {}",
//                                g.getEdgeTarget(de).getTextualDescription(model));
//                    }
                    newNodes.add(an);
                    logger.debug("The star is up in the sky...");
                    break;
                }
            }
        }
        // now, register them
        for (EditorNode en : newNodes) {
            model.registerEditorNodeWithGraph(en);
        }
    }
}

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
package ead.editor.model;

import java.util.TreeMap;
import org.jgrapht.graph.ListenableDirectedGraph;

/**
 * Recognizes a given pattern in an imported course, and builds all
 * necessary EditorNodes to represent it.
 * @author mfreire
 */
public interface EditorNodeFactory {

    /**
     * Create & register editorNodes with the model
     * @param g node graph; may contain no EditorNodes
     * @param annotator annotations for nodes (by ID)
     * @param nodesById nodes in graph, by ID
     * @param model where the nodes should be inserted, via registerEditorNode
     */
    public void createNodes(
            ListenableDirectedGraph<DependencyNode, DependencyEdge> g,
            EditorAnnotator importAnnotatorE,
            TreeMap<Integer, DependencyNode> nodesById,
            EditorModel model);
}

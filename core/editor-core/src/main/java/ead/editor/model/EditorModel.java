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

import java.util.HashMap;
import java.util.List;

import ead.common.model.elements.EAdAdventureModel;
import ead.editor.EditorStringHandler;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.view.dock.ModelAccessor;

/**
 * Contains a full model of what is being edited. This is a super-set of an
 * EAdAdventureModel, encompassing both engine-related model objects and
 * resources, assets, and strings. Everything is searchable, and dependencies
 * are tracked as objects are changed.
 *
 * @author mfreire
 */
public interface EditorModel extends ModelAccessor {

	// -------- nodes

	DependencyNode getNode(int id);

	DependencyNode getNodeFor(Object content);

	int getEditorId(Object o);

	int generateId(Object targetObject);

	List<DependencyNode> incomingDependencies(DependencyNode node);

	List<DependencyNode> outgoingDependencies(DependencyNode node);

	// -------- search

	ModelIndex.SearchResult searchAllDetailed(String queryText);

	List<DependencyNode> searchAll(String queryText);

	List<DependencyNode> search(String field, String queryText);

	// -------- saving, loading, and engine access

	EAdAdventureModel getEngineModel();

	EditorStringHandler getStringHandler();

	HashMap<String, String> getEngineProperties();

	EditorModelLoader getLoader();

	// -------- progress updates (when saving, loading, ...)

	void addProgressListener(ModelProgressListener progressListener);

	void removeProgressListener(ModelProgressListener progressListener);

	/**
	 * A very simple interface for progress updates
	 */
	public static interface ModelProgressListener {
		/**
		 * Called whenever progress is made.
		 * @param progress from 0 to 100
		 * @param text to display regarding progress
		 */
		public void update(int progress, String text);
	}
}
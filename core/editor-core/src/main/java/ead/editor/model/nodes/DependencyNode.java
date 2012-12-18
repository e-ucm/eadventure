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

import org.apache.lucene.document.Document;

import ead.editor.R;
import ead.editor.model.EditorModel;
import ead.editor.model.ModelIndex;

/**
 * The editor uses these nodes to encapsulate actual model objects, be they
 * Resources or EAdElements. The nodes are expected to be collected into
 * a large model graph, and must have a model-wide unique id.
 *
 * @author mfreire
 */
public abstract class DependencyNode<T> implements
		Comparable<DependencyNode<T>> {
	private int id;
	protected T content;
	private Document doc;
	private DependencyNode manager;

	public DependencyNode(int id, T content) {
		this.id = id;
		this.content = content;
		this.doc = new Document();
	}

	public void setManager(DependencyNode manager) {
		this.manager = manager;
	}

	public boolean isManaged() {
		return manager != null;
	}

	public DependencyNode getManager() {
		return manager;
	}

	public String getLinkText() {
		return "" + id;
	}

	public String getLinkIcon() {
		return R.Drawable.assets__engine_png;
	}

	public T getContent() {
		return content;
	}

	public Document getDoc() {
		return doc;
	}

	/**
	 * clears all fields except editor-id
	 */
	public void clearDoc() {
		doc = new Document();
		ModelIndex.addProperty(this, ModelIndex.editorIdFieldName, "" + id,
				false);
	}

	public void setContent(T content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		if (this.id == -1) {
			this.id = id;
		} else {
			throw new IllegalArgumentException(
					"Can only change temporary ids (== -1)");
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || (getClass() != other.getClass())) {
			return false;
		}
		return ((DependencyNode) other).id == id;
	}

	@Override
	public int hashCode() {
		return 23 * this.id + 5;
	}

	/**
	 * Generates a one-line description with as much information as possible.
	 * @return a human-readable description of this node
	 */
	public abstract String getTextualDescription(EditorModel m);

	/**
	 * Compares this node to another one, using IDs as a sorting key
	 * @param other
	 * @return
	 */
	@Override
	public int compareTo(DependencyNode<T> other) {
		return id - other.id;
	}
}

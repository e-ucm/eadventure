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

package es.eucm.ead.editor.model;

import es.eucm.ead.editor.model.nodes.DependencyNode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An implementation of ModelEvent, intended for bundling several
 * ModelEvents together
 *
 * @author mfreire
 */
public class MergeableModelChange implements ModelEvent {

	private static final DependencyNode[] emptyArray = new DependencyNode[0];

	private String name;
	private Object cause;
	private ArrayList<DependencyNode> added = new ArrayList<DependencyNode>();
	private ArrayList<DependencyNode> changed = new ArrayList<DependencyNode>();
	private ArrayList<DependencyNode> removed = new ArrayList<DependencyNode>();
	private boolean dirty = false;
	private DependencyNode[] addedArray, changedArray, removedArray;

	public MergeableModelChange(String name, Object cause) {
		this.name = name;
		this.cause = cause;
	}

	private void accumulate(ArrayList<DependencyNode> target,
			DependencyNode[] source) {
		if (source.length > 0) {
			target.addAll(Arrays.asList(source));
			dirty = true;
		}
	}

	/**
	 * Must be called after the last merge
	 */
	public void commit() {
		if (dirty) {
			addedArray = added.toArray(new DependencyNode[added.size()]);
			removedArray = removed.toArray(new DependencyNode[removed.size()]);
			changedArray = changed.toArray(new DependencyNode[changed.size()]);
			Arrays.sort(addedArray);
			Arrays.sort(removedArray);
			Arrays.sort(changedArray);
			dirty = false;
		}
	}

	public void merge(ModelEvent me) {
		accumulate(added, me.getAdded());
		accumulate(removed, me.getRemoved());
		accumulate(changed, me.getChanged());
		dirty = true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DependencyNode[] getAdded() {
		return addedArray;
	}

	@Override
	public DependencyNode[] getRemoved() {
		return removedArray;
	}

	@Override
	public DependencyNode[] getChanged() {
		return changedArray;
	}

	@Override
	public Object getCause() {
		return cause;
	}

	@Override
	public String toString() {
		return "ModelChange{" + "name=" + name + ", cause=" + cause
				+ ", added=" + getAdded() + ", changed=" + getChanged()
				+ ", removed=" + getRemoved() + '}';
	}
}

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

import ead.editor.model.nodes.DependencyNode;
import java.util.Arrays;

/**
 * An implementation of ModelEvent, heavy on the "ease of use" philosophy.
 *
 * @author mfreire
 */
public class DefaultModelEvent implements ModelEvent {

	private static final DependencyNode[] emptyArray = new DependencyNode[0];

	private String name;
	private Object cause;
	private DependencyNode[] added;
	private DependencyNode[] changed;
	private DependencyNode[] removed;

	public DefaultModelEvent(String name, Object cause, DependencyNode[] added,
			DependencyNode[] removed, DependencyNode... changed) {
		this.name = name;
		this.cause = cause;
		this.added = (added == null ? emptyArray : sorted(added));
		this.removed = (removed == null ? emptyArray : sorted(removed));
		this.changed = (changed == null ? emptyArray : sorted(changed));
	}

	private DependencyNode[] sorted(DependencyNode[] nodes) {
		if (nodes.length > 0) {
			Arrays.sort(nodes);
		}
		return nodes;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DependencyNode[] getAdded() {
		return added;
	}

	@Override
	public DependencyNode[] getRemoved() {
		return removed;
	}

	@Override
	public DependencyNode[] getChanged() {
		return changed;
	}

	@Override
	public Object getCause() {
		return cause;
	}

	/**
	 * Allows reuse of the same ModelChange for the original and the undo.
	 * Simply swaps 'added' and 'removed'
	 */
	public void flip() {
		DependencyNode[] aux;
		aux = added;
		added = removed;
		removed = aux;
	}

	@Override
	public String toString() {
		return "ModelChange{" + ModelEventUtils.dump(this) + '}';
	}
}

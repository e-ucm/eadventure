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

import ead.editor.model.EditorModel.ModelEvent;

/**
 * An implementation of ModelEvent, heavy on the "ease of use" philosophy.
 *
 * @author mfreire
 */
public class DefaultModelChange implements ModelEvent {

	private static final DependencyNode[] emptyArray = new DependencyNode[0];

	private String name;
	private Object cause;
	private DependencyNode[] added;
	private DependencyNode[] changed;
	private DependencyNode[] removed;

	public DefaultModelChange(String name, Object cause,
			DependencyNode[] added, DependencyNode[] removed,
			DependencyNode... changed) {
		this.name = name;
		this.cause = cause;
		this.added = (added == null ? emptyArray : sorted(added));
		this.removed = (removed == null ? emptyArray : sorted(removed));
		this.changed = sorted(changed);
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

	private static void append(StringBuilder sb, DependencyNode[] nodes) {
		sb.append('[');
		for (DependencyNode dn : nodes) {
			sb.append(dn.getId()).append(' ');
		}
		sb.append(']');
	}

	@Override
	public String toString() {
		return "ModelChange{" + dump(this) + '}';
	}

	/**
	 * Utility method to show an event in full glory
	 */
	public static String dump(ModelEvent me) {
		StringBuilder sb = new StringBuilder();
		sb.append(me.getName()).append(" (cause=").append(me.getCause())
				.append(")");
		sb.append(" change=");
		append(sb, me.getChanged());
		sb.append(" add=");
		append(sb, me.getAdded());
		sb.append(" remove=");
		append(sb, me.getAdded());
		return sb.toString();
	}

	/**
	 * Utility method to check whether a value is in an array. Note: uses
	 * binary search to greatly speed this up if there are many values. 
	 * Added, changed and removed must always be sorted.
	 * @param nodes
	 * @param value
	 * @return 
	 */
	public static boolean contains(DependencyNode[] nodes, DependencyNode value) {
		return Arrays.binarySearch(nodes, value) >= 0;
	}

	/**
	 * Utility method to check whether a value is changed in an event.
	 * @param e the ModelEvent to scourge
	 * @param value
	 * @return 
	 */
	public static boolean changes(ModelEvent e, DependencyNode value) {
		return contains(e.getChanged(), value);
	}
}

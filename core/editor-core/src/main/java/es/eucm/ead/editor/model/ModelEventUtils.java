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
import java.util.Arrays;

/**
 * ModelEvent processing utilities.
 *
 * @author mfreire
 */
public class ModelEventUtils {

	public static void appendIds(StringBuilder sb, DependencyNode[] nodes) {
		sb.append('[');
		for (DependencyNode dn : nodes) {
			sb.append(dn.getId()).append(' ');
		}
		sb.append(']');
	}

	/**
	 * Utility method to show an event in full glory
	 */
	public static String dump(ModelEvent me) {
		StringBuilder sb = new StringBuilder();
		sb.append(me.getName()).append(" (cause=").append(me.getCause())
				.append(")");
		sb.append(" change=");
		appendIds(sb, me.getChanged());
		sb.append(" add=");
		appendIds(sb, me.getAdded());
		sb.append(" remove=");
		appendIds(sb, me.getAdded());
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
	public static boolean contains(DependencyNode[] nodes,
			DependencyNode... values) {
		if (values == null) {
			return false;
		}
		for (DependencyNode node : values) {
			if (Arrays.binarySearch(nodes, node) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Utility method to check whether a value is changed in an event.
	 * @param e the ModelEvent to scourge
	 * @param value
	 * @return
	 */
	public static boolean changes(ModelEvent e, DependencyNode... values) {
		return contains(e.getChanged(), values);
	}
}

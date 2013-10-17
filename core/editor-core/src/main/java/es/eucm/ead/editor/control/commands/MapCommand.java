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

package es.eucm.ead.editor.control.commands;

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.model.DefaultModelEvent;
import es.eucm.ead.editor.model.EditorModel;
import es.eucm.ead.editor.model.ModelEvent;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.model.elements.extra.EAdMap;

/**
 * Contains subclasses for adding to, removing from, and reordering elements in
 * lists.
 * 
 * FIXME: currently does not update graph dependencies.
 */
public abstract class MapCommand<K, V> extends Command {

	protected String commandName;

	/**
	 * The map in which the elements will be placed.
	 */
	protected EAdMap<K, V> elementMap;

	/**
	 * The element to be added to the list.
	 */
	protected V anElement;

	protected DependencyNode[] changed;

	protected K oldKey;
	protected K newKey;

	/**
	 * Constructor for the MapCommand class.
	 * @param map
	 * @param value
	 * @param oldKey
	 * @param newKey
	 * @param changed
	 */
	protected MapCommand(EAdMap<K, V> map, V value, K oldKey, K newKey,
			DependencyNode... changed) {
		this.elementMap = map;
		this.anElement = value;
		this.oldKey = oldKey;
		this.newKey = newKey;
		this.changed = changed;
	}

	protected ModelEvent put(EditorModel em, K key) {
		elementMap.put(key, anElement);
		return new DefaultModelEvent(commandName, this, null, null, changed);
	}

	protected ModelEvent remove(EditorModel em, K key) {
		elementMap.remove(key);
		return new DefaultModelEvent(commandName, this, null, null, changed);
	}

	protected ModelEvent reorder(EditorModel em, K from, K to) {
		elementMap.remove(from);
		elementMap.put(to, anElement);
		return new DefaultModelEvent(commandName, this, null, null, changed);
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	@Override
	public boolean combine(Command other) {
		return false;
	}

	@Override
	public String toString() {
		return commandName + ": at '" + elementMap + "' with '" + anElement
				+ "'";
	}

	/**
	 * adds the element (element must NOT exist; position argument optional)
	 */
	public static class AddToMap<K, V> extends MapCommand<K, V> {

		public AddToMap(EAdMap<K, V> map, V e, K key, DependencyNode... changed) {
			super(map, e, null, key, changed);
			commandName = "AddToMap";
		}

		@Override
		public ModelEvent performCommand(EditorModel em) {
			return redoCommand(em);
		}

		@Override
		public ModelEvent undoCommand(EditorModel em) {
			return remove(em, newKey);
		}

		@Override
		public ModelEvent redoCommand(EditorModel em) {
			return put(em, newKey);
		}
	}

	/**
	 * removes the element (element MUST exist)
	 */
	public static class RemoveFromMap<K, V> extends MapCommand<K, V> {

		public RemoveFromMap(EAdMap<K, V> map, K key, DependencyNode... changed) {
			super(map, map.get(key), null, key, changed);
			commandName = "RemoveFromMap";
		}

		@Override
		public ModelEvent performCommand(EditorModel em) {
			return redoCommand(em);
		}

		@Override
		public ModelEvent undoCommand(EditorModel em) {
			return put(em, oldKey);
		}

		@Override
		public ModelEvent redoCommand(EditorModel em) {
			return remove(em, oldKey);
		}
	}

	/** reorders the element (element MUST exist; MUST give position argument) */
	public static class ChangeKeyInMap<K, V> extends MapCommand<K, V> {

		public ChangeKeyInMap(EAdMap<K, V> map, K from, K to,
				DependencyNode... changed) {
			super(map, map.get(from), from, to, changed);
			commandName = "ChangeKeyInMap";
		}

		@Override
		public ModelEvent performCommand(EditorModel em) {
			return redoCommand(em);
		}

		@Override
		public ModelEvent undoCommand(EditorModel em) {
			return reorder(em, newKey, oldKey);
		}

		@Override
		public ModelEvent redoCommand(EditorModel em) {
			return reorder(em, oldKey, newKey);
		}
	}
}

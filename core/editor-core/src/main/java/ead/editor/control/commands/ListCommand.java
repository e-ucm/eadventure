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

package ead.editor.control.commands;

import ead.common.model.elements.extra.EAdList;
import ead.editor.control.Command;
import ead.editor.model.DefaultModelEvent;
import ead.editor.model.EditorModel;
import ead.editor.model.EditorModel.ModelEvent;
import ead.editor.model.nodes.DependencyNode;

/**
 * Contains subclasses for adding to, removing from, and reordering elements in lists.
 * 
 * FIXME: currently does not update graph dependencies.
 */
public abstract class ListCommand<P> extends Command {

	protected String commandName;

	/**
	 * The list in which the added elements will be placed.
	 */
	protected EAdList<P> elementList;

	/**
	 * The element to be added to the list.
	 */
	protected P anElement;

	protected DependencyNode listNode;

	protected int oldPos;
	protected int newPos;

	/**
	 * Constructor for the ListCommand class.
	 * @param list The EAdList in which the command is to be applied
	 * @param e The P element to be added to a list by the command
	 */
	protected ListCommand(EAdList<P> list, P e, DependencyNode listNode,
			int oldPos, int newPos) {
		this.elementList = list;
		this.anElement = e;
		this.listNode = listNode;
		this.oldPos = oldPos;
		this.newPos = newPos;
	}

	protected ModelEvent add(EditorModel em, int position) {
		elementList.add(anElement, position);
		return new DefaultModelEvent(commandName, this, null, null, listNode);
	}

	protected ModelEvent remove(EditorModel em, int position) {
		elementList.remove(position);
		return new DefaultModelEvent(commandName, this, null, null, listNode);
	}

	protected ModelEvent reorder(EditorModel em, int from, int to) {
		elementList.remove(from);
		elementList.add(anElement, to);
		return new DefaultModelEvent(commandName, this, null, null, listNode);
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
		return commandName + ": at '" + elementList + "' with '" + anElement
				+ "'";
	}

	/** 
	 * adds the element (element must NOT exist; position argument optional) 
	 */
	public static class AddToList<P> extends ListCommand<P> {

		public AddToList(EAdList<P> list, P e, int newPos,
				DependencyNode listNode) {
			super(list, e, listNode, -1, newPos);
			commandName = "AddToList";
		}

		public AddToList(EAdList<P> list, P e, DependencyNode listNode) {
			this(list, e, list.size(), listNode);
		}

		@Override
		public ModelEvent performCommand(EditorModel em) {
			return redoCommand(em);
		}

		@Override
		public ModelEvent undoCommand(EditorModel em) {
			return remove(em, newPos);
		}

		@Override
		public ModelEvent redoCommand(EditorModel em) {
			return add(em, newPos);
		}
	}

	/** 
	 * removes the element (element MUST exist) 
	 */
	public static class RemoveFromList<P> extends ListCommand<P> {

		public RemoveFromList(EAdList<P> list, P e, DependencyNode listNode) {
			super(list, e, listNode, -1, -1);
			commandName = "RemoveFromList";
		}

		@Override
		public ModelEvent performCommand(EditorModel em) {
			return redoCommand(em);
		}

		@Override
		public ModelEvent undoCommand(EditorModel em) {
			return add(em, oldPos);
		}

		@Override
		public ModelEvent redoCommand(EditorModel em) {
			oldPos = elementList.indexOf(anElement);
			return remove(em, oldPos);
		}
	}

	/** reorders the element (element MUST exist; MUST give position argument) */
	public static class ReorderInList<P> extends ListCommand<P> {

		public ReorderInList(EAdList<P> list, P e, int from, int to,
				DependencyNode listNode) {
			super(list, e, listNode, from, to);
			commandName = "ReorderInList";
		}

		public ReorderInList(EAdList<P> list, P e, int to,
				DependencyNode listNode) {
			super(list, e, listNode, -1, to);
			commandName = "ReorderInList";
		}

		@Override
		public ModelEvent performCommand(EditorModel em) {
			return redoCommand(em);
		}

		@Override
		public ModelEvent undoCommand(EditorModel em) {
			return reorder(em, newPos, oldPos);
		}

		@Override
		public ModelEvent redoCommand(EditorModel em) {
			oldPos = (oldPos == -1) ? elementList.indexOf(anElement) : oldPos;
			return reorder(em, oldPos, newPos);
		}
	}
}

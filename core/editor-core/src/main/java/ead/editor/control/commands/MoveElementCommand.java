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

/**
 * Class that represents the generic command that moves an element in an
 * {@link EAdList}.
 */
public class MoveElementCommand<P> extends Command {

	/**
	 * The list in which the specified elements will be moved.
	 */
	private EAdList<P> elementList;

	/**
	 * The element to be moved in the list.
	 */
	private P anElement;

	/**
	 * The index in the list where the element is to be moved
	 */
	private int newIndex;

	/**
	 * The index in the list that the element had before being moved
	 */
	private int oldIndex;

	/**
	 * Constructor for the MoveElementCommand class.
	 * 
	 * @param list
	 *            The EAdElementList in which the command is to be applied
	 * @param e
	 *            The P element to be moved in a list by the command
	 * @param index
	 *            The index in the list where the element is to be moved
	 * 
	 */
	public MoveElementCommand(EAdList<P> list, P e, int ind) {
		this.elementList = list;
		this.anElement = e;
		this.newIndex = ind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#performCommand()
	 */
	@Override
	public boolean performCommand() {
		if (elementList.contains(anElement)) {
			oldIndex = elementList.indexOf(anElement);
			elementList.remove(anElement);
			elementList.add(anElement, newIndex);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#undoCommand()
	 */
	@Override
	public boolean undoCommand() {

		elementList.remove(anElement);
		elementList.add(anElement, oldIndex);
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#canRedo()
	 */
	@Override
	public boolean canRedo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#redoCommand()
	 */
	@Override
	public boolean redoCommand() {
		if (elementList.contains(anElement)) {
			oldIndex = elementList.indexOf(anElement);
			elementList.remove(anElement);
			elementList.add(anElement, newIndex);
			return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.editor.control.Command#combine(es.eucm.eadventure.
	 * editor.control.Command)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean combine(Command other) {
		if (other instanceof MoveElementCommand) {
			MoveElementCommand<P> cnt = (MoveElementCommand<P>) other;
			if (cnt.elementList.equals(this.elementList)
					&& cnt.anElement.equals(this.anElement)) {
				this.newIndex = cnt.newIndex;
				this.timeStamp = cnt.timeStamp;
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the index in the list where the element is to be moved
	 */
	public int getNewIndex() {
		return newIndex;
	}

	/**
	 * Returns the index in the list that the element had before being moved
	 */
	public int getOldIndex() {
		return oldIndex;
	}

}

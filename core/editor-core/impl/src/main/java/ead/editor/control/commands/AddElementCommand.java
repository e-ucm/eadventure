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
 * Class that represents the generic command that adds an element to an
 * {@link EAdList}.
 */
public class AddElementCommand<P> extends Command {

	/**
	 * The list in which the added elements will be placed.
	 */
	private EAdList<P> elementList;

	/**
	 * The element to be added to the list.
	 */
	private P anElement;

	/**
	 * Constructor for the AddElementCommand class.
	 * 
	 * @param list
	 *            The EAdElementList in which the command is to be applied
	 * @param e
	 *            The P element to be added to a list by the command
	 * 
	 */
	public AddElementCommand(EAdList<P> list, P e) {
		this.elementList = list;
		this.anElement = e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#performCommand()
	 */
	@Override
	public boolean performCommand() {
		elementList.add(anElement);
		return true;
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
		elementList.add(anElement);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.editor.control.Command#combine(es.eucm.eadventure.
	 * editor.control.Command)
	 */
	@Override
	public boolean combine(Command other) {
		// TODO Auto-generated method stub
		return false;
	}

}

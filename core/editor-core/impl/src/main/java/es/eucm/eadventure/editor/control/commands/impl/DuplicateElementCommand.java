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

package es.eucm.eadventure.editor.control.commands.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.editor.control.Command;

public class DuplicateElementCommand<P extends EAdElement> extends Command {
	
	/**
	 * The list in which the duplicated elements will be placed.
	 */
	private EAdList<P> elementList;
	/**
	 * The element to be duplicated
	 */
	private P anElement;
	
	/**
	 * The duplicated element.
	 */
	private P duplicatedElement;
	
	/**
     * Constructor for the AddElementCommand class.
     * 
     * @param list
     *            The EAdElementList in which the command is to be applied 
     * @param e
     *            The P element to be added to a list by the command
     *
     */
	public DuplicateElementCommand(EAdList<P> list, P e) {
		this.elementList = list;
		this.anElement = e;		
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean performCommand() {
		
		if (elementList.contains(anElement)){
			duplicatedElement = (P) anElement.copy();
			elementList.add(duplicatedElement);
			return true;
			}
		return false;
	}

	@Override
	public boolean canUndo() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean undoCommand() {
		// TODO Auto-generated method stub
		if (elementList.contains(duplicatedElement)){
			elementList.remove(duplicatedElement);
			return true;
		}
		return false;
	}

	@Override
	public boolean canRedo() {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean redoCommand() {

		if (elementList.contains(anElement)){
			duplicatedElement = (P) anElement.copy();
			elementList.add(duplicatedElement);
			return true;
			}
		return false;
	}

	@Override
	public boolean combine(Command other) {
		// TODO Auto-generated method stub
		return false;
	}

}

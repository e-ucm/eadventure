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

package ead.editor.control;

import ead.editor.control.change.ChangeEvent;
import ead.editor.control.change.ChangeNotifier;

/**
 * Interface for the management of Commands that modify the editor model.
 */
public interface CommandManager extends ChangeNotifier<ChangeEvent> {

	/**
	 * Perform an command over the game model
	 */
	public void performCommand(Command action);

	/**
	 * Undo the latest command over the game model
	 */
	public void undoCommand();

	/**
	 * Redo the latest undo command over the game model
	 */
	public void redoCommand();

	/**
	 * Returns true if there is an command to redo
	 */
	public boolean canRedo();

	/**
	 * Returns true if there is an command to undo
	 */
	public boolean canUndo();

	/**
	 * @return true if the game model was modified
	 */
	public boolean isChanged();

	/**
	 * Should be called to indicate that the model has been saved.
	 * @param changed
	 */
	public void setChanged();

	/**
	 * Clear the list of commands performed
	 */
	public void clearCommands();

	/**
	 * Add a new stack of commands, used to perform contained tasks
	 * such as those in a modal panel
	 */
	void addStack();

	/**
	 * Remove command stack
	 * 
	 * @param cancelChanges Cancel changes performed on the command stack
	 */
	void removeCommandStacks(boolean cancelChanges);
}

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

import java.util.Arrays;
import java.util.List;

import ead.editor.control.Command;
import java.util.ArrayList;

/**
 * Class that handles multiple commands in a list as a single one
 */
public class CombineCommandList extends Command {

	/**
	 * The list of Command objects to be treated as a single one
	 */
	private List<Command> commandList;

	/**
	 * Constructor for the CombineCommand class.
	 * 
	 * @param list
	 *            The list of Command objects to be treated as a single one
	 * 
	 */
	public CombineCommandList(List<Command> list) {
		this.commandList = list;
	}

	/**
	 * Constructor with variable number of arguments for the CombineCommand
	 * class.
	 * 
	 * @param comms
	 *            The array of Command objects to be treated as a single one
	 * 
	 */
	public CombineCommandList(Command... comms) {
		this.commandList = Arrays.asList(comms);
	}

	/**
	 * Performs this list of commands. Failure in any triggers an 
	 * undo of previously-completed list-commands.
	 */
	@Override
	public boolean performCommand() {		
		ArrayList<Command> done = new ArrayList<Command>();
		for (Command c : commandList) {
			if (c.performCommand()) {
				done.add(c);
			} else {
				for (Command good : done) {
					good.undoCommand();
				}				
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if all commands in the list can be undone
	 */
	@Override
	public boolean canUndo() {
		for (Command c : commandList) {
			if ( ! c.canUndo()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Undoes all commands in this list. Failure to undo any triggers
	 * a redo of previously-completed undos.
	 */
	@Override
	public boolean undoCommand() {
		ArrayList<Command> undone = new ArrayList<Command>();
		for (Command c : commandList) {
			if (c.undoCommand()) {
				undone.add(c);
			} else {
				for (Command good : undone) {
					good.redoCommand();
				}				
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if all commands in the list can be redone
	 */
	@Override
	public boolean canRedo() {
		for (Command c : commandList) {
			if (!c.canRedo()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Repeats all commands in the list
	 */
	@Override
	public boolean redoCommand() {
		ArrayList<Command> redone = new ArrayList<Command>();
		for (Command c : commandList) {
			if (c.redoCommand()) {
				redone.add(c);
			} else {
				for (Command good : redone) {
					good.undoCommand();
				}				
				return false;
			}
		}
		return true;
	}

	/**
	 * Combines this command with another. Essentially a NOP, as we do not
	 * want to combine command-lists
	 */
	@Override
	public boolean combine(Command other) {
		return false;
	}
}

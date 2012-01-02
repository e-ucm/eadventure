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

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#performCommand()
	 */
	@Override
	public boolean performCommand() {
		int c = 0;
		while (c < commandList.size()) {
			if (!commandList.get(c).performCommand()) {
				if (c > 0)
					performCommandFail(c);
				return false;
			}
			c++;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		for (Command c : commandList) {
			if (!c.canUndo())
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#undoCommand()
	 */
	@Override
	public boolean undoCommand() {
		int c = 0;
		while (c < commandList.size()) {
			if (!commandList.get(c).undoCommand()) {
				if (c > 0)
					undoCommandFail(c);
				return false;
			}
			c++;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#canRedo()
	 */
	@Override
	public boolean canRedo() {
		for (Command c : commandList) {
			if (!c.canRedo())
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.control.Command#redoCommand()
	 */
	@Override
	public boolean redoCommand() {
		int c = 0;
		while (c < commandList.size()) {
			if (!commandList.get(c).redoCommand()) {
				if (c > 0)
					redoCommandFail(c);
				return false;
			}
			c++;
		}
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

	private void performCommandFail(int c) {
		for (int i = c - 1; i >= 0; i--)
			commandList.get(i).undoCommand();
	}

	private void undoCommandFail(int c) {
		for (int i = c - 1; i >= 0; i--)
			commandList.get(i).redoCommand();
	}

	private void redoCommandFail(int c) {
		for (int i = c - 1; i >= 0; i--)
			commandList.get(i).undoCommand();
	}

}

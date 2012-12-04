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

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.editor.control.change.ChangeEvent;
import ead.editor.control.commands.CombinedChangeEvent;

/**
 * Stacks of performed and undone actions
 */
public class CommandStack extends Command {

	private static Logger logger = LoggerFactory.getLogger(CommandStack.class);

	/**
	 * Stack of performed actions
	 */
	private Stack<Command> performed;

	/**
	 * Stack of undone actions
	 */
	private Stack<Command> undone;

	/**
	 * The number of actions performed successfully on the model.
	 * Might differ from preformed.size() if there are actions that
	 * cannot be undone.
	 */
	private int actionHistory;

	/**
	 * Default constructor
	 */
	public CommandStack() {
		performed = new Stack<Command>();
		undone = new Stack<Command>();
		actionHistory = 0;
	}

	@Override
	public ChangeEvent performCommand() {
		throw new UnsupportedOperationException("Cannot 'perform' whole stacks");
	}

	@Override
	public boolean canUndo() {
		return actionHistory == performed.size();
	}

	@Override
	public ChangeEvent undoCommand() {
		undone.clear();
		CombinedChangeEvent cce = new CombinedChangeEvent();
		while (!performed.isEmpty()) {
			ChangeEvent ce = performed.peek().undoCommand();
			if (ce != null) {
				cce.add(ce);
			} else {
				logger.error("Error redoing command-stack at {}", performed
						.peek());
			}
			undone.push(performed.pop());
		}
		return cce;
	}

	@Override
	public boolean canRedo() {
		for (Command a : undone) {
			if (!a.canRedo()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ChangeEvent redoCommand() {
		performed.clear();
		CombinedChangeEvent cce = new CombinedChangeEvent();
		while (!undone.isEmpty()) {
			ChangeEvent ce = undone.peek().undoCommand();
			if (ce != null) {
				cce.add(ce);
			} else {
				logger
						.error("Error redoing command-stack at {}", undone
								.peek());
			}
			performed.push(undone.pop());
		}
		return cce;
	}

	@Override
	public boolean combine(Command other) {
		return false;
	}

	/**
	 * @return The stack of performed actions
	 */
	public Stack<Command> getPerformed() {
		return performed;
	}

	/**
	 * @return The stack of undone actions
	 */
	public Stack<Command> getUndone() {
		return undone;
	}

	/**
	 * Increase the action history
	 */
	public void increaseActionHistory() {
		this.actionHistory++;
	}

	/**
	 * Decrease the action history
	 */
	public void decreaseActionHistory() {
		this.actionHistory--;
	}

	/**
	 * @return The action history
	 */
	public int getActionHistory() {
		return actionHistory;
	}

	/**
	 * Clear stacks of performed and undone actions
	 */
	public void clear() {
		performed.clear();
		undone.clear();
	}
}

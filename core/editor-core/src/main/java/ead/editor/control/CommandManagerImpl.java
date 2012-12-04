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
import com.google.inject.Singleton;
import ead.editor.control.change.ChangeEvent;

import ead.editor.control.change.ChangeNotifierImpl;

/**
 * Default implementation of the {@link CommandManager}.
 */
@Singleton
public class CommandManagerImpl extends ChangeNotifierImpl<ChangeEvent>
		implements CommandManager {

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory
			.getLogger(CommandManagerImpl.class);

	/**
	 * Action stacks
	 */
	private Stack<CommandStack> stacks;

	/**
	 * Default constructor
	 */
	public CommandManagerImpl() {
		stacks = new Stack<CommandStack>();
		stacks.push(new CommandStack());
	}

	@Override
	public void addStack() {
		stacks.push(new CommandStack());
		notifyListeners(null);
	}

	@Override
	public void removeCommandStacks(boolean cancelChanges) {
		if (cancelChanges && stacks.peek().canUndo()) {
			stacks.peek().undoCommand();
			stacks.pop();
		} else {
			CommandStack as = stacks.pop();
			if (as.getActionHistory() != 0) {
				stacks.peek().increaseActionHistory();
				if (as.canUndo()) {
					stacks.peek().getPerformed().add(as);
				} else {
					clearCommands();
				}
			}
		}
		notifyListeners(null);
	}

	@Override
	public void performCommand(Command action) {
		logger.debug("performing action: {}", action);
		CommandStack currentStack = stacks.peek();
		ChangeEvent ce = action.performCommand();
		if (ce != null) {
			if (action.canUndo()) {
				currentStack.getPerformed().push(action);
			} else {
				clearCommands();
			}
		} else {
			logger.warn("action returned null: {}", action);
		}
		currentStack.increaseActionHistory();
		//TODO maybe it is worth optimizing so its only called when necessary
		notifyListeners(ce);
	}

	@Override
	public void undoCommand() {
		if (!canUndo()) {
			return;
		}
		CommandStack currentStack = stacks.peek();
		ChangeEvent ce = currentStack.getPerformed().peek().undoCommand();
		if (ce != null) {
			Command action = currentStack.getPerformed().pop();
			if (action.canRedo()) {
				currentStack.getUndone().push(action);
			} else {
				currentStack.getUndone().clear();
			}
		}
		currentStack.decreaseActionHistory();
		//TODO maybe it is worth optimizing so its only called when necessary
		notifyListeners(ce);
	}

	@Override
	public void redoCommand() {
		if (!canRedo()) {
			return;
		}
		CommandStack currentStack = stacks.peek();
		ChangeEvent ce = currentStack.getUndone().peek().redoCommand();
		if (ce != null) {
			Command action = currentStack.getUndone().pop();
			if (action.canUndo()) {
				currentStack.getPerformed().push(action);
			} else {
				clearCommands();
			}
		}
		currentStack.increaseActionHistory();
		//TODO maybe it is worth optimizing so its only called when necessary
		notifyListeners(ce);
	}

	@Override
	public boolean canRedo() {
		if (!stacks.peek().getUndone().empty()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canUndo() {
		if (!stacks.peek().getPerformed().empty()) {
			return true;
		}
		return false;
	}

	private boolean checkChanged() {
		// FIXME - this should take into account setChanged()
		for (CommandStack as : stacks) {
			if (as.getActionHistory() != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isChanged() {
		return checkChanged();
	}

	@Override
	public void clearCommands() {
		for (CommandStack as : stacks) {
			as.clear();
		}
	}

	@Override
	public void setChanged() {
		// FIXME - does nothing
		throw new IllegalArgumentException("Not yet implemented");
	}
}

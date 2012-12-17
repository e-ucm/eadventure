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

import java.util.ArrayList;
import com.google.inject.Singleton;
import ead.editor.control.change.ChangeNotifierImpl;
import ead.editor.model.ModelEvent;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the {@link CommandManager}.
 */
@Singleton
public class CommandManagerImpl extends ChangeNotifierImpl<String> implements
		CommandManager {

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory
			.getLogger(CommandManagerImpl.class.getSimpleName());

	/**
	 * Action stacks
	 */
	private Stack<CommandStack> stacks;

	/**
	 * Parent controller
	 */
	private Controller controller;

	/**
	 * When this says 'clean', then saving should be useless; queried via
	 * setSaved and isChanged
	 */
	private DirtyTracker dirtyTracker = new DirtyTracker();

	/**
	 * Default constructor
	 */
	public CommandManagerImpl() {
		stacks = new Stack<CommandStack>();
		stacks.push(new CommandStack());
	}

	/*
	 * Set the controller
	 */
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void addStack() {
		stacks.push(new CommandStack());
		notifyListeners(null);
	}

	@Override
	public void removeCommandStacks(boolean cancelChanges) {
		if (cancelChanges && stacks.peek().canUndo()) {
			stacks.peek().undoCommand(controller.getModel());
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
		logger.debug("performing: {}", action);
		CommandStack currentStack = stacks.peek();
		ModelEvent me = action.performCommand(controller.getModel());
		if (me != null) {
			// once you do something, you can no longer redo what you had undone
			currentStack.getUndone().clear();

			if (action.canUndo()) {
				if (currentStack.getPerformed().isEmpty()
						|| !currentStack.getPerformed().peek().combine(action)) {
					currentStack.getPerformed().push(action);
				}
			} else {
				clearCommands();
			}
			controller.getModel().fireModelEvent(me);
		} else {
			logger.warn("action returned null: {}", action);
		}
		currentStack.increaseActionHistory();
		notifyListeners("Performed: " + action.toString());
	}

	@Override
	public void undoCommand() {
		if (!canUndo()) {
			return;
		}
		CommandStack currentStack = stacks.peek();
		Command action = currentStack.getPerformed().peek();
		logger.debug("undoing: {}", action);
		ModelEvent me = action.undoCommand(controller.getModel());
		if (me != null) {
			action = currentStack.getPerformed().pop();
			if (action.canRedo()) {
				currentStack.getUndone().push(action);
			} else {
				currentStack.getUndone().clear();
			}
			controller.getModel().fireModelEvent(me);
		} else {
			logger.warn("action returned null: {}", action);
		}
		currentStack.decreaseActionHistory();
		notifyListeners("Undone: " + action.toString());
	}

	@Override
	public void redoCommand() {
		if (!canRedo()) {
			return;
		}
		CommandStack currentStack = stacks.peek();
		Command action = currentStack.getUndone().peek();
		logger.debug("redoing: {}", action);
		ModelEvent me = action.redoCommand(controller.getModel());
		if (me != null) {
			action = currentStack.getUndone().pop();
			if (action.canUndo()) {
				currentStack.getPerformed().push(action);
			} else {
				clearCommands();
			}
			controller.getModel().fireModelEvent(me);
		} else {
			logger.warn("action returned null: {}", action);
		}
		currentStack.increaseActionHistory();
		notifyListeners("Redone: " + action.toString());
	}

	@Override
	public boolean canRedo() {
		if (!stacks.peek().getUndone().empty()) {
			return stacks.peek().getUndone().peek().canRedo();
		}
		return false;
	}

	@Override
	public boolean canUndo() {
		if (!stacks.peek().getPerformed().empty()) {
			return stacks.peek().getPerformed().peek().canUndo();
		}
		return false;
	}

	@Override
	public boolean isChanged() {
		return !dirtyTracker.isClean();
	}

	@Override
	public void clearCommands() {
		for (CommandStack as : stacks) {
			as.clear();
		}
	}

	@Override
	public void setSaved() {
		dirtyTracker.reset();
	}

	private class DirtyTracker {
		private boolean broken;
		private ArrayList<Command> snapshot = new ArrayList<Command>();

		void reset() {
			broken = false;
			snapshot.clear();
			for (Command c : stacks.peek().getPerformed()) {
				snapshot.add(c);
			}
		}

		boolean isClean() {
			if (broken) {
				return false;
			}
			Stack<Command> performed = stacks.peek().getPerformed();
			if (performed.size() != snapshot.size()) {
				return false;
			}
			for (int i = 0; i < snapshot.size(); i++) {
				if (!performed.get(i).equals(snapshot.get(i))) {
					broken = true;
					return false;
				}
			}
			return true;
		}

		void setDirty() {
			broken = true;
		}
	}
}

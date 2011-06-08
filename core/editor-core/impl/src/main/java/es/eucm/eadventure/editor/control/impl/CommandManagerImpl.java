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

package es.eucm.eadventure.editor.control.impl;

import java.util.Stack;

import com.google.inject.Singleton;

import es.eucm.eadventure.editor.control.Command;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.change.impl.ChangeNotifierImpl;

/**
 * Default implementation of the {@link CommandManager}.
 */
@Singleton
public class CommandManagerImpl extends ChangeNotifierImpl implements CommandManager  {

	/**
	 * Action stacks
	 */
	private Stack<CommandStacks> stacks;
	
	/**
	 * Default constructor
	 */
	public CommandManagerImpl() {
		stacks = new Stack<CommandStacks>();
		stacks.push(new CommandStacks());
	}
	
	@Override
	public void addStack() {
		stacks.push(new CommandStacks());
		processChange();
	}
	
	@Override
	public void removeCommandStacks(boolean cancelChanges) {
		if (cancelChanges && stacks.peek().canUndo()) {
			stacks.peek().undoCommand();
			stacks.pop();
		}
		else {
			CommandStacks as = stacks.pop();
			if (as.getActionHistory() != 0) {
				stacks.peek().increaseActionHistory();
				if (as.canUndo())
					stacks.peek().getPerformed().add(as);
				else
					clearCommands();
			}
		}	
		processChange();
	}
	
	@Override
	public void performCommand(Command action) {
		CommandStacks currentStack = stacks.peek();
		if (action.performCommand()) {
			if (action.canUndo())
				currentStack.getPerformed().push(action);
			else
				clearCommands();
		}
		currentStack.increaseActionHistory();
		//TODO maybe it is worth optimizing so its only called when necessary
		processChange();
	}

	@Override
	public void undoCommand() {
		if (!canUndo())
			return;
		CommandStacks currentStack = stacks.peek();
		if (currentStack.getPerformed().peek().undoCommand()) {
			Command action = currentStack.getPerformed().pop();
			if (action.canRedo())
				currentStack.getUndone().push(action);
			else
				currentStack.getUndone().clear();
		}
		currentStack.decreaseActionHistory();
		//TODO maybe it is worth optimizing so its only called when necessary
		processChange();
	}

	@Override
	public void redoCommand() {
		if (!canRedo())
			return;
		CommandStacks currentStack = stacks.peek();
		if (currentStack.getUndone().peek().redoCommand()) {
			Command action = currentStack.getUndone().pop();
			if (action.canUndo())
				currentStack.getPerformed().push(action);
			else
				clearCommands();
		}		
		currentStack.increaseActionHistory();
		//TODO maybe it is worth optimizing so its only called when necessary
		processChange();
	}

	@Override
	public boolean canRedo() {
		if (!stacks.peek().getUndone().empty())
			return true;
		return false;
	}

	@Override
	public boolean canUndo() {
		if (!stacks.peek().getPerformed().empty())
			return true;
		return false;
	}

	@Override
	public boolean isChanged() {
		for (CommandStacks as : stacks)
			if (as.getActionHistory() != 0)
				return true;
		return false;
	}

	@Override
	public void clearCommands() {
		for (CommandStacks as : stacks)
			as.clear();
	}

}

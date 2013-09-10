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

package es.eucm.ead.editor.control.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.model.DefaultModelEvent;
import es.eucm.ead.editor.model.EditorModel;
import es.eucm.ead.editor.model.ModelEvent;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;

/**
 * A command that changes a field-value. The most common case of command.
 */
public class EmptyCommand<T> extends Command {

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EmptyCommand.class.getSimpleName());

	/**
	 * Node that will be passed in model-events returned by this command
	 */
	protected DependencyNode[] changed;

	/**
	 * Constructor. Little to no overhead.
	 * @param changed nodes that change when this command is done or undone
	 */
	public EmptyCommand(DependencyNode... changed) {
		this.changed = changed;
	}

	/**
	 * Method to perform an empty command. Essentially a "nop".
	 * @param em
	 * @return 
	 */
	@Override
	public ModelEvent performCommand(EditorModel em) {
		return new DefaultModelEvent("Empty", this, null, null, changed);
	}
	
	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	@Override
	public ModelEvent redoCommand(EditorModel em) {
		logger.debug("Redoing: empty");
		return performCommand(em);
	}

	@Override
	public ModelEvent undoCommand(EditorModel em) {
		logger.debug("Undoing: empty");
		return performCommand(em);
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public boolean combine(Command other) {
		if (other instanceof EmptyCommand) {
			EmptyCommand<T> o = (EmptyCommand) other;
			timeStamp = o.timeStamp;
			logger.info("Combined command");
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Empty command";
	}
}

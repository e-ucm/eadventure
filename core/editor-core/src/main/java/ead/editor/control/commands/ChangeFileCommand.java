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

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.editor.control.Command;
import ead.editor.model.EditorModel;
import ead.editor.model.ModelEvent;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.view.generic.accessors.Accessor;

/**
 * A specialized FieldValueCommand that operates on files.
 * Uses a file-cache to allow undo/redo actions on files.
 */
public class ChangeFileCommand extends ChangeFieldCommand<File> {

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ChangeFileCommand.class.getSimpleName());

	public final static String ChangeFile = "ChangeFile";

	protected String oldSourceHash;
	protected String newSourceHash;
	protected String currentHash;

	protected FileCache fileCache;

	/**
	 * Constructor for the ChangeFileCommand class.
	 *
	 * @param newValue
	 *            The new value (T)
	 * @param fieldDescriptor
	 *
	 */
	public ChangeFileCommand(File newSource, Accessor<File> fieldDescriptor,
			FileCache fileCache, DependencyNode... changed) {
		super(newSource, fieldDescriptor, changed);
		this.fileCache = fileCache;
		this.commandName = ChangeFile;
	}

	/**
	 * @param one
	 * @param another
	 * @return true if any change fro one to another
	 */
	@Override
	protected boolean isChange(File one, File another) {
		boolean validOne = (one != null);
		boolean validAnother = (another != null);

		return (validOne != validAnother) || (validOne && validAnother);
		// Notice that we don't care if they are 'equals'
		// -- because the FS may have changed; the fileCache will help us out here
	}

	/**
	 * Method to perform a changing values command
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ModelEvent performCommand(EditorModel em) {
		oldValue = fieldDescriptor.read();
		if (!isChange(oldValue, newValue)) {
			throw new IllegalArgumentException("Refusing to do nothing");
		}
		File toHash = (oldValue != null && oldValue.exists()) ? oldValue : null;
		// if toHash is null, the "empty file hash" will be used instead
		try {
			oldSourceHash = fileCache.saveFile(toHash);
			newSourceHash = fileCache.saveFile(newValue);
		} catch (IOException ioe) {
			logger.error("Could not hash old:{} new:{}", new Object[] {
					oldValue, newValue }, ioe);
			return null;
		}
		currentHash = newSourceHash;
		return setValue(newValue);
	}

	/**
	 * @see es.eucm.eadventure.editor.control.Command#redoCommand()
	 */
	@Override
	public ModelEvent redoCommand(EditorModel em) {
		currentHash = newSourceHash;
		return super.redoCommand(em);
	}

	/**
	 * @see es.eucm.eadventure.editor.control.Command#undoCommand()
	 */
	@Override
	public ModelEvent undoCommand(EditorModel em) {
		currentHash = oldSourceHash;
		return super.undoCommand(em);
	}

	/**
	 * Not combinable by default.
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public boolean combine(Command other) {
		return false;
	}

	/**
	 * To be called by interested (or subclasses).
	 * Causes the last version of the file to be written out into the target.
	 * @param target
	 */
	public void writeFile(File target) {
		try {
			fileCache.restoreFile(currentHash, target);
		} catch (IOException ex) {
			logger.error("Could not write file to '{}'", target, ex);
		}
	}
}

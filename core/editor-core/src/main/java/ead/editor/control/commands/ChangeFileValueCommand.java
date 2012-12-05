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
import ead.editor.control.change.ChangeEvent;
import ead.editor.view.generic.FieldDescriptor;

/**
 * Uses a file-cache to allow undo/redo actions on files.
 */
public class ChangeFileValueCommand extends Command implements ChangeEvent {

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ChangeFileValueCommand.class.getSimpleName());

	protected File oldSource;
	protected String oldSourceHash;

	protected File newSource;
	protected String newSourceHash;

	protected String currentHash;

	protected FieldDescriptor<File> fieldDescriptor;

	protected FileCache fileCache;

	/**
	 * Constructor for the ChangeValueCommand class.
	 *
	 * @param newValue
	 *            The new value (T)
	 * @param fieldDescriptor
	 *
	 */
	public ChangeFileValueCommand(File oldSource, File newSource,
			FieldDescriptor<File> fieldDescriptor, FileCache fileCache) {
		this.oldSource = oldSource;
		this.newSource = newSource;
		this.fieldDescriptor = fieldDescriptor;
		this.fileCache = fileCache;
	}

	/**
	 * Method to perform a changing values command
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ChangeEvent performCommand() {
		oldSource = fieldDescriptor.read();

		if ((newSource != null && oldSource == null)
				|| (newSource == null && oldSource != null)
				|| (newSource != null && oldSource != null)) {
			// Notice that we don't care if they are 'equals'
			// -- because the FS may have changed; the fileCache will help us out here
			try {
				oldSourceHash = (oldSource != null && oldSource.exists()) ? fileCache
						.saveFile(oldSource)
						: fileCache.saveFile(null); // an empty file
				newSourceHash = fileCache.saveFile(newSource);
				currentHash = newSourceHash;
				return setValue(newSource);
			} catch (IOException ex) {
				throw new RuntimeException("Error reading file \"" + newSource
						+ "\"", ex);
			}
		}

		return null;
	}

	@Override
	public boolean hasChanged(FieldDescriptor fd) {
		return fd.equals(fieldDescriptor);
	}

	private ChangeEvent setValue(File value) {
		try {
			fieldDescriptor.write(value);
		} catch (Exception e) {
			throw new RuntimeException("Error writing field "
					+ fieldDescriptor.getFieldName(), e);
		}
		return this;
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.editor.control.Command#redoCommand()
	 */
	@Override
	public ChangeEvent redoCommand() {
		logger.debug("Redoing: setting file to '{}' ({})", newSource,
				newSourceHash);
		currentHash = newSourceHash;
		return setValue(newSource);
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.editor.control.Command#combine(es.eucm.eadventure.editor.control.Command)
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public boolean combine(Command other) {
		return false;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.editor.control.Command#undoCommand()
	 */
	@Override
	public ChangeEvent undoCommand() {
		logger.debug("Undoing: setting file to '{}' ({})", oldSource,
				oldSourceHash);
		currentHash = oldSourceHash;
		return setValue(oldSource);
	}

	public void writeFile(File target) {
		try {
			fileCache.restoreFile(currentHash, target);
		} catch (IOException ex) {
			logger.error("Could not write file to '{}'", target, ex);
		}
	}

	@Override
	public String toString() {
		return "ChangeFile: from '" + oldSource + "' to '" + newSource
				+ "' in " + fieldDescriptor;
	}
}

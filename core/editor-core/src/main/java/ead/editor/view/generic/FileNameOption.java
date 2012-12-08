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

package ead.editor.view.generic;

import ead.editor.view.generic.accessors.Accessor;
import java.io.File;

import ead.editor.model.nodes.DependencyNode;

public class FileNameOption extends TextOption {

	private boolean fileMustExist = false;

	/**
	 * Resolves the actual file this field refers to.
	 * @param value
	 * @return a file built from this value
	 */
	public File resolveFile(String value) {
		return new File(value);
	}

	public FileNameOption(String title, String toolTipText,
			Accessor<String> fieldDescriptor, boolean fileMustExist,
			DependencyNode node) {
		super(title, toolTipText, fieldDescriptor, ExpectedLength.NORMAL, node);
		this.fileMustExist = fileMustExist;
	}

	/**
	 * Should return whether a value is valid or not. Invalid values will
	 * not generate updates, and will therefore not affect either model or other
	 * views.
	 * @param value
	 * @return whether it is valid or not; default is "always-true" 
	 */
	@Override
	protected boolean isValid(String value) {
		File f = resolveFile(value);
		return (fileMustExist && f.exists()) || f.getParentFile().isDirectory();
	}
}

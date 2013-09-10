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

package es.eucm.ead.editor.view.generic;

import java.io.File;

import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.generic.accessors.Accessor;

public class FileNameOption extends TextOption {

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
		validityConstraint.getList().add(fileMustExist ? new FileMustExist() : new ParentPathMustExist());
	}

	public FileNameOption(String title, String toolTipText, Object object,
			String fieldName, boolean fileMustExist, DependencyNode node) {
		super(title, toolTipText, object, fieldName, ExpectedLength.NORMAL,
				node);
		validityConstraint.getList().add(fileMustExist ? new FileMustExist() : new ParentPathMustExist());
	}
	
	public class FileMustExist implements Constraint {
		@Override
		public boolean isValid() {
			return resolveFile(getControlValue()).exists();
		}
		@Override
		public String getTooltip() {
			return (isValid() ? "" : Messages.file_must_exist);
		}
		@Override
		public void validityChanged() {}
	}
	
	public class ParentPathMustExist implements Constraint {
		@Override
		public boolean isValid() {
			return resolveFile(getControlValue()).getParentFile().isDirectory();
		}
		@Override
		public String getTooltip() {
			return (isValid() ? "" : Messages.path_must_exist);
		}
		@Override
		public void validityChanged() {}
	}
}

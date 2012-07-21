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

import ead.common.params.text.EAdString;
import ead.editor.control.Command;
import ead.tools.StringHandler;

/**
 * Command to change the value of a string in the {@link StringHandler}
 * 
 */
public class ChangeEAdStringValueCommand extends Command {

	/**
	 * Current {@link StringHandler}
	 */
	private StringHandler stringHandler;
	
	/**
	 * The string key (allowing for internationalization)
	 */
	private EAdString key;
	
	/**
	 * The string value
	 */
	private String value;

	/**
	 * The old value stored for the string
	 */
	private String oldValue;
	
	public ChangeEAdStringValueCommand(EAdString key, String value, StringHandler stringHandler) {
		this.stringHandler = stringHandler;
		this.key = key;
		this.value = value;
		oldValue = stringHandler.getString(key);
	}
	
	@Override
	public boolean performCommand() {
		stringHandler.setString(key, value);
		return false;
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean undoCommand() {
		stringHandler.setString(key, oldValue);
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	@Override
	public boolean redoCommand() {
		stringHandler.setString(key, value);
		return false;
	}

	@Override
	public boolean combine(Command other) {
		if (other != null && other instanceof ChangeEAdStringValueCommand) {
			oldValue = ((ChangeEAdStringValueCommand) other).oldValue;
			return true;
		}
		return false;
	}

}

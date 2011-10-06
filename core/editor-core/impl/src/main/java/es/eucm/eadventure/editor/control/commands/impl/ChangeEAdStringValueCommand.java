package es.eucm.eadventure.editor.control.commands.impl;

import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.Command;

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

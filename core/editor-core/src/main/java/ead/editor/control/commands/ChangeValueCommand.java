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

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.editor.control.Command;
import ead.editor.control.change.ChangeEvent;
import ead.editor.view.generic.FieldDescriptor;

/**
 * Class that represents the generic command that uses introspection to change T values.
 */
public class ChangeValueCommand<T> extends Command implements ChangeEvent {

	/**
	 * The old value (T) to be changed.
	 */
	protected T oldValue;

	/**
	 * The new value (T) to change.
	 */
	protected T newValue;

	/**
	 * The object which data is to be modified.
	 */
	protected Object data;

	/**
	 * The name of the object data set method.
	 */
	protected String setName;

	/**
	 * The name of the object data get method.
	 */
	protected String getName;

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ChangeValueCommand.class);

	/**
	 * Constructor for the ChangeValueCommand class.
	 * 
	 * @param data
	 *            The object which data is to be modified
	 * @param newValue
	 *            The new value (T)
	 * @param setMethodName
	 *            The name of the object data set method
	 * @param getMethodName
	 *            The name of the object data get method
	 */
	public ChangeValueCommand(Object data, T newValue, String setMethodName,
			String getMethodName) {
		this.data = data;
		this.newValue = newValue;
		this.getName = getMethodName;
		this.setName = setMethodName;
	}

	/**
	 * Constructor for the ChangeValueCommand with fieldName
	 * 
	 * @param data The object where to change the value
	 * @param newValue The new value
	 * @param fieldName The name of the field
	 */
	public ChangeValueCommand(Object data, T newValue, String fieldName) {
		this(data, newValue, fieldName, fieldName);
	}

	/**
	 * Method to obtain the setter of the data object. 
	 */
	public Method getSetMethod() {
		try {
			return data.getClass().getMethod(setName, newValue.getClass());
		} catch (SecurityException e) {
			logger.error("Security exception setting {}", setName, e);
		} catch (NoSuchMethodException e) {
			logger.error("No such set method: {}", setName, e);
		}
		return null;
	}

	/**
	 * Method to obtain the getter of the data object. 
	 */
	public Method getGetMethod() {

		try {
			return data.getClass().getMethod(getName);
		} catch (SecurityException e) {
			logger.error("Security exception getting {}", getName, e);
		} catch (NoSuchMethodException e) {
			logger.error("No such get method: {}", getName, e);
		}
		return null;
	}

	/**
	 * Method to perform a changing values command 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ChangeEvent performCommand() {

		boolean done = false;
		if (getGetMethod() != null && getSetMethod() != null) {
			try {
				oldValue = (T) getGetMethod().invoke(data);
				if ((newValue != null && oldValue == null)
						|| (newValue == null && oldValue != null)
						|| (newValue != null && oldValue != null && !oldValue
								.equals(newValue))) {
					// ok, new != old, and 
					getSetMethod().invoke(data, newValue);
					done = true;
				}
			} catch (Exception e) {
				logger.error("Error performing changValueCommand: {} ",
						setName, e);
			}
		}
		return done ? this : null;
	}

	@Override
	public boolean hasChanged(FieldDescriptor fd) {
		return fd.getElement() == data;
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see es.eucm.eadventure.editor.control.Command#redoCommand()
	 */
	@Override
	public ChangeEvent redoCommand() {

		boolean done = false;
		try {
			getSetMethod().invoke(data, newValue);
			done = true;
		} catch (Exception e) {
			logger.error("Error at redoing Action");
		}
		return this;

	}

	/**
	 * (non-Javadoc)
	 * @see es.eucm.eadventure.editor.control.Command#combine(es.eucm.eadventure.editor.control.Command)
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public boolean combine(Command other) {
		if (other instanceof ChangeValueCommand) {
			ChangeValueCommand<T> cnt = (ChangeValueCommand) other;
			if (cnt.getName.equals(getName) && cnt.setName.equals(setName)
					&& data == cnt.data) {
				newValue = cnt.newValue;
				timeStamp = cnt.timeStamp;
				return true;
			}
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see es.eucm.eadventure.editor.control.Command#undoCommand()
	 */
	@Override
	public ChangeEvent undoCommand() {

		boolean done = false;
		try {
			getSetMethod().invoke(data, oldValue);
			done = true;
		} catch (Exception e) {
			logger.error("Error at redoing Action");
		}
		return this;
	}

	/**
	 * Returns the old value
	 */
	public T getOldValue() {
		return oldValue;
	}

	/**
	 * Returns the new value
	 */
	public T getNewValue() {
		return newValue;
	}
}

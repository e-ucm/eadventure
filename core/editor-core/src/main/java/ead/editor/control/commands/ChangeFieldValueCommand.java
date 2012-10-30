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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ead.editor.control.Command;
import ead.editor.view.generic.FieldDescriptor;

/**
 * Class that represents the generic command that uses introspection to change T values.
 */
public class ChangeFieldValueCommand<T> extends Command {

	/**
	 * The old value (T) to be changed.
	 */
    protected T oldValue;

    /**
	 * The new value (T) to change.
	 */
    protected T newValue;

    protected FieldDescriptor<T> fieldDescriptor;

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ChangeFieldValueCommand.class);

    /**
     * Constructor for the ChangeValueCommand class.
     *
     * @param newValue
     *            The new value (T)
     * @param fieldDescriptor
     *
     */
    public ChangeFieldValueCommand(T newValue, FieldDescriptor<T> fieldDescriptor) {
        this.newValue = newValue;
        this.fieldDescriptor = fieldDescriptor;
    }

    /**
	 * Method to perform a changing values command
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean performCommand() {
		boolean done = false;

		try {
			PropertyDescriptor pd = getPropertyDescriptor(fieldDescriptor.getElement().getClass(), fieldDescriptor.getFieldName());
			Method method = pd.getReadMethod();
			oldValue = (T) method.invoke(fieldDescriptor.getElement());
		} catch (Exception e) {
			throw new RuntimeException("Error reading field \"" + fieldDescriptor.getFieldName() + "\"", e);
		}

        if (newValue != null && oldValue == null || newValue == null && oldValue != null || (newValue != null && oldValue != null && !oldValue.equals(newValue))) {
            done = setValue(newValue);
        }

        return done;
	}

	private boolean setValue(T value) {
		try {
			PropertyDescriptor pd = getPropertyDescriptor(fieldDescriptor.getElement().getClass(), fieldDescriptor.getFieldName());
			pd.getWriteMethod().invoke(fieldDescriptor.getElement(), value);
		} catch (Exception e) {
			throw new RuntimeException("Error reading field " + fieldDescriptor.getFieldName(), e);
		}
		return true;
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
	public boolean redoCommand() {
		return setValue(newValue);
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.editor.control.Command#combine(es.eucm.eadventure.editor.control.Command)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean combine(Command other) {
		if(other instanceof ChangeFieldValueCommand) {
			ChangeFieldValueCommand<T> cnt = (ChangeFieldValueCommand) other;
            if(fieldDescriptor.equals(cnt.fieldDescriptor)) {
                newValue = cnt.newValue;
                timeStamp = cnt.timeStamp;
                logger.info("Combiened command");
                return true;
            }
        }
        return false;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.editor.control.Command#undoCommand()
	 */
	@Override
	public boolean undoCommand() {
		return setValue(oldValue);
	}

	/**
	 * Returns the old value
	 */
	public T getOldValue(){
		return oldValue;
	}

	/**
	 * Returns the new value
	 */
	public T getNewValue(){
		return newValue;
	}

	/**
	 * Utility method to find a property descriptor for a single property
	 *
	 * @param c
	 * @param fieldName
	 * @return
	 */
	private static PropertyDescriptor getPropertyDescriptor(Class<?> c, String fieldName) {
		try {
			for (PropertyDescriptor pd :
				Introspector.getBeanInfo(c).getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					return pd;
				}
			}
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("Could not find getters or setters for field "
					+ fieldName + " in class " + c.getCanonicalName());
		}
		return null;
	}

}

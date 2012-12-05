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

import javax.swing.JComponent;
import ead.editor.control.Command;
import ead.editor.control.CommandManager;
import ead.editor.control.change.ChangeEvent;
import ead.editor.control.change.ChangeListener;
import ead.editor.control.commands.ChangeFieldValueCommand;

/**
 * Abstract implementation for {@link Option}s
 *
 * @param <S>
 *            The type of the option element
 */
public abstract class AbstractOption<S> implements Option<S>,
		ChangeListener<ChangeEvent> {

	/**
	 * Label on the component
	 */
	private String label;
	/**
	 * Tool tip text explanation
	 */
	private String toolTipText;
	/**
	 * Descriptor of the field represented by this option
	 */
	protected FieldDescriptor<S> fieldDescriptor;
	/**
	 * While updating, external updates will be ignored
	 */
	protected boolean isUpdating = false;
	/**
	 * Keeps a copy of the old value
	 */
	protected S oldValue;
	/**
	 * Keeps a reference to the current commandManager
	 */
	protected CommandManager manager;

	/**
	 * @param label
	 *            The label in the option (can be null)
	 * @param toolTipText
	 *            The toolTipText in the option (cannot be null)
	 * @param fieldDescriptor
	 *            The {@link FieldDescriptor} that describes the field in the
	 *            element to be displayed/edited
	 */
	public AbstractOption(String label, String toolTipText,
			FieldDescriptor<S> fieldDescriptor) {
		this.label = label;
		this.toolTipText = toolTipText;
		if (toolTipText == null || toolTipText.isEmpty()) {
			throw new RuntimeException(
					"ToolTipTexts MUST be provided for all interface elements!");
		}
		this.fieldDescriptor = fieldDescriptor;
	}

	/**
	 * Will be called when another command modifies this field.
	 * @param event 
	 */
	@Override
	public void processChange(ChangeEvent event) {
		if (event != null && event.hasChanged(fieldDescriptor)) {
			if (!isUpdating) {
				isUpdating = true;
				setControlValue(fieldDescriptor.read());
				valueUpdated(oldValue);
				isUpdating = false;
			}
			oldValue = getControlValue();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.editor.view.generics.Option#getTitle()
	 */
	@Override
	public String getTitle() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.editor.view.generics.Option#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.editor.view.generics.Option#getFieldDescriptor()
	 */
	@Override
	public FieldDescriptor<S> getFieldDescriptor() {
		return fieldDescriptor;
	}

	/**
	 * Creates the control, setting the initial value. 
	 * Subclasses should register as a listeners
	 * to any changes in the model, and call update() when such changes occur.
	 */
	protected abstract JComponent createControl();

	/**
	 * Creates and initializes the component
	 */
	@Override
	public JComponent getComponent(CommandManager manager) {
		JComponent component = createControl();
		oldValue = getControlValue();
		this.manager = manager;
		manager.addChangeListener(this);
		return component;
	}

	/**
	 * Reads the value of the control.
	 * @return 
	 */
	protected abstract S getControlValue();

	/**
	 * Writes the value of the control.
	 * @return 
	 */
	protected abstract void setControlValue(S newValue);

	/**
	 * Creates a Command that describes a change to the manager
	 * @return 
	 */
	protected Command createUpdateCommand() {
		return new ChangeFieldValueCommand<S>(oldValue, getControlValue(),
				getFieldDescriptor());
	}

	/**
	 * Should return whether a value is valid or not. Invalid values will
	 * not generate updates, and will therefore not affect either model or other
	 * views.
	 * @param value
	 * @return whether it is valid or not; default is "always-true" 
	 */
	protected boolean isValid(S value) {
		return true;
	}

	/**
	 * Should be called when changes to the control are detected
	 */
	protected void update() {
		if (isUpdating || !isValid(getControlValue())) {
			return;
		}

		isUpdating = true;
		manager.performCommand(createUpdateCommand());
		valueUpdated(oldValue);
		oldValue = getControlValue();
		isUpdating = false;
	}

	/**
	 * Called after the control value is updated. Intended to be used by 
	 * subclasses; default implementation is to do nothing.
	 * @param oldValue 
	 */
	public void valueUpdated(S oldValue) {
		// by default, do nothing
	}

	/**
	 * Triggers a manual update. This should be indistinguishable from 
	 * the user typing in stuff directly (if this were a typing-enabled control)
	 * @param nextValue 
	 */
	public void updateValue(S nextValue) {
		if (isUpdating || !isValid(nextValue)) {
			return;
		}

		isUpdating = true;
		setControlValue(nextValue);
		manager.performCommand(createUpdateCommand());
		valueUpdated(oldValue);
		oldValue = getControlValue();
		isUpdating = false;
	}

	@Override
	public void cleanup(CommandManager manager) {
		manager.removeChangeListener(this);
	}
}

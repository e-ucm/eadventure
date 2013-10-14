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

import javax.swing.JComponent;

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.model.ModelEvent;
import es.eucm.ead.editor.model.ModelEventUtils;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.editor.control.CommandManager;
import es.eucm.ead.editor.control.commands.EmptyCommand;
import es.eucm.ead.editor.util.Log4jConfig;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Abstract implementation for {@link Option}s
 *
 * @param <S> type of the option element
 */
public abstract class AbstractOption<S> implements Option<S> {

	static private Logger logger = LoggerFactory
			.getLogger(AbstractOption.class);

	private static enum UpdateType {
		Event, Control, Synthetic
	};

	/**
	 * Label on the component
	 */
	private String label;
	/**
	 * Tool tip text explanation
	 */
	private String toolTipText;
	/**
	 * Last valid status
	 */
	protected boolean currentlyValid = true;
	/**
	 * Validity-checking class. Useful for establishing complex constraints
	 */
	protected CompositeConstraint validityConstraint = new CompositeConstraint();
	/**
	 * While updating, external updates will be ignored
	 */
	protected boolean isUpdating = false;
	/**
	 * A copy of the old value. Used when creating change events / commands,
	 * and generally updated by the AbstractAction itself.
	 */
	protected S oldValue;
	/**
	 * Keeps a reference to the current commandManager
	 */
	protected CommandManager manager;

	/**
	 * A reference to the node to include in 'changed' ModelEvents
	 */
	protected DependencyNode[] changed;

	/**
	 * The default color for invalidness
	 */
	protected static final Color invalidBorderColor = Color.red;

	/**
	 * The default (non-error) border for this control
	 */
	protected Border defaultBorder;

	/**
	 * The returned component
	 */
	protected JComponent component;

	/**
	 * Creates an AbstractAction.
	 * @param label The label in the option (can be null)
	 * @param toolTipText The toolTipText in the option (cannot be null)
	 * @param changed dependency nodes to be considered "changed" when this changes
	 */
	public AbstractOption(String label, String toolTipText,
			DependencyNode... changed) {
		this.label = label;
		this.toolTipText = toolTipText;
		if (toolTipText == null || toolTipText.isEmpty()) {
			throw new RuntimeException(
					"ToolTipTexts MUST be provided for all interface elements!");
		}
		this.changed = changed == null ? new DependencyNode[0] : changed;
	}

	public ArrayList<Constraint> getConstraints() {
		return validityConstraint.getList();
	}

	/**
	 * Will be called when the model changes. Uses changeConsideredRelevant
	 * to avoid acting on non-changes.
	 * @param event
	 */
	@Override
	public void modelChanged(ModelEvent event) {
		if (isUpdating) {
			logger.debug("option {} isUpdating -- ignores change", hashCode());
			return;
		}

		logger.debug("option {} notified of change: {}", new Object[] {
				hashCode(), event });
		if (ModelEventUtils.changes(event, changed)) {
			uncontestedUpdate(readModelValue(), UpdateType.Event);
		} else {
			logger.debug("why am I even receiving this?");
		}
	}

	/**
	 * Reads model value.
	 * @return
	 */
	public abstract S readModelValue();

	/**
	 * Queried within modelChanged before considering a change to
	 * have occurred.
	 * @return
	 */
	protected abstract boolean changeConsideredRelevant(S oldValue, S newValue);

	/**
	 * Retrieves title (used for label).
	 *
	 * @see es.eucm.eadventure.editor.view.generics.Option#getTitle()
	 */
	@Override
	public String getTitle() {
		return label;
	}

	/**
	 * Retrieves tooltip-text (used for tooltips)
	 *
	 * @see es.eucm.eadventure.editor.view.generics.Option#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	/**
	 * Creates the control, setting the initial value.
	 * Subclasses should register as listeners to any changes in the control,
	 * and call update() when such changes occur.
	 */
	protected abstract JComponent createControl();

	/**
	 * Utility method to draw a border around the component
	 */
	protected void decorateComponent() {
		if (currentlyValid) {
			component.setBorder(defaultBorder);
		} else {
			component.setBorder(BorderFactory.createLineBorder(
					invalidBorderColor, 1));
		}
	}

	/**
	 * Creates and initializes the component.
	 * Also sets oldValue for the first time.
	 */
	@Override
	public JComponent getComponent(CommandManager manager) {
		component = createControl();
		defaultBorder = component.getBorder();
		oldValue = getControlValue();
		this.manager = manager;
		return component;
	}

	/**
	 * Reads the value of the control.
	 * @return
	 */
	public abstract S getControlValue();

	/**
	 * Writes the value of the control.
	 * @return
	 */
	protected abstract void setControlValue(S newValue);

	/**
	 * Creates a Command that describes a change to the manager.
	 * No change should be described if no change exists.
	 * @return
	 */
	protected abstract Command createUpdateCommand();

	/**
	 * Should return whether a value is valid or not. Invalid values will
	 * not generate updates, and will therefore not affect either model or other
	 * views.
	 * @param value
	 * @return whether it is valid or not; default is "always-true"
	 */
	protected boolean isValid() {
		return validityConstraint.isValid();
	}

	/**
	 * Set validity. Should be called only from within the 
	 * @param valid 
	 */
	public void refreshValid() {
		currentlyValid = validityConstraint.isValid();
		decorateComponent();
	}

	/**
	 * Should be called when changes to the control are detected.
	 * Updates oldValue after informing all interested parties.
	 * Does nothing if new value is not valid, same as previous, 
	 * or if an update is already under way.
	 */
	protected void update() {
		if (isUpdating) {
			return;
		}
		uncontestedUpdate(getControlValue(), UpdateType.Control);
	}

	/**
	 * Called after the control value is updated. Intended to be used by
	 * subclasses; default implementation is to do nothing. Use to chain
	 * updates for complex models - for example, say that field X, Y and Z
	 * are related, so that X+Y+Z must =10. If X changes, all of them will be
	 * invalid. When all become valid again, valueUpdated would read all 
	 * related fields and call updateValue on each of them.
	 * 
	 * Only called if the update is valid.
	 * 
	 * @param oldValue
	 */
	public void valueUpdated(S oldValue, S newValue) {
		// by default, do nothing
	}

	/**
	 * Triggers a manual update. This should be indistinguishable from
	 * the user typing in stuff directly (if this were a typing-enabled control)
	 * @param nextValue value to set the control to, prior to firing an update
	 */
	public void updateValue(S nextValue) {
		if (isUpdating) {
			return;
		}
		uncontestedUpdate(nextValue, UpdateType.Synthetic);
	}

	/**
	 * synchronizes model values with control values. Called after the control 
	 * has changed due to user (type is Control), or due to programmatic 
	 * set-to-this (type is Synthetic), or due to changed validity constraints
	 * (type is Event).
	 */
	private void uncontestedUpdate(S nextValue, UpdateType type) {
		Log4jConfig.pushNDC(("" + type).charAt(0) + "@"
				+ ("" + this.hashCode()).substring(7));
		if (!isValid()) {
			if (currentlyValid) {
				// add an undoable operation to reset to the previous, valid values
				logger.debug("Notifying of empty command");
				isUpdating = true;
				manager.performCommand(new EmptyCommand(changed));
				isUpdating = false;
			}
			currentlyValid = false;
			validityConstraint.validityChanged();
			logger.debug("Update invalid: {}", nextValue);
			// ignore - non-valid values are not written to the model
		} else if (!changeConsideredRelevant(oldValue, nextValue)) {
			if (!currentlyValid) {
				validityConstraint.validityChanged();
			}
			currentlyValid = true;
			logger.debug("Update is nop");
			// ignore - not a real update
		} else {
			// process update
			if (logger.isDebugEnabled()) {
				logger.debug("Update to {}", nextValue);
			}
			isUpdating = true;
			if (type.equals(UpdateType.Synthetic)
					|| type.equals(UpdateType.Event)) {
				// the user did not set the control -- it needs to be set here
				setControlValue(nextValue);
			}
			if (!type.equals(UpdateType.Event)) {
				// if incoming event, then the model has already been changed
				manager.performCommand(createUpdateCommand());
			}
			valueUpdated(oldValue, nextValue);
			oldValue = nextValue;
			isUpdating = false;
			if (!currentlyValid) {
				validityConstraint.validityChanged();
			}
			currentlyValid = true;
		}
		decorateComponent();
		Log4jConfig.popNDC();
	}
}

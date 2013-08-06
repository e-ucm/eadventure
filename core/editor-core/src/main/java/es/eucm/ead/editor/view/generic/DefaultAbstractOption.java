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

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.control.commands.ChangeFieldCommand;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.editor.model.nodes.DependencyNode;

/**
 * Abstract implementation for {@link Option}s
 *
 * @param <S>
 *            The type of the option element
 */
public abstract class DefaultAbstractOption<S> extends AbstractOption<S> {

	private static final Logger logger = LoggerFactory
			.getLogger("DefaultAOption");

	/**
	 * Descriptor of the field represented by this option
	 */
	protected Accessor<S> fieldDescriptor;

	/**
	 * Common-case constructor
	 * @param label The label in the option (can be null)
	 * @param toolTipText The toolTipText in the option (cannot be null)
	 * @param object object to look into
	 * @param fieldName fieldName to access
	 */
	public DefaultAbstractOption(String label, String toolTipText,
			Object object, String fieldName, DependencyNode... changed) {
		this(label, toolTipText,
				new IntrospectingAccessor<S>(object, fieldName), changed);
	}

	/**
	 * Common-case constructor
	 * @param label The label in the option (can be null)
	 * @param toolTipText The toolTipText in the option (cannot be null)
	 * @param object object to look into
	 * @param fieldName fieldName to access
	 */
	public DefaultAbstractOption(String label, String toolTipText,
			Accessor<S> fieldDescriptor, DependencyNode... changed) {
		super(label, toolTipText, changed);
		this.fieldDescriptor = fieldDescriptor;
	}

	/**
	 * Reads model value.
	 * @return
	 */
	@Override
	public S readModelValue() {
		return fieldDescriptor.read();
	}

	/**
	 * @return the {@link Accessor} for the field that is displayed and
	 *         modified by this option element
	 */
	protected Accessor<S> getFieldDescriptor() {
		return fieldDescriptor;
	}

	/**
	 * Queried within modelChanged before considering a change to
	 * have occurred.
	 * @return
	 */
	@Override
	protected boolean changeConsideredRelevant(S oldValue, S newValue) {
		return ChangeFieldCommand.defaultIsChange(oldValue, newValue);
	}

	/**
	 * Creates a Command that describes a change to the manager.
	 * No change should be described if no change exists.
	 * @return
	 */
	@Override
	protected Command createUpdateCommand() {
		return new ChangeFieldCommand<S>(getControlValue(),
				getFieldDescriptor(), changed);
	}
}

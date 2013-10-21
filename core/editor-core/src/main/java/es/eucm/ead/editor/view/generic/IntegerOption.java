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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.control.commands.ChangeFieldCommand;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;

public class IntegerOption extends AbstractOption<Integer> {

	protected JSpinner spinner;
	protected SpinnerNumberModel model;

	/**
	 * A number option for integers from min (included) to max (excluded)
	 * @param title
	 * @param toolTipText
	 * @param object
	 * @param fieldName
	 * @param node
	 * @param model describes valid values
	 */
	public IntegerOption(String title, String toolTipText, Object object,
			String fieldName, DependencyNode node, SpinnerNumberModel model) {
		super(title, toolTipText, new IntrospectingAccessor<Integer>(object, fieldName), node);
		this.model = model;
	}

	@Override
	public Integer getControlValue() {
		return model.getNumber().intValue();
	}

	@Override
	public void setControlValue(Integer newValue) {
		model.setValue(newValue);
	}

	@Override
	public JComponent createControl() {
		spinner = new JSpinner(model);
		model.setValue(accessor.read());
		spinner.setToolTipText(getToolTipText());
		model.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				update();
			}
		});
		return spinner;
	}

	@Override
	protected Command createUpdateCommand() {
		// Users expect to undo/redo entire words, rather than character-by-character
		return new ChangeFieldCommand<Integer>(getControlValue(),
				accessor, changed) {
			@Override
			public boolean likesToCombine(Integer nextValue) {
				// return Math.abs(nextValue - oldValue) <= 1;
				return true;
			}
		};
	}
}

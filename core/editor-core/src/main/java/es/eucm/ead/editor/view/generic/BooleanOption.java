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

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;

public class BooleanOption extends AbstractOption<Boolean> {

	private JCheckBox checkBox;

	public BooleanOption(String title, String toolTipText, Object target,
			String fieldName, DependencyNode... changed) {
		super(title, toolTipText, new IntrospectingAccessor<Boolean>(target,
				fieldName), changed);
	}

	public BooleanOption(String title, String toolTipText,
			Accessor<Boolean> accessor, DependencyNode... changed) {
		super(title, toolTipText, accessor, changed);
	}

	@Override
	protected JComponent createControl() {
		checkBox = new JCheckBox(getTitle());
		checkBox.setToolTipText(getToolTipText());
		checkBox.setSelected(accessor.read());
		checkBox.addChangeListener(new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged(javax.swing.event.ChangeEvent change) {
				if (checkBox.isSelected() != oldValue) {
					update();
				}
			}
		});
		return checkBox;
	}

	@Override
	public Boolean getControlValue() {
		return checkBox.isSelected();
	}

	@Override
	protected void setControlValue(Boolean newValue) {
		checkBox.setSelected(newValue);
	}
}

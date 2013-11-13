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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.eucm.ead.editor.view.generic.table;

import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 * A text-containing column. Use null for the fieldName if you are exposing
 * keys or values directly; use the corresponding field-name otherwise.
 * @param <V>
 * @param <K>
 */
public class TextColumn<V, K> extends OptionColumn<V, K, String> {
	private final String fieldName;
	private boolean isKeyField = false;

	public TextColumn(String title, boolean editable, int width) {
		this(title, null, false, editable, width);
		super.initialize();
	}

	public TextColumn(String title, String fieldName, boolean editable,
			int width) {
		this(title, fieldName, false, editable, width);
	}

	public TextColumn(String title, String fieldName, boolean isKeyField,
			boolean editable, int width) {
		super(title, String.class, editable, width);
		this.fieldName = fieldName;
		this.isKeyField = isKeyField;
		// logger.debug("Created TextOC{} for {} named {}", hashCode(), fieldName, title);
	}

	@Override
	public Accessor<String> getAccessor(TableSupport.Row<V, K> row,
			int columnIndex) {
		// logger.debug("{}: row is {}, fieldName is {}", hashCode(), row, fieldName);
		return fieldName == null ? new IntrospectingAccessor<String>(row,
				isKeyField ? "key" : "value")
				: new IntrospectingAccessor<String>(isKeyField ? row.getKey()
						: row.getValue(), fieldName);
	}

	@Override
	public OptionColumn<V, K, String>.OptionCellControl createControl() {
		return new OptionCellControl() {
			@Override
			public JComponent createEditControl() {
				return new JTextField();
			}

			@Override
			public JComponent createViewControl() {
				JLabel jl = new JLabel();
				jl.setFont(jl.getFont().deriveFont(Font.PLAIN));
				return jl;
			}

			@Override
			public void setEditControlValue(String value) {
				((JTextComponent) control).setText(value);
			}

			@Override
			public void setViewControlValue(String value) {
				((JLabel) control).setText(value);
			}

			@Override
			public String getCellEditorValue() {
				return ((JTextComponent) control).getText();
			}
		};
	}

}

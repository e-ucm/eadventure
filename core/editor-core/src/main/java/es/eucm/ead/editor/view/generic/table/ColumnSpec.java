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

package es.eucm.ead.editor.view.generic.table;

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Allows easier customization of classes
 *
 * @param <V> object-type for rows
 * @param <K> object-type for row-keys
 */
public class ColumnSpec<V, K> {
	private final String name;
	private final Class<?> clazz;
	private final boolean editable;
	private final int width;
	private TableCellRenderer renderer;
	private TableCellEditor editor;

	public ColumnSpec(String name, Class<?> clazz, boolean editable, int width) {
		this.name = name;
		this.clazz = clazz;
		this.editable = editable;
		this.width = width;
	}

	public void setRenderer(TableCellRenderer renderer) {
		this.renderer = renderer;
	}

	public void setEditor(TableCellEditor editor) {
		this.editor = editor;
	}

	public Object getValue(TableSupport.Row<V, K> row, int columnIndex) {
		return row.getValue();
	}

	public Accessor<?> getAccessor(TableSupport.Row<V, K> row, int columnIndex) {
		throw new UnsupportedOperationException("No default accessor defined");
	}

	public String getName() {
		return name;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public int getWidth() {
		return width;
	}

	public boolean isEditable() {
		return editable;
	}

	public TableCellRenderer getRenderer() {
		return renderer;
	}

	public TableCellEditor getEditor() {
		return editor;
	}

	public Command createEditCommand(Object value, TableSupport.Row<V, K> row,
			int columnIndex, TableLikeControl<V, K> control) {
		throw new UnsupportedOperationException(
				"No default edit command defined");
	}

}

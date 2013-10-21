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

import es.eucm.ead.editor.control.CommandManager;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.generic.AbstractOption;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Manages columns exposed through (smart-ish) options. This is the preferred
 * way to expose string, integer, and related options.
 * 
 * @author mfreire
 * @param <V> type of objects in each row
 * @param <K> type of key used to index objects within their rows
 * @param <T> type of objects in cell
 */
public abstract class OptionColumn<V, K, T> extends TableSupport.ColumnSpec<V, K> {

	private final AbstractOption<T> option;
	private final OptionCellEditor editor;
	private final OptionCellRenderer renderer;
	
	public OptionColumn(AbstractOption<T> option, CommandManager manager, Class<T> clazz, 
			boolean editable, int width) {
		super(option.getTitle(), clazz, editable, width);
		this.option = (AbstractOption<T>)option;
		this.renderer = new OptionCellRenderer();
		this.editor = new OptionCellEditor();
	}
	
	public abstract Accessor<T> getAccessor(JTable table, V value, int column, int row);
	
	@Override
	public TableCellEditor getEditor() {
		return editor;
	}
	
	@Override
	public TableCellRenderer getRenderer() {
		return renderer;
	}
	
	public class OptionCellEditor implements TableCellEditor {

		@Override
		public Component getTableCellEditorComponent(JTable table, 
				Object value, boolean isSelected, int row, int column) {
			return option.retarget(getAccessor(table, value, column, row), 
					(DependencyNode[])null);
		}

		@Override
		public Object getCellEditorValue() {
			return option.getControlValue();
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			return isEditable();
		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			return false;
		}

		@Override
		public boolean stopCellEditing() {
			return true;
		}

		@Override
		public void cancelCellEditing() {
		}

		@Override
		public void addCellEditorListener(CellEditorListener l) {
		}

		@Override
		public void removeCellEditorListener(CellEditorListener l) {
		}
		
	}
	
	public class OptionCellRenderer implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			return option.retarget(getAccessor(table, value, column, row), 
					(DependencyNode[])null);
		}		
	}
}

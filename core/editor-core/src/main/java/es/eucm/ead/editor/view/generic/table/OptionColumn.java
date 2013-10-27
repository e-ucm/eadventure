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

import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;
import es.eucm.ead.editor.view.generic.table.TableSupport.Row;
import java.awt.Component;
import java.awt.Font;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages columns exposed through (smart-ish) options. This is the preferred
 * way to expose string, integer, and related options.
 * 
 * @author mfreire
 * @param <V> type of objects in each row
 * @param <K> type of key used to index objects within their rows
 * @param <T> type of objects in cell
 */
public abstract class OptionColumn<V, K, T> extends
		ColumnSpec<V, K> {

	static private Logger logger = LoggerFactory.getLogger(OptionColumn.class);

	protected Accessor<T> lastAccessor;

	protected OptionColumn(String title, Class<T> clazz, boolean editable,
			int width) {
		super(title, clazz, editable, width);
	}

    protected void initialize() {
		setEditor(createControl());
		setRenderer(createControl());        
    }
    
	@Override
	public Object getValue(Row<V, K> row, int columnIndex) {
		lastAccessor = (Accessor<T>) getAccessor(row, columnIndex);
		return lastAccessor.read();
	}

	public abstract OptionCellControl createControl();

	public abstract class OptionCellControl extends AbstractCellEditor
			implements TableCellRenderer, TableCellEditor {

		protected JComponent control;
		protected Accessor<T> accessor;

		/**
		 * creates edit control
		 */
		public abstract JComponent createEditControl();

		/**
		 * creates view control
		 */
		public abstract JComponent createViewControl();

		/**
		 * will be called to re-target the control after the accessor changes
		 */
		public abstract void setEditControlValue(T value);

		/**
		 * will be called to re-target the control after the accessor changes
		 */
		public abstract void setViewControlValue(T value);

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			accessor = lastAccessor;
			if (control == null) {
				control = createEditControl();
			}
			setEditControlValue(accessor.read());
			return control;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			accessor = lastAccessor;
			logger.debug("Requested accessor for {},{}: {}", row, column,
					accessor.read());
			if (control == null) {
				control = createViewControl();
			}
			setViewControlValue(accessor.read());
			return control;
		}

		@Override
		public abstract T getCellEditorValue();
	}
}

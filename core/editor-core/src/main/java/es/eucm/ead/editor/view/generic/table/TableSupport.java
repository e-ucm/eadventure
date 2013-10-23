/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 * <e-UCM> research group.
 *
 * Copyright 2005-2010 <e-UCM> research group.
 *
 * You can access a list of all the contributors to eAdventure at:
 * http://e-adventure.e-ucm.es/contributors
 *
 * <e-UCM> is a research group of the Department of Software Engineering and
 * Artificial Intelligence at the Complutense University of Madrid (School of
 * Computer Science).
 *
 * C Profesor Jose Garcia Santesmases sn, 28040 Madrid (Madrid), Spain.
 *
 * For more info please visit: <http://e-adventure.e-ucm.es> or
 * <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 * This file is part of eAdventure, version 2.0
 *
 * eAdventure is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * eAdventure is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with eAdventure. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.generic.table;

import es.eucm.ead.editor.R;
import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.util.i18n.Resource;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support class for table-like options
 *
 * @author mfreire
 */
public class TableSupport {

	static private Logger logger = LoggerFactory.getLogger(TableSupport.class);

	public static class Row<V, K> implements Map.Entry<K, V> {

		private K key;
		private V value;

		public Row(Map.Entry<K, V> e) {
			this(e.getValue(), e.getKey());
		}

		public Row(V value, K key) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Allows easier customization of classes
	 *
	 * @param <V> object-type for rows
	 * @param <K> object-type for row-keys
	 */
	public static class ColumnSpec<V, K> {

		private final String name;
		private final Class<?> clazz;
		private final boolean editable;
		private final int width;
		private TableCellRenderer renderer;
		private TableCellEditor editor;

		public ColumnSpec(String name, Class<?> clazz, boolean editable,
				int width) {
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

		public Object getValue(Row<V, K> row, int columnIndex) {
			return row.getValue();
		}

		public Accessor<?> getAccessor(Row<V, K> row, int columnIndex) {
			throw new UnsupportedOperationException(
					"No default accessor defined");
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

		public Command createEditCommand(Object value, Row<V, K> row,
				int columnIndex, TableLikeControl<V, K> control) {
			throw new UnsupportedOperationException(
					"No default edit command defined");
		}
	}

	public static abstract class AbstractRowTableModel<V, K> extends
			AbstractTableModel {

		protected TableSupport.ColumnSpec<V, K>[] cols;
		protected Row<V, K>[] rows;
		protected final TableLikeControl<V, K> control;

		public abstract void reindex();

		protected AbstractRowTableModel(TableLikeControl<V, K> control) {
			this.control = control;
		}

		public K keyForRow(int row) {
			return rows[row].getKey();
		}

		@Override
		public int getRowCount() {
			return rows.length;
		}

		@Override
		public int getColumnCount() {
			return cols.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return cols[columnIndex].getName();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return cols[columnIndex].getClazz();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return cols[columnIndex].isEditable();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return cols[columnIndex].getValue(rows[rowIndex], columnIndex);
		}

		@Override
		public abstract void setValueAt(Object value, int rowIndex,
				int columnIndex);

		@Override
		public void fireTableDataChanged() {
			reindex();
			super.fireTableDataChanged();
		}
	}

	// marking class for a cell containing move buttons
	public static class MoveIt {
	}

	// marking class for a cell containing a delete button
	public static class DeleteIt {
	}

	private static JButton createMinimalButton(String icon, String tooltip) {
		JButton b = new JButton();
		BufferedImage image = Resource.loadImage(icon);
		logger.info("Loading {} for '{}': {}x{}", icon, tooltip, image
				.getWidth(), image.getHeight());
		b.setIcon(new ImageIcon(image));
		b.setPreferredSize(new Dimension(16, 16));
		b.setToolTipText(tooltip);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		return b;

	}

	/**
	 * Renderer and editor for vertical 'move' buttons
	 */
	public static class MoveButtonWidget<T> extends AbstractCellEditor
			implements TableCellEditor, TableCellRenderer {

		private final JButton upButton = createMinimalButton(
				R.Drawable.interface__upArrow_png,
				Messages.options_table_upArrow);
		private final JButton downButton = createMinimalButton(
				R.Drawable.interface__downArrow_png,
				Messages.options_table_downArrow);
		private final JPanel fillerPanel = new JPanel();
		private final JPanel buttonPanel = new JPanel(new BorderLayout());
		private Object v; // whatever was last set for editing
		private int editPos;
		private TableLikeControl<T, Integer> control;

		public MoveButtonWidget(TableLikeControl<T, Integer> control) {
			this.control = control;

			upButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					MoveButtonWidget.this.control.moveUp(editPos);
				}
			});
			downButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					MoveButtonWidget.this.control.moveDown(editPos);
				}
			});
			buttonPanel.add(upButton, BorderLayout.NORTH);
			buttonPanel.add(fillerPanel, BorderLayout.CENTER);
			buttonPanel.add(downButton, BorderLayout.SOUTH);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return buttonPanel;
		}

		@Override
		public Object getCellEditorValue() {
			return v;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			this.v = value;
			this.editPos = row;
			return buttonPanel;
		}
	}

	/**
	 * Renderer and editor for 'delete' buttons
	 */
	public static class DeleteButtonWidget<T, K> extends AbstractCellEditor
			implements TableCellEditor, TableCellRenderer {

		private final JButton deleteButton = createMinimalButton(
				R.Drawable.interface__delete_png, Messages.options_table_delete);
		private final JPanel fillerPanel = new JPanel();
		private final JPanel buttonPanel = new JPanel(new BorderLayout());
		private Object v; // whatever was last set for editing
		private K deletePos;

		private TableLikeControl<T, K> control;

		public DeleteButtonWidget(TableLikeControl<T, K> control) {
			this.control = control;

			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					DeleteButtonWidget.this.control.remove(deletePos);
				}
			});
			buttonPanel.add(deleteButton, BorderLayout.NORTH);
			buttonPanel.add(fillerPanel, BorderLayout.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return buttonPanel;
		}

		@Override
		public Object getCellEditorValue() {
			return v;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			this.v = value;
			this.deletePos = control.keyForRow(row);
			return buttonPanel;
		}
	}
}

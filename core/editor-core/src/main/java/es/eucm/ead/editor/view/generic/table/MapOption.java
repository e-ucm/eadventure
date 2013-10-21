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

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.control.commands.MapCommand;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.generic.DefaultAbstractOption;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.model.elements.extra.EAdMap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An option that allows a map of elements to be manipulated.
 * Conceptually very similar to manipulating a list.
 * 
 * @author mfreire
 * @param <K> key-type
 * @param <V> value-type (for underlying list)
 */
public class MapOption<V> extends DefaultAbstractOption<EAdMap<V>> implements
		TableLikeControl<V, String> {

	static private Logger logger = LoggerFactory.getLogger(MapOption.class);

	private JPanel controlPanel;
	private JXTable tableControl;
	private JButton chooseMoreButton;
	private MapTableModel tableModel;
	private final Class<?> contentClass;

	public MapOption(String title, String toolTipText, Object object,
			String fieldName, Class<?> contentClass, DependencyNode... changed) {
		super(title, toolTipText, object, fieldName, changed);
		this.contentClass = contentClass;
	}

	public MapOption(String title, String toolTipText,
			Accessor<EAdMap<V>> fieldDescriptor, Class<?> contentClass,
			DependencyNode... changed) {
		super(title, toolTipText, fieldDescriptor, changed);
		this.contentClass = contentClass;
	}

	public TableSupport.ColumnSpec<V>[] getKeyColumns() {
		return (TableSupport.ColumnSpec<V>[]) new TableSupport.ColumnSpec[] { new TableSupport.ColumnSpec(
				"Key", String.class, false, -1) {
			@Override
			public Object getValue(int index, Object o) {
				return indexToKey(index).toString();
			}
		} };
	}

	public TableSupport.ColumnSpec<V>[] getValueColumns() {
		return (TableSupport.ColumnSpec<V>[]) new TableSupport.ColumnSpec[] { new TableSupport.ColumnSpec(
				"Value", contentClass, false, -1) };
	}

	public int keyToIndex(String key) {
		Iterator<String> it = oldValue.keySet().iterator();
		for (int i = 0; it.hasNext(); i++) {
			if (it.next() == key)
				return i;
		}
		return -1;
	}

	public String indexToKey(int index) {
		Iterator<String> it = oldValue.keySet().iterator();
		for (int i = 0; it.hasNext(); i++) {
			String key = it.next();
			if (i == index)
				return key;
		}
		return null;
	}

	/**
	 * Model used to represent the map. Looks directly at oldValue; which must
	 * always be updated.
	 */
	private class MapTableModel extends AbstractTableModel {
		private TableSupport.ColumnSpec<V>[] cols;

		@SuppressWarnings("unchecked")
		public MapTableModel() {
			TableSupport.ColumnSpec<V> upDown = new TableSupport.ColumnSpec<V>(
					"", TableSupport.MoveIt.class, true, 16) {
				@Override
				public void setValue(V o, Object value) {
					// do nothing; unchangeable values
				}
			};
			upDown.setEditor(new TableSupport.MoveButtonWidget(MapOption.this));
			upDown
					.setRenderer(new TableSupport.MoveButtonWidget(
							MapOption.this));
			TableSupport.ColumnSpec<V> delete = new TableSupport.ColumnSpec<V>(
					"", TableSupport.DeleteIt.class, true, 20) {
				@Override
				public void setValue(V o, Object value) {
					// do nothing; unchangeable values
				}
			};
			delete
					.setEditor(new TableSupport.DeleteButtonWidget(
							MapOption.this));
			delete.setRenderer(new TableSupport.DeleteButtonWidget(
					MapOption.this));
			TableSupport.ColumnSpec<V>[] keys = (TableSupport.ColumnSpec<V>[]) getKeyColumns();
			TableSupport.ColumnSpec<V>[] values = (TableSupport.ColumnSpec<V>[]) getValueColumns();
			cols = (TableSupport.ColumnSpec<V>[]) new TableSupport.ColumnSpec[keys.length
					+ values.length + 1];
			System.arraycopy(keys, 0, cols, 0, keys.length);
			System.arraycopy(values, 0, cols, keys.length, values.length);
			cols[cols.length - 1] = delete;
		}

		@Override
		public int getRowCount() {
			return oldValue.size();
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
			return cols[columnIndex].getValue(rowIndex, oldValue
					.get(indexToKey(rowIndex)));
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			cols[columnIndex].setValue(oldValue.get(indexToKey(rowIndex)),
					aValue);
		}
	}

	@Override
	protected JComponent createControl() {

		oldValue = fieldDescriptor.read();
		tableModel = new MapTableModel();
		tableControl = new JXTable(tableModel);
		for (int i = 0; i < tableModel.cols.length; i++) {
			TableSupport.ColumnSpec<V> c = tableModel.cols[i];
			if (c.getRenderer() != null) {
				tableControl.setDefaultRenderer(c.getClazz(), c.getRenderer());
			}
			if (c.getEditor() != null) {
				tableControl.setDefaultEditor(c.getClazz(), c.getEditor());
			}
			if (c.getWidth() != -1) {
				tableControl.getColumn(i).setMinWidth(c.getWidth());
				tableControl.getColumn(i).setMaxWidth(c.getWidth());
			}
		}
		tableControl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableControl.setColumnControlVisible(false);
		tableControl.setSortable(false);
		tableControl.setAutoResizeMode(JXTable.AUTO_RESIZE_ALL_COLUMNS);
		tableControl.setRowHeight(32);
		tableControl.setColumnMargin(5);

		chooseMoreButton = new JButton("+");
		chooseMoreButton.setToolTipText(Messages.options_table_add);
		chooseMoreButton.setPreferredSize(new Dimension(50, 16));
		chooseMoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				V value = chooseElementToAdd();
				String key = chooseKeyToAdd();
				if (value != null) {
					add(value, key);
				}
			}
		});

		controlPanel = new JPanel(new BorderLayout());
		JScrollPane tableScroll = new JScrollPane(tableControl);
		tableScroll.setMinimumSize(new Dimension(0, 120));
		tableScroll.setPreferredSize(new Dimension(0, 120));
		controlPanel.add(tableScroll, BorderLayout.CENTER);
		controlPanel.add(chooseMoreButton, BorderLayout.SOUTH);

		return controlPanel;
	}

	@Override
	public EAdMap<V> getControlValue() {
		return fieldDescriptor.read();
	}

	@Override
	protected void setControlValue(EAdMap<V> newValue) {
		tableModel.fireTableDataChanged();
	}

	private void executeCommand(Command c) {
		manager.performCommand(c);
	}

	@Override
	public void remove(String key) {
		V o = oldValue.get(key);
		logger.info("Removing {} (at {})", new Object[] { o, key });
		Command c = new MapCommand.RemoveFromMap<V>(oldValue, key, changed);
		executeCommand(c);
	}

	// FIXME - unimplemented
	@Override
	public V chooseElementToAdd() {
		logger.info("User wants to CHOOSE something to ADD! Madness!!");
		return null;
	}

	/**
	 * Launches UI prompt to add a key to a list element
	 */
	@Override
	public String chooseKeyToAdd() {
		logger.info("User wants to CHOOSE a KEY to ADD something! Madness!!");
		return null;
	}

	@Override
	public void add(V added, String key) {
		logger.info("Adding {}", oldValue);
		Command c = new MapCommand.AddToMap<V>(oldValue, added, key, changed);
		executeCommand(c);
	}

	/**
	 * Moves an object one position up. Triggered either externally or via
	 * button-click.
	 */
	@Override
	public void moveUp(String index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click.
	 */
	@Override
	public void moveDown(String index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the key for a given row
	 */
	@Override
	public String keyForRow(int row) {
		return indexToKey(row);
	}

	/**
	 * Consider contents to have changed, even if the list-reference does not
	 * change.
	 *
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	@Override
	protected boolean changeConsideredRelevant(EAdMap<V> oldValue,
			EAdMap<V> newValue) {
		return true;
	}
}

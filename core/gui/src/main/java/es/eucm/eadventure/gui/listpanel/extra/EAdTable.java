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

package es.eucm.eadventure.gui.listpanel.extra;

import java.util.List;

import javax.swing.DropMode;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.gui.listpanel.ColumnDescriptor;
import es.eucm.eadventure.gui.listpanel.ListPanel;
import es.eucm.eadventure.gui.listpanel.ListPanelListener;

/**
 * Represents the table where is showing the list of elements.
 */
public class EAdTable extends JTable {

	private static final long serialVersionUID = -6013030629645046573L;

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(ListPanel.class);

	// constant indicating the height of each cell, when it ISN'T selected.
	private static final int NORMAL_ROW_SIZE = 23;

	// constant indicating the height of each cell, when it IS selected.
	private static final int SELECTED_ROW_SIZE = 30;

	private List<ColumnDescriptor> columnsList;

	private ListPanelListener listPanelListener;

	/**
	 * Constructor.
	 * 
	 * @param columnsList
	 *            Columns list to draw in the table.
	 * @param listPanelListener
	 *            Listener to the elements.
	 */
	public EAdTable(List<ColumnDescriptor> columnsList,
			ListPanelListener listPanelListener) {

		super();

		this.columnsList = columnsList;
		this.listPanelListener = listPanelListener;

		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		this.getColumnModel().setColumnSelectionAllowed(false);

		this.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		this.setRowHeight(NORMAL_ROW_SIZE);

		// -----------DRAG AND DROP-------------------------------
		this.setDragEnabled(true);
		this.setDropMode(DropMode.INSERT_ROWS);
		this.setTransferHandler(new EAdTableRowTransferHandler(this));
		// ---------------------------------------------

		this.setModel(new EAdTableModel());

		for (int i = 0; i < columnsList.size(); i++) {
			if (columnsList.get(i).getHelp() == null
					|| columnsList.get(i).getHelp().isEmpty()) {
				this.getColumnModel().getColumn(i)
						.setHeaderRenderer(new InfoHeaderRenderer());
			} else
				this.getColumnModel()
						.getColumn(i)
						.setHeaderRenderer(
								new InfoHeaderRenderer(columnsList.get(i)
										.getHelp()));
			if (columnsList.get(i).getTypeColumn() != null) {
				this.getColumnModel().getColumn(i)
						.setCellRenderer(columnsList.get(i).getTypeColumn());
				this.getColumnModel().getColumn(i)
						.setCellEditor(columnsList.get(i).getTypeColumn());
			}

		}

		// allows us to highlight the selected row
		this.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent arg0) {

						setRowHeight(NORMAL_ROW_SIZE);
						if (getSelectedRow() != -1)
							setRowHeight(getSelectedRow(), SELECTED_ROW_SIZE);
					}
				});

	}

	/**
	 * Model of EAdTable.
	 * 
	 */
	public class EAdTableModel extends AbstractTableModel implements
			Reorderable {

		private static final long serialVersionUID = -3205401577873156940L;

		public int getColumnCount() {

			return columnsList.size();
		}

		public int getRowCount() {

			return listPanelListener.getCount();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {

			return listPanelListener.getValue(rowIndex, columnIndex);

		}

		@Override
		public String getColumnName(int columnIndex) {

			if (columnIndex < columnsList.size())
				return columnsList.get(columnIndex).getName();
			return "";
		}

		/**
		 * Update the element when it's modified in the table.
		 */
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {

			listPanelListener.setValue(rowIndex, columnIndex, value);
		}

		/**
		 * Update is only possible if is defined in the column by
		 * {@link Column#isEditable()}
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return getSelectedRow() == row
					&& columnsList.get(column).isEditable();
		}

		/**
		 * This method receives the movement of Drag and Drop in the table. View
		 * {@link Reorderable#reorder(int, int)}
		 */
		@Override
		public void reorder(int fromIndex, int toIndex) {
			logger.info("Drag and Drop Movement");
			
			int nRowsToMove = fromIndex - toIndex;
			if (nRowsToMove > 0) {
				for (int i = 0; i < nRowsToMove; i++) {
					listPanelListener.moveUp(fromIndex - i);
				}
			} else if (nRowsToMove < 0) {
				for (int i = 0; i < ((-nRowsToMove) - 1); i++) {
					listPanelListener.moveDown(fromIndex + i);
				}
			}
		}

	}

}

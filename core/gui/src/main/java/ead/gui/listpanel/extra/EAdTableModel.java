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

package ead.gui.listpanel.extra;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.gui.listpanel.ColumnDescriptor;
import ead.gui.listpanel.ListPanelListener;

/**
 * Model of EAdTable.
 * 
 */
public class EAdTableModel extends AbstractTableModel implements
		Reorderable {

	private static final long serialVersionUID = -3205401577873156940L;

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(EAdTableModel.class);

	private List<ColumnDescriptor> columnsList;

	private ListPanelListener listPanelListener;
	
	private EAdTable table;
	
	public EAdTableModel( List<ColumnDescriptor> columnsList, ListPanelListener listPanelListener, EAdTable eAdTable) {
		this.columnsList = columnsList;
		this.listPanelListener = listPanelListener;
		this.table = eAdTable;
	}
	
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
		return table.getSelectedRow() == row
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

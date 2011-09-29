package es.eucm.eadventure.gui.listpanel.extra;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.gui.listpanel.ColumnDescriptor;
import es.eucm.eadventure.gui.listpanel.ListPanelListener;

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

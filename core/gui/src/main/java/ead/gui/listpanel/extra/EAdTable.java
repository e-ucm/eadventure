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

import javax.swing.DropMode;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ead.gui.listpanel.ColumnDescriptor;
import ead.gui.listpanel.ListPanelListener;

/**
 * Represents the table where is showing the list of elements.
 */
public class EAdTable extends JTable {

	private static final long serialVersionUID = -6013030629645046573L;

	// constant indicating the height of each cell, when it ISN'T selected.
	private static final int NORMAL_ROW_SIZE = 23;

	// constant indicating the height of each cell, when it IS selected.
	private static final int SELECTED_ROW_SIZE = 30;


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

		this.setModel(new EAdTableModel(columnsList, listPanelListener, this));

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



}

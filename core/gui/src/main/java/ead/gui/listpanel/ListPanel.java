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

package ead.gui.listpanel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.gui.EAdScrollPane;
import ead.gui.listpanel.extra.EAdTable;
import ead.gui.listpanel.extra.ListButton;

/**
 * Main class of ListPanel
 * 
 * It create a JPanel with a {@link EAdTable} and a panel buttons to add,
 * duplicate, delete or move the elements.
 * 
 * After calling the constructor, we add the columns and finally call the method
 * createElements() to build the panel.
 */
public class ListPanel extends JPanel {

	private static final long serialVersionUID = 3671857878646326347L;

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(ListPanel.class);

	private EAdTable table;

	// Buttons
	private JButton newButton, deleteButton, duplicateButton, moveUpButton, moveDownButton;

	private List<ColumnDescriptor> columnsList;

	private ListPanelListener listPanelListener;

	/**
	 * Constructor.
	 * 
	 * @param listPanelListener
	 *            Need a class that implements {@link ListPanelListener} to
	 *            update the represented elements.
	 */
	public ListPanel(ListPanelListener listPanelListener) {
		this.listPanelListener = listPanelListener;
		columnsList = new ArrayList<ColumnDescriptor>();
	}

	/**
	 * This method adds a column to the table, must match the element's
	 * attributes.
	 * 
	 * @param columnDescriptor {@link ColumnDescriptor}
	 */
	public void addColumn(ColumnDescriptor columnDescriptor) {
		columnsList.add(columnDescriptor);
	}

	/**
	 * This method draws the panel. Call after create all columns.
	 */
	public void createElements() {

		table = new EAdTable(columnsList, listPanelListener);
		JScrollPane scroll = new EAdScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent arg0) {

						if (table.getSelectedRow() >= 0
								&& table.getSelectedRow() < listPanelListener
										.getCount()) {
							deleteButton.setEnabled(true);
							duplicateButton.setEnabled(true);
							// Enable moveUp and moveDown buttons when there is
							// more than one element
							moveUpButton.setEnabled(listPanelListener
									.getCount() > 1
									&& table.getSelectedRow() > 0);
							moveDownButton.setEnabled(listPanelListener
									.getCount() > 1
									&& table.getSelectedRow() < table
											.getRowCount() - 1);

							listPanelListener.selectionChanged();
						} else {
							deleteButton.setEnabled(false);
							duplicateButton.setEnabled(false);
							moveUpButton.setEnabled(false);
							moveDownButton.setEnabled(false);
						}
					}
				});

		JPanel buttonsPanel = new JPanel();

		newButton = new ListButton(ListButton.Type.ADD);

		newButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				addElement();
			}
		});

		deleteButton = new ListButton(ListButton.Type.DELETE);
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				deleteElement();
			}
		});

		duplicateButton = new ListButton(ListButton.Type.DUPLICATE);
		duplicateButton.setEnabled(false);
		duplicateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				duplicateElement();
			}
		});

		moveUpButton = new ListButton(ListButton.Type.MOVE_UP);
		moveUpButton.setEnabled(false);
		moveUpButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				moveUp();
			}
		});

		moveDownButton = new ListButton(ListButton.Type.MOVE_DOWN);
		moveDownButton.setEnabled(false);
		moveDownButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				moveDown();
			}
		});

		buttonsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		buttonsPanel.add(newButton, c);
		c.gridy = 1;
		buttonsPanel.add(duplicateButton, c);
		c.gridy = 2;
		buttonsPanel.add(moveUpButton, c);
		c.gridy = 3;
		buttonsPanel.add(moveDownButton, c);
		c.gridy = 5;
		buttonsPanel.add(deleteButton, c);

		c.gridy = 4;
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0;
		buttonsPanel.add(new JFiller(), c);

		this.setLayout(new BorderLayout());
		this.add(scroll, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.EAST);

	}

	// ACTIONS

	/**
	 * To add a new element.
	 */
	private void addElement() {
		if (listPanelListener.addElement()) {
			((AbstractTableModel) table.getModel()).fireTableDataChanged();
			table.changeSelection(listPanelListener.getCount() - 1,
					listPanelListener.getCount() - 1, false, false);

		} else {
			logger.warn("Can not add the element");
		}
	}

	/**
	 * To duplicate an element.
	 */
	private void duplicateElement() {

		if (listPanelListener.duplicateElement(table.getSelectedRow())) {
			((AbstractTableModel) table.getModel()).fireTableDataChanged();
			table.changeSelection(listPanelListener.getCount() - 1,
					listPanelListener.getCount() - 1, false, false);
		} else {
			logger.warn("Can not duplicate the element");
		}
	}

	/**
	 * To delete an element.
	 */
	private void deleteElement() {
		if (listPanelListener.deleteElement(table.getSelectedRow())) {
			((AbstractTableModel) table.getModel()).fireTableDataChanged();
		} else {
			logger.warn("Can not delete the element");
		}
	}

	/**
	 * To move up an element.
	 */
	private void moveUp() {

		int row = table.getSelectedRow();
		listPanelListener.moveUp(row);
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		table.changeSelection(row - 1, row - 1, false, false);

	}

	/**
	 * To move down an element.
	 */
	private void moveDown() {

		int row = table.getSelectedRow();
		listPanelListener.moveDown(row);
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		table.changeSelection(row + 1, row + 1, false, false);
	}

	/**
	 * Update the table.
	 */
	public boolean updateFields() {

		int selected = table.getSelectedRow();
		int items = table.getRowCount();

		if (table.getCellEditor() != null)
			table.getCellEditor().cancelCellEditing();

		((AbstractTableModel) table.getModel()).fireTableDataChanged();

		if (items != 0 && items == table.getRowCount()) {
			if (selected != -1) {
				table.changeSelection(selected, 0, false, false);
			}
		} else {
			// TODO must to remove the selection
			listPanelListener.selectionChanged();
		}
		return true;
	}

	/**
	 * Filler component for the panels. To use with grid bag layouts.
	 */
	private class JFiller extends JComponent {

		private static final long serialVersionUID = 1L;

		/**
		 * Constructor for placing the panel buttons.
		 */
		private JFiller() {

			setBackground(null);
			setOpaque(false);
		}
	}

}

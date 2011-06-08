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

package es.eucm.eadventure.gui.listpanel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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

import es.eucm.eadventure.gui.EAdScrollPane;
import es.eucm.eadventure.gui.R;
import es.eucm.eadventure.gui.listpanel.columntypes.CellRenderEditor;

/**
 * Main class of ListPanel
 * 
 * It create a JPanel with a {@link EAdTable} and a panel buttons to add,
 * duplicate, delete or move the elements.
 * 
 * After calling the constructor, we add the columns and finally call the method
 * createElements() to build the panel.
 * 
 * @author Sergio Bellón
 * 
 */
public class ListPanel extends JPanel {

	private static final long serialVersionUID = 3671857878646326347L;

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(ListPanel.class);

	private EAdTable table;

	// Buttons
	private JButton newButton;

	private JButton deleteButton;

	private JButton duplicateButton;

	private JButton moveUpButton;

	private JButton moveDownButton;

	private List<Column> columnsList;

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
		columnsList = new ArrayList<Column>();
	}

	/**
	 * This method adds a column to the table, must match the element's
	 * attributes. If we want editable cells call to
	 * {@link ListPanel#addColumn(String, String, CellRenderEditor, boolean)}
	 * 
	 * @param name
	 *            Name column header.
	 * @param help
	 *            Help file, can be empty.
	 * @param typeColumn
	 *            Type of the column to create, their are defined in
	 *            gui->listpanel->columntypes
	 */
	public void addColumn(String name, String help, CellRenderEditor typeColumn) {
		columnsList.add(new Column(name, help, typeColumn, false));
	}

	/**
	 * This method adds a column to the table, must match the element's
	 * attributes.
	 * 
	 * @param name
	 *            Name column header.
	 * @param help
	 *            Help file, can be empty.
	 * @param typeColumn
	 *            Type of the column to create, their are defined in
	 *            gui->listpanel->columntypes
	 * @param editable
	 *            If the cell is editable or not.
	 */
	public void addColumn(String name, String help,
			CellRenderEditor typeColumn, boolean editable) {
		columnsList.add(new Column(name, help, typeColumn, editable));
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

		try {

			InputStream is = ClassLoader
					.getSystemResourceAsStream(R.Drawable.add_png);
			newButton = new JButton(new ImageIcon(ImageIO.read(is)));
			newButton.setContentAreaFilled(false);
			newButton.setMargin(new Insets(0, 0, 0, 0));
			newButton.setBorder(BorderFactory.createEmptyBorder());
			// newButton.setToolTipText("AddBarrier");
			newButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {

					addElement();
				}
			});

			is = ClassLoader.getSystemResourceAsStream(R.Drawable.delete_png);
			deleteButton = new JButton(new ImageIcon(ImageIO.read(is)));
			deleteButton.setContentAreaFilled(false);
			deleteButton.setMargin(new Insets(0, 0, 0, 0));
			deleteButton.setBorder(BorderFactory.createEmptyBorder());
			// deleteButton.setToolTipText("DeleteBarrier");
			deleteButton.setEnabled(false);
			deleteButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					deleteElement();
				}
			});
			is = ClassLoader
					.getSystemResourceAsStream(R.Drawable.duplicate_png);
			duplicateButton = new JButton(new ImageIcon(ImageIO.read(is)));
			duplicateButton.setContentAreaFilled(false);
			duplicateButton.setMargin(new Insets(0, 0, 0, 0));
			duplicateButton.setBorder(BorderFactory.createEmptyBorder());
			// duplicateButton.setToolTipText("DuplicateBarrier");
			duplicateButton.setEnabled(false);
			duplicateButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					duplicateElement();
				}
			});
			is = ClassLoader.getSystemResourceAsStream(R.Drawable.move_up_png);
			moveUpButton = new JButton(new ImageIcon(ImageIO.read(is)));
			moveUpButton.setContentAreaFilled(false);
			moveUpButton.setMargin(new Insets(0, 0, 0, 0));
			moveUpButton.setBorder(BorderFactory.createEmptyBorder());
			// moveUpButton.setToolTipText("MoveUp");
			moveUpButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					moveUp();
				}
			});
			moveUpButton.setEnabled(false);
			is = ClassLoader
					.getSystemResourceAsStream(R.Drawable.move_down_png);
			moveDownButton = new JButton(new ImageIcon(ImageIO.read(is)));
			moveDownButton.setContentAreaFilled(false);
			moveDownButton.setMargin(new Insets(0, 0, 0, 0));
			moveDownButton.setBorder(BorderFactory.createEmptyBorder());
			// moveDownButton.setToolTipText("MoveDown");
			moveDownButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					moveDown();
				}
			});
			moveDownButton.setEnabled(false);

		} catch (IOException e) {
			e.printStackTrace();
		}

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
	 * This class represents a column in the table. It need the name of the
	 * column, and optionally can take a help file, a column type and if it's
	 * editable;
	 * 
	 * @author Sergio Bellón
	 * 
	 */
	public class Column {
		private String name;
		private String help;
		private CellRenderEditor typeColumn;
		private Boolean editable;

		/**
		 * Constructor.
		 * 
		 * @param name
		 *            Name of the column, will be shown at the header.
		 * @param help
		 *            Help file, can be empty.
		 * @param typeColumn
		 *            Type of the column to create, their are defined in
		 *            gui->listpanel->columntypes
		 * @param editable
		 *            If the cell is editable or not.
		 */
		private Column(String name, String help, CellRenderEditor typeColumn,
				boolean editable) {
			this.name = name;
			this.help = help;
			this.typeColumn = typeColumn;
			this.editable = editable;
		}

		public String getName() {
			return name;
		}

		public String getHelp() {
			return help;
		}

		public CellRenderEditor getTypeColumn() {
			return typeColumn;
		}

		public boolean isEditable() {
			return editable;
		}

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

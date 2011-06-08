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

package es.eucm.eadventure.gui.structurepanel.extra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.AbstractBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.gui.EAdScrollPane;
import es.eucm.eadventure.gui.R;
import es.eucm.eadventure.gui.structurepanel.StructureElement;
import es.eucm.eadventure.gui.structurepanel.StructurePanel;
import es.eucm.eadventure.utils.swing.SwingUtilities;

/**
 * The panel (button and list) for the selected element
 */
public class SelectedElementPanel extends JPanel {

	private static final long serialVersionUID = -4374705918840121052L;

	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory
			.getLogger(SelectedElementPanel.class);
	
	private JButton button;

	public SelectedElementPanel(final StructureElement element, int index,
			final StructurePanel panel) {
		setLayout(new StructureElementPanelLayout());
		button = new SelectedElementButton(element.getProvider().getLabel(), element.getProvider().getIcon(), panel, index, this);

		add(button, "title");

		StructureSubElementTableModel model = new StructureSubElementTableModel(
				element, "add");
		// create the table that contains sub-elements
		final JTable list = new JTable(model);
		model.setList(list);
		panel.setList(list);

		list.setTableHeader(null);

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StructureElementRenderer renderer = new StructureElementRenderer(
				element);
		list.getColumnModel().getColumn(0).setCellRenderer(renderer);
		list.getColumnModel().getColumn(0).setCellEditor(renderer);
		list.setCellSelectionEnabled(true);
		list.setShowHorizontalLines(true);
		list.setRowHeight(StructurePanel.NORMAL_ROW_SIZE);
		list.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		list.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));

						if (list.getSelectedRow() >= 0) {
							element.changeSelectedChild(element.getChild(list.getSelectedRow()));
						}

						SwingUtilities.doInEDTNow(new Runnable() {
							public void run() {
								if (list.getSelectedRow() >= 0) {
									list.setRowHeight(StructurePanel.NORMAL_ROW_SIZE);
									list.setRowHeight(list.getSelectedRow(),
											StructurePanel.SELECTED_ROW_SIZE);
									list.editCellAt(list.getSelectedRow(), 0);

								} else
									repaint();
							}
						});

						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					}
				});

		// It's launched when add, remove or duplicate a cell
		list.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				try {
					panel.update();
					list.changeSelection(list.getRowCount() - 1, 0, false,
							false);
				} catch (Exception e) {
					logger.error("Error updating the table");
				}
			}
		});

		JPanel temp = new JPanel();
		temp.setLayout(new BorderLayout());
		temp.add(list, BorderLayout.CENTER);

		if (element.getProvider().canHaveChildren()) {
			JButton addButton = new JButton(Messages.add_new, new ImageIcon(
					ClassLoader.getSystemResource(R.Drawable.add_png)
							.getPath()));

			addButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					element.addChild();
					// to select last element in the table
					list.changeSelection(list.getRowCount() - 1, 0, false,
							false);
				}
			});

			temp.add(addButton, BorderLayout.NORTH);
		}

		EAdScrollPane scrollPane = new EAdScrollPane(temp);
		add(scrollPane, "list");

		setBackground(Color.WHITE);
		scrollPane.setBorder(null);
		temp.setBorder(new PanelBorder());
	}

	/**
	 * The border of the panel of the selected element
	 */
	private class PanelBorder extends AbstractBorder {

		private static final long serialVersionUID = -8890333227355217280L;

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int w,
				int h) {
			g.setColor(Color.BLACK);
			g.drawLine(x, y, x, y + h);
			g.drawLine(x + w - 1, y, x + w - 1, y + h);
			g.drawLine(x, y + h - 1, x + w, y + h - 1);
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(0, 1, 1, 1);
		}

	}

	public int getButtonHeight() {
		return button.getHeight();
	}

}

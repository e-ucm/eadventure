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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.view.components;

import ead.editor.model.EditorModel;
import ead.editor.model.nodes.AssetNode;
import ead.editor.model.nodes.EditorNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXTable;

/**
 * A panel that can be collapsed or expanded by clicking on a button.
 *
 * @author mfreire
 */
public class PropertiesTablePanel extends NodeBrowserPanel {

	private EditorModel editorModel;
	private ArrayList<EditorNode> nodes = new ArrayList<EditorNode>();

	private JXTable table;
	private SimpleTableModel tableModel;

	public PropertiesTablePanel() {
		tableModel = new SimpleTableModel();
		table = new JXTable(tableModel);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						selected.clear();
						for (int row : table.getSelectedRows()) {
							selected.add(nodes.get(row));
						}
						firePropertyChange(selectedPropertyName, null, null);
					}
				});
		table.setColumnControlVisible(true);
		table.setSortable(true);
		table.setDefaultRenderer(BufferedImage.class, new ImageCellRenderer());
		table.setAutoResizeMode(JXTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setRowHeight(32);
		table.setColumnMargin(5);
		table.setDragEnabled(true);
		table.setTransferHandler(new NodeTransferHandler());
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setMaxWidth(50);
		tcm.getColumn(2).setMaxWidth(80);
		tcm.getColumn(3).setMaxWidth(50);
		tcm.getColumn(4).setMaxWidth(50);

		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private static class ImageCellRenderer implements TableCellRenderer {
		private ThumbnailPanel tp = new ThumbnailPanel();

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			tp.background = isSelected ? table.getSelectionBackground() : table
					.getBackground();
			tp.image = (BufferedImage) value;
			return tp;
		}
	}

	private static class ThumbnailPanel extends JPanel {
		private BufferedImage image;
		private Color background;

		@Override
		public void paint(Graphics g) {
			g.setColor(background);
			int h = getHeight();
			int w = getWidth();
			g.fillRect(0, 0, w, h);
			g.drawImage(image, (w - 32) / 2, (h - 32) / 2, 32, 32, this);
		}
	}

	private class SimpleTableModel extends AbstractTableModel {

		@Override
		public int getRowCount() {
			return nodes.size();
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "Thumb";
			case 1:
				return "Name";
			case 2:
				return "Size";
			case 3:
				return "Used in";
			case 4:
				return "Uses";
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return BufferedImage.class;
			case 1:
				return String.class;
			case 2:
				return Integer.class;
			case 3:
				return Integer.class;
			case 4:
				return Integer.class;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			EditorNode node = nodes.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return node.getThumbnail();
			case 1:
				return node.getLinkText();
			case 2: {
				return (node instanceof AssetNode) ? ((AssetNode) node)
						.getAssetSize() : 0;
			}
			case 3: {
				return (editorModel != null) ? editorModel
						.incomingDependencies(node) : -1;
			}
			case 4: {
				return (editorModel != null) ? editorModel
						.outgoingDependencies(node) : -1;
			}
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public void setNodes(Collection<EditorNode> nodes) {
		this.nodes.clear();
		this.nodes.addAll(nodes);
		selected.clear();
		tableModel.fireTableDataChanged();
	}
}

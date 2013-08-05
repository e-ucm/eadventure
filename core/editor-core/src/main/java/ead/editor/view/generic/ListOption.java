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

package ead.editor.view.generic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.model.elements.extra.EAdList;
import ead.editor.R;
import ead.editor.control.Command;
import ead.editor.control.commands.ListCommand;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.view.generic.accessors.Accessor;
import ead.utils.i18n.Resource;

/**
 * An option that allows a list of elements to be manipulated.
 * 
 * @author mfreire
 * @param <T>
 */
public class ListOption<T> extends DefaultAbstractOption<EAdList<T>> {

	private static final Logger logger = LoggerFactory.getLogger("ListOption");

	private JPanel controlPanel;
	private JXTable tableControl;
	private JButton chooseMoreButton;
	private ListTableModel tableModel;

	protected ListCellRenderer outerRenderer;

	public ListOption(String title, String toolTipText, Object object,
			String fieldName, DependencyNode... changed) {
		super(title, toolTipText, object, fieldName, changed);
	}

	public ListOption(String title, String toolTipText,
			Accessor<EAdList<T>> fieldDescriptor, DependencyNode... changed) {
		super(title, toolTipText, fieldDescriptor, changed);
	}

	/**
	 * Allows easier customization of classes
	 * 
	 * @param <T>
	 */
	public static class ColumnSpec<T> {
		private String name;
		private Class<?> clazz;
		private boolean editable;
		private int width;
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

		public Object getValue(T o) {
			return o;
		}

		public void setValue(T o, Object value) {
			throw new UnsupportedOperationException("Not supported yet.");
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
	}

	// marking class for a cell containing move buttons
	private static class MoveIt {
	}

	// marking class for a cell containing a delete button
	private static class DeleteIt {
	}

	private JButton createMinimalButton(String icon) {
		JButton b = new JButton();
		b.setIcon(new ImageIcon(Resource.loadImage(icon)));
		b.setPreferredSize(new Dimension(16, 16));
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		return b;
	}

	/**
	 * Renderer and editor for vertical 'move' buttons
	 */
	private class MoveButtonWidget extends AbstractCellEditor implements
			TableCellEditor, TableCellRenderer {
		private JButton upButton = createMinimalButton(R.Drawable.interface__upArrow_png);
		private JButton downButton = createMinimalButton(R.Drawable.interface__downArrow_png);
		private JPanel fillerPanel = new JPanel();
		private JPanel buttonPanel = new JPanel(new BorderLayout());
		private Object v; // whatever was last set for editing
		private int editPos;

		public MoveButtonWidget() {
			upButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					moveUp(oldValue.get(editPos), editPos);
				}
			});
			downButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					moveDown(oldValue.get(editPos), editPos);
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
	private class DeleteButtonWidget extends AbstractCellEditor implements
			TableCellEditor, TableCellRenderer {
		private JButton deleteButton = createMinimalButton(R.Drawable.interface__delete_png);
		private JPanel fillerPanel = new JPanel();
		private JPanel buttonPanel = new JPanel(new BorderLayout());
		private Object v; // whatever was last set for editing
		private int deletePos;

		public DeleteButtonWidget() {
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					remove(oldValue.get(deletePos), deletePos);
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
			this.deletePos = row;
			return buttonPanel;
		}
	}

	/**
	 * Called to get settings for the "meat" columns.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ColumnSpec<T>[] getExtraColumns() {
		return new ColumnSpec[] { new ColumnSpec("value", oldValue.get(0)
				.getClass(), false, -1) };
	}

	/**
	 * Model used to represent the list. Looks directly at oldValue; which must
	 * always be updated.
	 */
	private class ListTableModel extends AbstractTableModel {
		private ColumnSpec<T>[] cols;

		@SuppressWarnings("unchecked")
		public ListTableModel() {
			ColumnSpec<T> upDown = new ColumnSpec<T>("", MoveIt.class, true, 16) {
				@Override
				public void setValue(T o, Object value) {
					// do nothing; unchangeable values
				}
			};
			upDown.setEditor(new MoveButtonWidget());
			upDown.setRenderer(new MoveButtonWidget());
			ColumnSpec<T> delete = new ColumnSpec<T>("", DeleteIt.class, true,
					20) {
				@Override
				public void setValue(T o, Object value) {
					// do nothing; unchangeable values
				}
			};
			delete.setEditor(new DeleteButtonWidget());
			delete.setRenderer(new DeleteButtonWidget());
			ColumnSpec<T>[] user = (ColumnSpec<T>[]) getExtraColumns();
			cols = (ColumnSpec<T>[]) new ColumnSpec[user.length + 2];
			cols[0] = upDown;
			cols[cols.length - 1] = delete;
			System.arraycopy(user, 0, cols, 1, user.length);
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
			return cols[columnIndex].getValue(oldValue.get(rowIndex));
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			cols[columnIndex].setValue(oldValue.get(rowIndex), aValue);
		}
	}

	@Override
	protected JComponent createControl() {

		oldValue = fieldDescriptor.read();
		tableModel = new ListTableModel();
		tableControl = new JXTable(tableModel);
		for (int i = 0; i < tableModel.cols.length; i++) {
			ColumnSpec<T> c = tableModel.cols[i];
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
		chooseMoreButton.setPreferredSize(new Dimension(50, 16));
		chooseMoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				T choice = chooseElementToAdd();
				if (choice != null) {
					add(choice);
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
	protected EAdList<T> getControlValue() {
		return fieldDescriptor.read();
	}

	@Override
	protected void setControlValue(EAdList<T> newValue) {
		tableModel.fireTableDataChanged();
	}

	private void executeCommand(Command c) {
		manager.performCommand(c);
	}

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click.
	 * 
	 * @param o
	 */
	protected void remove(T o, int index) {
		logger.info("Removing {} (at {})", new Object[] { o, index });
		Command c = new ListCommand.RemoveFromList<T>(oldValue, o, changed);
		executeCommand(c);
	}

	/**
	 * Allows user to choose from available elements to add one to the list
	 * 
	 * @return
	 */
	protected T chooseElementToAdd() {
		logger.info("User wants to CHOOSE something to ADD! Madness!!");
		return null;
	}

	/**
	 * Adds an object to the list. Triggered either externally or via
	 * button-click.
	 * 
	 * @param o
	 */
	protected void add(T o) {
		logger.info("Adding {}", o);
	}

	/**
	 * Moves an object one position up. Triggered either externally or via
	 * button-click.
	 * 
	 * @param o
	 */
	protected void moveUp(T o, int index) {
		logger.info("MovingUp {} (at {})", new Object[] { o, index });
		if (index == 0) {
			logger.warn("You should NOT allow people to try to move above 0");
			return;
		}
		Command c = new ListCommand.ReorderInList<T>(oldValue, o, index,
				index - 1, changed);
		executeCommand(c);
	}

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click.
	 * 
	 * @param o
	 */
	protected void moveDown(T o, int index) {
		logger.info("MovingDown {} (at {})", new Object[] { o, index });
		if (index == tableModel.getRowCount() - 1) {
			logger.warn("You should NOT allow people to try to move below end");
			return;
		}
		Command c = new ListCommand.ReorderInList<T>(oldValue, o, index,
				index + 1, changed);
		executeCommand(c);
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
	protected boolean changeConsideredRelevant(EAdList<T> oldValue,
			EAdList<T> newValue) {
		return true;
	}
}

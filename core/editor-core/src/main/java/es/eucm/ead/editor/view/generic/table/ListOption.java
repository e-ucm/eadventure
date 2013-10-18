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

package es.eucm.ead.editor.view.generic.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.control.commands.ListCommand;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.generic.DefaultAbstractOption;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.editor.view.generic.table.TableSupport.ColumnSpec;
import es.eucm.ead.editor.view.generic.table.TableSupport.DeleteButtonWidget;
import es.eucm.ead.editor.view.generic.table.TableSupport.DeleteIt;
import es.eucm.ead.editor.view.generic.table.TableSupport.MoveButtonWidget;
import es.eucm.ead.editor.view.generic.table.TableSupport.MoveIt;
import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.model.elements.extra.EAdList;

/**
 * An option that allows a list of elements to be manipulated.
 *
 * @author mfreire
 * @param <T>
 */
public class ListOption<T> extends DefaultAbstractOption<EAdList<T>> implements
		TableLikeControl<T, Integer> {

	static private Logger logger = LoggerFactory.getLogger(ListOption.class);

	private JPanel controlPanel;
	private JXTable tableControl;
	private JButton chooseMoreButton;
	private ListTableModel tableModel;
	private final Class<?> contentClass;

	public ListOption(String title, String toolTipText, Object object,
			String fieldName, Class<?> contentClass, DependencyNode... changed) {
		super(title, toolTipText, object, fieldName, changed);
		this.contentClass = contentClass;
	}

	public ListOption(String title, String toolTipText,
			Accessor<EAdList<T>> fieldDescriptor, Class<?> contentClass,
			DependencyNode... changed) {
		super(title, toolTipText, fieldDescriptor, changed);
		this.contentClass = contentClass;
	}

	public ColumnSpec<T>[] getExtraColumns() {
		return (ColumnSpec<T>[]) new ColumnSpec[] { new ColumnSpec<T>("Value",
				contentClass, false, -1) };
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
			upDown.setEditor(new MoveButtonWidget(ListOption.this));
			upDown.setRenderer(new MoveButtonWidget(ListOption.this));
			ColumnSpec<T> delete = new ColumnSpec<T>("", DeleteIt.class, true,
					20) {
				@Override
				public void setValue(T o, Object value) {
					// do nothing; unchangeable values
				}
			};
			delete.setEditor(new DeleteButtonWidget(ListOption.this));
			delete.setRenderer(new DeleteButtonWidget(ListOption.this));
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
			return cols[columnIndex].getValue(rowIndex, oldValue.get(rowIndex));
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
		chooseMoreButton.setToolTipText(Messages.options_table_add);
		chooseMoreButton.setPreferredSize(new Dimension(50, 16));
		chooseMoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				T choice = chooseElementToAdd();
				if (choice != null) {
					add(choice, oldValue.size() - 1);
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
	public EAdList<T> getControlValue() {
		return fieldDescriptor.read();
	}

	@Override
	protected void setControlValue(EAdList<T> newValue) {
		tableModel.fireTableDataChanged();
	}

	private void executeCommand(Command c) {
		manager.performCommand(c);
	}

	@Override
	public void remove(Integer index) {
		T o = oldValue.get(index);
		logger.info("Removing {} (at {})", new Object[] { o, index });
		Command c = new ListCommand.RemoveFromList<T>(oldValue, o, changed);
		executeCommand(c);
	}

	@Override
	public T chooseElementToAdd() {
		logger.info("User wants to CHOOSE something to ADD! Madness!!");
		return null;
	}

	/**
	 * Launches UI prompt to add a key to a list element
	 */
	@Override
	public Integer chooseKeyToAdd() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(T added, Integer index) {
		logger.info("Adding {}", oldValue);
		Command c = new ListCommand.AddToList<T>(oldValue, added, oldValue
				.size(), changed);
		executeCommand(c);
	}

	/**
	 * Moves an object one position up. Triggered either externally or via
	 * button-click.
	 */
	@Override
	public void moveUp(Integer index) {
		T o = oldValue.get(index);

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
	 */
	@Override
	public void moveDown(Integer index) {
		T o = oldValue.get(index);
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
	 * Returns the key for a given row
	 */
	@Override
	public Integer keyForRow(int row) {
		return row;
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

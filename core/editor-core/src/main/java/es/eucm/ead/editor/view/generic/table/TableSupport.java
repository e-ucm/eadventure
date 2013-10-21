/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.generic.table;

import es.eucm.ead.editor.R;
import es.eucm.ead.editor.util.i18n.Resource;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support class for table-like options
 * 
 * @author mfreire
 */
public class TableSupport {

	static private Logger logger = LoggerFactory.getLogger(TableSupport.class);

	/**
	 * Allows easier customization of classes
	 *
	 * @param <T> object-type for rows
	 * @param <K> object-type for row-keys
	 */
	public static class ColumnSpec<T, K> {
		private final String name;
		private final Class<?> clazz;
		private final boolean editable;
		private final int width;
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

		public Object getValue(T o, K i) {
			return o;
		}

		public void setValue(T o, K i, Object value) {}

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
	public static class MoveIt {
	}

	// marking class for a cell containing a delete button
	public static class DeleteIt {
	}

	private static JButton createMinimalButton(String icon, String tooltip) {
		JButton b = new JButton();
		BufferedImage image = Resource.loadImage(icon);
		logger.info("Loading {} for '{}': {}x{}", icon, tooltip, image
				.getWidth(), image.getHeight());
		b.setIcon(new ImageIcon(image));
		b.setPreferredSize(new Dimension(16, 16));
		b.setToolTipText(tooltip);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		return b;
	}

	/**
	 * Renderer and editor for vertical 'move' buttons
	 */
	public static class MoveButtonWidget<T> extends AbstractCellEditor
			implements TableCellEditor, TableCellRenderer {
		private final JButton upButton = createMinimalButton(
				R.Drawable.interface__upArrow_png,
				Messages.options_table_upArrow);
		private final JButton downButton = createMinimalButton(
				R.Drawable.interface__downArrow_png,
				Messages.options_table_downArrow);
		private final JPanel fillerPanel = new JPanel();
		private final JPanel buttonPanel = new JPanel(new BorderLayout());
		private Object v; // whatever was last set for editing
		private int editPos;
		private TableLikeControl<T, Integer> control;

		public MoveButtonWidget(TableLikeControl<T, Integer> control) {
			this.control = control;

			upButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					MoveButtonWidget.this.control.moveUp(editPos);
				}
			});
			downButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					MoveButtonWidget.this.control.moveDown(editPos);
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
	public static class DeleteButtonWidget<T, K> extends AbstractCellEditor
			implements TableCellEditor, TableCellRenderer {
		private final JButton deleteButton = createMinimalButton(
				R.Drawable.interface__delete_png, Messages.options_table_delete);
		private final JPanel fillerPanel = new JPanel();
		private final JPanel buttonPanel = new JPanel(new BorderLayout());
		private Object v; // whatever was last set for editing
		private K deletePos;

		private TableLikeControl<T, K> control;

		public DeleteButtonWidget(TableLikeControl<T, K> control) {
			this.control = control;

			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					DeleteButtonWidget.this.control.remove(deletePos);
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
			this.deletePos = control.keyForRow(row);
			return buttonPanel;
		}
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.eucm.ead.editor.view.generic.table;

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.view.generic.accessors.Accessor;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Allows easier customization of classes
 *
 * @param <V> object-type for rows
 * @param <K> object-type for row-keys
 */
public class ColumnSpec<V, K> {
    private final String name;
    private final Class<?> clazz;
    private final boolean editable;
    private final int width;
    private TableCellRenderer renderer;
    private TableCellEditor editor;

    public ColumnSpec(String name, Class<?> clazz, boolean editable, int width) {
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

    public Object getValue(TableSupport.Row<V, K> row, int columnIndex) {
        return row.getValue();
    }

    public Accessor<?> getAccessor(TableSupport.Row<V, K> row, int columnIndex) {
        throw new UnsupportedOperationException("No default accessor defined");
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

    public Command createEditCommand(Object value, TableSupport.Row<V, K> row, int columnIndex, TableLikeControl<V, K> control) {
        throw new UnsupportedOperationException("No default edit command defined");
    }
    
}

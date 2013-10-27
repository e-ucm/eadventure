/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.eucm.ead.editor.view.generic.table;

import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 * A text-containing column. Use null for the fieldName if you are exposing
 * keys or values directly; use the corresponding field-name otherwise.
 * @param <V>
 * @param <K>
 */
public class TextColumn<V, K> extends OptionColumn<V, K, String> {
    private final String fieldName;
    private boolean isKeyField = false;

    public TextColumn(String title, boolean editable, int width) {
        this(title, null, false, editable, width);
        super.initialize();
    }

    public TextColumn(String title, String fieldName, boolean editable, int width) {
        this(title, fieldName, false, editable, width);
    }

    public TextColumn(String title, String fieldName, boolean isKeyField, boolean editable, int width) {
        super(title, String.class, editable, width);
        this.fieldName = fieldName;
        this.isKeyField = isKeyField;
        // logger.debug("Created TextOC{} for {} named {}", hashCode(), fieldName, title);
    }

    @Override
    public Accessor<String> getAccessor(TableSupport.Row<V, K> row, int columnIndex) {
        // logger.debug("{}: row is {}, fieldName is {}", hashCode(), row, fieldName);
        return fieldName == null ? new IntrospectingAccessor<String>(row, isKeyField ? "key" : "value") : new IntrospectingAccessor<String>(isKeyField ? row.getKey() : row.getValue(), fieldName);
    }

    @Override
    public OptionColumn.OptionCellControl createControl() {
        return new OptionCellControl() {
            @Override
            public JComponent createEditControl() {
                return new JTextField();
            }

            @Override
            public JComponent createViewControl() {
                JLabel jl = new JLabel();
                jl.setFont(jl.getFont().deriveFont(Font.PLAIN));
                return jl;
            }

            @Override
            public void setEditControlValue(String value) {
                ((JTextComponent) control).setText(value);
            }

            @Override
            public void setViewControlValue(String value) {
                ((JLabel) control).setText(value);
            }

            @Override
            public String getCellEditorValue() {
                return ((JTextComponent) control).getText();
            }
        };
    }
    
}

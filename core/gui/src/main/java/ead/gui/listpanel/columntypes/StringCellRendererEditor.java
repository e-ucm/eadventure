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

package ead.gui.listpanel.columntypes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ead.gui.EAdTextField;

public class StringCellRendererEditor extends AbstractCellEditor implements CellRenderEditor {

    private static final long serialVersionUID = 8128260157985286632L;

    private String value;

    private EAdTextField textField;

    public Object getCellEditorValue( ) {

        return value;
    }

    public Component getTableCellEditorComponent( final JTable table, Object value2, boolean isSelected, final int row, final int col ) {

        this.value = (String) value2;
        return createPanel( isSelected, table );
    }

    private Component createPanel( boolean isSelected, JTable table ) {

        JPanel containerPanel = new JPanel( );
        Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        if (isSelected) {
            border = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder( 2, 0, 2, 0, table.getSelectionBackground( ) ), border);
        }
    	containerPanel.setBorder( border );

        textField = new EAdTextField(  );
        textField.setText(this.value);
        containerPanel.addFocusListener( new FocusListener( ) {

            public void focusGained( FocusEvent e ) {

                SwingUtilities.invokeLater( new Runnable( ) {

                    public void run( ) {

                        if( !textField.hasFocus( ) ) {
                            textField.selectAll( );
                            textField.requestFocusInWindow( );
                        }
                    }
                } );
            }

            public void focusLost( FocusEvent e ) {

            }
        } );
        textField.addFocusListener( new FocusListener( ) {

            public void focusGained( FocusEvent arg0 ) {

            }

            public void focusLost( FocusEvent arg0 ) {

                stopCellEditing( );
            }
        } );
        textField.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                stopCellEditing( );
            }
        } );
        textField.getDocument( ).addDocumentListener( new DocumentListener( ) {

            public void changedUpdate( DocumentEvent arg0 ) {

            }

            public void insertUpdate( DocumentEvent arg0 ) {

                value = textField.getText( );
            }

            public void removeUpdate( DocumentEvent arg0 ) {

                value = textField.getText( );
            }
        } );
        containerPanel.setLayout( new BorderLayout( ) );
        containerPanel.add( textField, BorderLayout.CENTER );
        return containerPanel;
    }

    public Component getTableCellRendererComponent( JTable table, Object value2, boolean isSelected, boolean hasFocus, int row, int column ) {

        this.value = (String) value2;
        if( isSelected ) {
            return createPanel( isSelected, table );
        }
        else
            return new JLabel( value );
    }
    
	@Override
	public boolean isEditable() {
		return true;
	}

}

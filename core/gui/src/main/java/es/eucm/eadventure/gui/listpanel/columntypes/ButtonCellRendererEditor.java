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

package es.eucm.eadventure.gui.listpanel.columntypes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;

import es.eucm.eadventure.gui.EAdButton;
import es.eucm.eadventure.gui.listpanel.columntypes.extra.TableButtonActionListener;

public class ButtonCellRendererEditor<S> extends AbstractCellEditor implements CellRenderEditor{

    private static final long serialVersionUID = 1L;

    private String label;
    
    private TableButtonActionListener<S> actionListener;
    
    private S value;
    
    public ButtonCellRendererEditor(String label, TableButtonActionListener<S> actionListener) {
    	this.label = label;
    	this.actionListener = actionListener;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Component getTableCellEditorComponent( JTable table, Object value2, boolean isSelected, int row, int col ) {
    	value = (S) value2;
    	return getComponent(isSelected, table);
    }

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
        return getComponent(isSelected, table);
    }
    
    public JPanel getComponent(boolean isSelected, JTable table) {
        JPanel containerPanel = new JPanel( );
        Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        if (isSelected) {
            border = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder( 2, 0, 2, 0, table.getSelectionBackground( ) ), border);
        }
    	containerPanel.setBorder( border );
        EAdButton button = new EAdButton( label );
        button.setEnabled(isSelected);
        button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionListener.processClick(value);
			}
        	
        });
        containerPanel.setLayout( new BorderLayout( ) );
        containerPanel.add( button, BorderLayout.CENTER );
        containerPanel.add(button);
        return containerPanel;
    }

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

}

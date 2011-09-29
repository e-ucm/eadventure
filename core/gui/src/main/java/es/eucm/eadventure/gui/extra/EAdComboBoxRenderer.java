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

/**
 * <e-Adventure> is an <e-UCM> research project.
 * <e-UCM>, Department of Software Engineering and Artificial Intelligence.
 * Faculty of Informatics, Complutense University of Madrid (Spain).
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J.
 * @author Moreno-Ger, P. & Fern‡ndez-Manj—n, B. (directors)
 * @year 2009
 * Web-site: http://e-adventure.e-ucm.es
 */

/*
    Copyright (C) 2004-2009 <e-UCM> research group

    This file is part of <e-Adventure> project, an educational game & game-like 
    simulation authoring tool, availabe at http://e-adventure.e-ucm.es. 

    <e-Adventure> is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    <e-Adventure> is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with <e-Adventure>; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/
package es.eucm.eadventure.gui.extra;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;

public class EAdComboBoxRenderer extends BasicComboBoxRenderer {

    private static final long serialVersionUID = -1185307126270599578L;

    private HashMap<Object, JPanel> components;
    
    private JComboBox comboBox;
    
    public EAdComboBoxRenderer(JComboBox comboBox) {
        components = new HashMap<Object, JPanel>();
        this.comboBox = comboBox;
    }

    @Override
    public Component getListCellRendererComponent(JList list,  Object value, int index,  boolean isSelected, boolean cellHasFocus) {
        JPanel panel = components.get( value );

        if (panel == null) {
            panel = new JPanel();
            components.put( value, panel );
            panel.add( getContent(value) );
        }
        
        if (index == -1) {
            JComponent content = getContent(value);
            content.setBorder(new EAdBorder());

            if (isSelected)
            	((EAdBorder) content.getBorder()).setColor(EAdGUILookAndFeel.getFocusColor());

            if (!comboBox.isEnabled()) {
            	content.setForeground(Color.LIGHT_GRAY);
            }

            return content;
        }

        if (isSelected) {
        	panel.setBackground(EAdGUILookAndFeel.getFocusColor());
        	panel.setForeground(EAdGUILookAndFeel.getFocusColor());
        }
        else {
        	panel.setBackground(list.getBackground());
        	panel.setForeground(list.getForeground());
        }

        return panel;
    }

    protected JComponent getContent( Object value ) {
    	JLabel label = new JLabel() {
			private static final long serialVersionUID = 1L;

			@Override
    		public Insets getInsets() {
    			return new Insets(6,6,6,6);
    		}
			
			@Override
    		public Insets getInsets(Insets insets) {
    			return new Insets(6,6,6,6);
    		}
    	};

    	if (value == null || (value instanceof String && ((String) value).equals("")) )
        	label.setText("");
      	else if ( value instanceof String ) 
        	label.setText( (String) value );
        else
        	label.setText( value.toString( ) );
      	return label;
    }
   
}
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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;

import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

import es.eucm.eadventure.gui.EAdButton;
import es.eucm.eadventure.gui.EAdGUILookAndFeel;
import es.eucm.eadventure.gui.EAdTextField;
import es.eucm.eadventure.utils.swing.SwingUtilities;

public class EAdComboBoxUI extends BasicComboBoxUI {

    public static ComponentUI createUI(JComponent c) {
        return new EAdComboBoxUI();
    }
    
    public EAdComboBoxUI() {
        super();
    }
    
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);

        comboBox = (JComboBox) c;
		comboBox.setBorder( null );
		comboBox.setBackground(EAdGUILookAndFeel.getBackgroundColor());
        comboBox.setRenderer( new EAdComboBoxRenderer( comboBox ) );
        
        listBox.setSelectionForeground(EAdGUILookAndFeel.getForegroundColor());
        listBox.setSelectionBackground(EAdGUILookAndFeel.getBackgroundColor());
        
        if (comboBox.getEditor( ) != null && comboBox.getEditor( ).getEditorComponent( ) != null) {
        	comboBox.getEditor( ).getEditorComponent( ).addFocusListener( new FocusListener( ) {
                public void focusGained( FocusEvent arg0 ) {
                   SwingUtilities.doInEDTNow( new Runnable( ) {
                        public void run( ) {
                        	comboBox.repaint( );
                        }
                   } );
                }
                
                public void focusLost( FocusEvent arg0 ) {
                    SwingUtilities.doInEDTNow( new Runnable( ) {
                        public void run( ) {
                        	comboBox.repaint( );
                        }
                    } );
                }
            } );
        }
    }
	
	@Override
	public Dimension getPreferredSize(JComponent c) {
		Dimension d = super.getPreferredSize(c);
        return new Dimension(Math.max(d.width, 30), Math.max(d.height, 12));
	}
	
    @Override
    protected LayoutManager createLayoutManager() {
        return new LayoutManager() {
            public void addLayoutComponent(String name, Component comp) {}

            public void removeLayoutComponent(Component comp) {}

            public Dimension preferredLayoutSize(Container parent) {
                return parent.getPreferredSize();
            }

            public Dimension minimumLayoutSize(Container parent) {
                return parent.getMinimumSize();
            }

            public void layoutContainer(Container parent) {
                JComboBox cb = (JComboBox)parent;
                int width = cb.getWidth();
                int height = cb.getHeight();
                
                Insets insets = getInsets();
                int buttonSize = height - (insets.top + insets.bottom);
                Rectangle cvb;

                if ( arrowButton != null ) {
                    arrowButton.setBounds( width - (insets.right + (int) arrowButton.getMaximumSize( ).getWidth( )),
                           insets.top,
                           (int) arrowButton.getMaximumSize( ).getWidth( ), buttonSize);
                }
                if ( editor != null ) {
                    cvb = rectangleForCurrentValue();
                    editor.setBounds(cvb);
                }
            }
        };
    }

    @Override
    protected ComboPopup createPopup() {
        EAdComboPopup bcp = new EAdComboPopup(comboBox);
        bcp.setBorder( new EAdBorder());
        return bcp;
    }
    
    @Override
    protected JButton createArrowButton() {
        JButton button = new EAdButton(JButton.PREVIOUS);
        button.setMaximumSize( new Dimension(16 + EAdBorder.BORDER, 20) );
        return button;
    }

    @Override
    public void paintCurrentValueBackground(Graphics g, Rectangle b, boolean hasFocus) {
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color c = g.getColor( );
        g.setColor( EAdGUILookAndFeel.getBackgroundColor() );
        g.fillRect( b.x, b.y, b.width, b.height );
        g.setColor( c );
    }

    @Override
    protected ComboBoxEditor createEditor() {
    	return new EAdComboBoxEditor(comboBox.getToolTipText());
    }

    private class EAdComboBoxEditor extends EAdTextField implements ComboBoxEditor {

        private static final long serialVersionUID = -4690197769299510034L;

        private Object oldValue;
        
        public EAdComboBoxEditor( String emptyText2 ) {
            super();

            FontMetrics metrics = getFontMetrics(getFont());
            int columnWidth = metrics.charWidth('m');

            if (emptyText2 != null) {
            	setColumns( (int ) ((float) metrics.stringWidth( emptyText2 ) / columnWidth) + 1);
                setToolTipText(emptyText2);
            }
        }
        
        @Override
        public Insets getInsets() {
        	return new Insets(6, 6, 6, 6);
        }

       
        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled( enabled );
            SwingUtilities.doInEDTNow( new Runnable() {
                public void run( ) {
                    repaint();
                }
            });
        }
        
        public Component getEditorComponent( ) {
            return this;
        }

        /** 
         * Sets the item that should be edited. 
         *
         * @param anObject the displayed value of the editor
         */
        public void setItem(Object anObject) {
            if ( anObject != null )  {
                setText(anObject.toString());
                
                oldValue = anObject;
            } else {
                setText("");
            }
        }

        public Object getItem() {
            Object newValue = getText();
            
            if (oldValue != null && !(oldValue instanceof String))  {
                // The original value is not a string. Should return the value in it's
                // original type.
                if (newValue.equals(oldValue.toString()))  {
                    return oldValue;
                } else {
                    // Must take the value from the editor and get the value and cast it to the new type.
                    Class<?> cls = oldValue.getClass();
                    try {
                        Method method = cls.getMethod("valueOf", new Class[]{String.class});
                        newValue = method.invoke(oldValue, new Object[] { getText()});
                    } catch (Exception ex) {
                        // Fail silently and return the newValue (a String object)
                    }
                }
            }
            return newValue;
        }

    }
    
    
    
}

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

package es.eucm.eadventure.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import es.eucm.eadventure.gui.extra.EAdBorder;

public class EAdTextArea extends JPanel {

    private static final long serialVersionUID = -5047534512351426805L;
    
    private Color borderColor;
    
    private String title = null;
    
    private int titleHeight = 0;
    
    private TextArea textArea;
    
    public EAdTextArea( ) {
        this(null);
    }
    
    public EAdTextArea(String title2) {
       	super();
    	setTitle(title2);
    	initialize();
 	}
    
    public JTextArea getTextArea() {
    	return textArea;
    }

	private void setTitle(String title) {
    	this.title = title;
    }
    
    private void initialize() {
    	textArea = new TextArea();
    	textArea.setLineWrap(true);
    	textArea.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent k) {
				if (k.getKeyChar() == KeyEvent.VK_ENTER) {
					int pos =  textArea.getCaretPosition();
					textArea.insert("\n", pos);
				}
			}
    		
    	});
		FontMetrics metrics = getFontMetrics(getFont());
		titleHeight = metrics.getHeight();
    	setLayout(new BorderLayout());
    	EAdScrollPane pane = new EAdScrollPane(textArea, EAdScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    			EAdScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	pane.setMinimumSize(new Dimension(0, 3 * titleHeight));
    	add(pane, BorderLayout.CENTER);
        if (this.hasFocus( ))
            borderColor =  EAdGUILookAndFeel.getFocusColor();
        else
        	borderColor = EAdGUILookAndFeel.getForegroundColor();

    	this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
	            borderColor =  EAdGUILookAndFeel.getFocusColor();
	            repaint();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
	        	borderColor = EAdGUILookAndFeel.getForegroundColor();
	        	repaint();
			}
    	});
    	
        if (isEnabled( ))
            setBackground( EAdGUILookAndFeel.getBackgroundColor() );
        else
            setBackground( EAdGUILookAndFeel.getDisabledColor());
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setBackground( enabled ? EAdGUILookAndFeel.getBackgroundColor() : EAdGUILookAndFeel.getDisabledColor());
    }
    
    @Override
    public void paintBorder(Graphics g) {
    	Color temp = g.getColor();
        g.setColor( borderColor );
        g.drawRect( 0, 1, this.getWidth( ) - 1, this.getHeight( ) - 3 );
        g.setColor(temp);
    }
    
 
    @Override
    public void paintComponent(Graphics g) {
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        super.paintComponent(g);

        if (title != null) {
	    	Color temp = g.getColor();
	        g.setColor( borderColor );
	        g.drawLine(0, titleHeight + g.getFontMetrics().getDescent() + 2, this.getWidth(), titleHeight + g.getFontMetrics().getDescent()  + 2);
	        g.setColor(temp);
	        g.drawString(title, 6, titleHeight);
        }
    }
    
    @Override
    public void setToolTipText(String text) {
    	if ((text == null && this.getToolTipText() != null) || (text != null && !text.equals(this.getToolTipText()))) {
    		textArea.setToolTipText( text );
    	}
    }
    
    @Override
    public Insets getInsets() {
    	return new Insets(8 + titleHeight, 5, 5, 1);
    }

    
    private class TextArea extends JTextArea {

        private static final long serialVersionUID = -5047534512351426805L;
        
        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setBackground( enabled ? EAdGUILookAndFeel.getBackgroundColor() : EAdGUILookAndFeel.getDisabledColor());
        }
                
        @Override
        public void paintComponent(Graphics g) {
            ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

            super.paintComponent(g);
            if (this.isEditable( ) && getToolTipText( ) != null && (this.getText( ) == null || this.getText( ).equals( "" ))) {
                Color temp = g.getColor( );
                g.setColor( Color.LIGHT_GRAY );
                g.drawString( getToolTipText(), 6, (getHeight( ) - g.getFontMetrics( ).getHeight( ) ) / 2 + g.getFontMetrics( ).getAscent());
                g.setColor( temp );
            }
        }
        
        @Override
        public void setToolTipText(String text) {
        	if ((text == null && this.getToolTipText() != null) || (text != null && !text.equals(this.getToolTipText()))) {
    	        super.setToolTipText( text );
        	}
        }
        
        @Override
        public void validate() {
        	super.validate();
        	FontMetrics fm = getFontMetrics(getFont());
            this.setMinimumSize( new Dimension(0,
    				3 * fm.getHeight() + 3 + EAdBorder.BORDER) );
        }
        
        @Override
        public Insets getInsets() {
        	return new Insets(6, 5, 6, 5);
        }

    }
}

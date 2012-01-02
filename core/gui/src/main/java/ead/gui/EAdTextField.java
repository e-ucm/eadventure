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

package ead.gui;

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

import javax.swing.JTextField;

import ead.gui.extra.EAdBorder;

public class EAdTextField extends JTextField {

    private static final long serialVersionUID = -5047534512351426805L;
    
    private Color borderColor;
    
    private String title = null;
    
    private int titleLength = 0;
    
    public EAdTextField(int size) {
        super(size);
        initialize();
    }
    
    public EAdTextField( ) {
        super();
        initialize();
    }
    
    public EAdTextField(String title, int size){
    	super(size);
    	setTitle(title);
    	initialize();
    }
    
    public EAdTextField(String title2) {
       	super();
    	setTitle(title2);
    	initialize();
 	}

	private void setTitle(String title) {
    	this.title = title;
    	if (title != null) {
    		FontMetrics metrics = getFontMetrics(getFont());
    		titleLength = metrics.stringWidth(title) + 10;
    	}
    }
    
    private void initialize() {
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
        
        //FIXME this key adapter is used because the backspace key stops working when changing l&f
    	this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent k) {
				if (getText().length() > 0) {
					if (k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						setText(getText().substring(0, getText().length() - 1));
						k.consume();
					}
					else if (k.getKeyCode() == KeyEvent.VK_LEFT) {
						setCaretPosition(getCaretPosition() - 1);
						k.consume();
					}
					else if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
						if (getCaretPosition() >= getText().length())
							setCaretPosition(getCaretPosition() + 1);
						k.consume();
					}
				}
			}

    	});

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
        if (this.isEditable( ) && getToolTipText( ) != null && (this.getText( ) == null || this.getText( ).equals( "" ))) {
            Color temp = g.getColor( );
            g.setColor( Color.LIGHT_GRAY );
            g.drawString( getToolTipText(), 6 + titleLength, (getHeight( ) - g.getFontMetrics( ).getHeight( ) ) / 2 + g.getFontMetrics( ).getAscent());
            g.setColor( temp );
        }
        
        if (title != null) {
	    	Color temp = g.getColor();
	        g.setColor( borderColor );
	        g.drawLine(titleLength, 1, titleLength, this.getHeight() - 2);
	        g.setColor(temp);
	        g.drawString(title, 6, (getHeight( ) - g.getFontMetrics( ).getHeight( ) ) / 2 + g.getFontMetrics( ).getAscent());
        }
    }
    
    @Override
    public void setToolTipText(String text) {
    	if ((text == null && this.getToolTipText() != null) || (text != null && !text.equals(this.getToolTipText()))) {
	        super.setToolTipText( text );
	        if (text != null) {
	            FontMetrics metrics = getFontMetrics(getFont());
	            int columnWidth = metrics.charWidth('m');
	            int columns = (int ) ((float) metrics.stringWidth( text ) / columnWidth) + 1;
	            setColumns( Math.max( this.getColumns( ), columns ) );
	        }
    	}
    }
    
    @Override
    public void validate() {
    	super.validate();
    	FontMetrics fm = getFontMetrics(getFont());
        this.setPreferredSize( new Dimension(fm.stringWidth(getText()) + 4 + EAdBorder.BORDER,
				fm.getHeight() + 3 + EAdBorder.BORDER) );
    }
    
    @Override
    public Insets getInsets() {
    	return new Insets(6, 5 + titleLength, 6, 5);
    }

}

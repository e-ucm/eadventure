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

package es.eucm.eadventure.gui.extra;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;
import es.eucm.eadventure.gui.R;

public class EAdRadioButtonUI extends BasicRadioButtonUI {
	
	public EAdRadioButtonUI() {
		super();
	}
	
	public static ComponentUI createUI(JComponent c) {
		return new EAdRadioButtonUI();
	}
	
	public void installUI(JComponent c) {
		super.installUI(c);
		
		JRadioButton radioButton = (JRadioButton) c;
		
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream( R.Drawable.radio_empty_png );
            ImageIcon icon = new ImageIcon( ImageIO.read(is) );
            radioButton.setIcon( icon );
            radioButton.setDisabledIcon( icon );
            radioButton.setPressedIcon( icon );
            
            is = ClassLoader.getSystemResourceAsStream( R.Drawable.radio_checked_png );
            icon = new ImageIcon( ImageIO.read(is) );
            radioButton.setDisabledSelectedIcon( icon );
            radioButton.setSelectedIcon( icon );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
        
        radioButton.setBackground(radioButton.isEnabled() ? EAdGUILookAndFeel.getBackgroundColor() : EAdGUILookAndFeel.getDisabledColor());
        radioButton.setForeground(EAdGUILookAndFeel.getForegroundColor());
        
        radioButton.setFocusPainted( false );
        radioButton.setBorderPainted( true );

        EAdBorder border = new EAdBorder(radioButton);
        radioButton.setBorder( border );

        EAdButtonListener eAdButtonListener = new EAdButtonListener( border, radioButton );
        radioButton.addMouseListener(eAdButtonListener);
        radioButton.addFocusListener(eAdButtonListener);
        radioButton.addActionListener(eAdButtonListener);
        radioButton.addPropertyChangeListener("enabled", eAdButtonListener);
	}
	
	@Override
	public Dimension getPreferredSize(JComponent c) {
		JRadioButton r = (JRadioButton) c;
	    r.setBorderPainted( true );
    	FontMetrics fm = c.getFontMetrics(c.getFont());
        return new Dimension(fm.stringWidth(r.getText()) + r.getIcon().getIconWidth() + 16 + 2*EAdBorder.BORDER,
				(int) (1.2f*fm.getHeight()) + 4 + EAdBorder.BORDER);
	}

	@Override
	public void paint(Graphics g, JComponent c)  {
	       ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			super.paint(g, c);
		}

}

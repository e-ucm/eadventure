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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;
import es.eucm.eadventure.utils.swing.SwingUtilities;

public class EAdBorderListener implements FocusListener, MouseListener, ActionListener, PropertyChangeListener {

	private EAdBorder border;
	
	private JComponent component;
	
	private boolean enabled = true;
	
	public EAdBorderListener(EAdBorder border, JComponent component) {
		this.border = border;
		this.component = component;
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
        border.setColor( EAdGUILookAndFeel.getFocusColor() );
        border.hightlight();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
        border.setColor( EAdGUILookAndFeel.getForegroundColor() );
        if (component.getMousePosition() != null || component.hasFocus())
        	border.hightlight();
        else
        	border.dehighlight();
	}
	
    public void mouseClicked( MouseEvent e ) {
    	if (enabled) {
            border.click();
            border.unclick();
    	}
    }

    public void mouseEntered( MouseEvent e ) {
    	if (enabled)
    		border.hightlight();
    }

    public void mouseExited( MouseEvent e ) {
    	if (enabled)
    		border.dehighlight();
    }

    public void mousePressed( MouseEvent e ) {
    	if (enabled)
    		border.click();
    }

    public void mouseReleased( MouseEvent e ) {
    	if (enabled)
    		border.unclick();
    }
    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		border.click();
        if (component.getMousePosition() != null || component.hasFocus())
        	border.hightlight();
        else
        	border.dehighlight();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
    	this.enabled = component.isEnabled();
    	component.setBackground(enabled ? EAdGUILookAndFeel.getBackgroundColor() : EAdGUILookAndFeel.getDisabledColor());
        SwingUtilities.doInEDTNow( new Runnable( ) {
            public void run( ) {
            	component.repaint( );
            }
       } );
	}

}

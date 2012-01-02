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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ead.gui.extra.EAdBorder;
import ead.utils.swing.SwingUtilities;

public class EAdSplitPane extends JSplitPane {

	private static final long serialVersionUID = 668872885162090686L;
	
	 /**
     * Creates a new <code>JSplitPane</code> with the specified
     * orientation and
     * with the specified components that do not do continuous
     * redrawing.
     *
     * @param newOrientation  <code>JSplitPane.HORIZONTAL_SPLIT</code> or
     *                        <code>JSplitPane.VERTICAL_SPLIT</code>
     * @param newLeftComponent the <code>Component</code> that will
     *		appear on the left
     *        	of a horizontally-split pane, or at the top of a
     *        	vertically-split pane
     * @param newRightComponent the <code>Component</code> that will
     *		appear on the right
     *        	of a horizontally-split pane, or at the bottom of a
     *        	vertically-split pane
     * @exception IllegalArgumentException if <code>orientation</code>
     *		is not one of: HORIZONTAL_SPLIT or VERTICAL_SPLIT
     */
    public EAdSplitPane(int newOrientation,
                      Component newLeftComponent,
                      Component newRightComponent){
    	super(newOrientation, newLeftComponent, newRightComponent);
    }

    public void initialize() {
        this.setBackground( EAdGUILookAndFeel.getBackgroundColor() );
        
        addFocusListener( new FocusListener() {
            public void focusGained( FocusEvent e ) {
                SwingUtilities.doInEDTNow( new Runnable() {
                    public void run( ) {
                        repaint();
                    }
                });
            }

            public void focusLost( FocusEvent e ) {
                SwingUtilities.doInEDTNow( new Runnable() {
                    public void run( ) {
                        repaint();
                    }
                });
            }
        });
    }
	
	@Override
	public void setLeftComponent(Component comp) {
    	if (comp instanceof JComponent) {
    		JComponent jcomp = (JComponent) comp;
    		if (!(jcomp.getBorder() instanceof EAdBorder)) {
    			JPanel panel = new JPanel();
    			panel.setLayout(new BorderLayout());
    			panel.add(comp, BorderLayout.CENTER);
    			super.setLeftComponent(panel);
    		} else
    			super.setLeftComponent(comp);
    	} else {
    		JPanel panel = new JPanel();
			panel.add(comp);
			super.setLeftComponent(panel);
    	}
    }

	@Override
	public void setRightComponent(Component comp) {
    	if (comp instanceof JComponent) {
    		JComponent jcomp = (JComponent) comp;
    		if (!(jcomp.getBorder() instanceof EAdBorder)) {
    			JPanel panel = new JPanel();
    			panel.setLayout(new BorderLayout());
    			panel.add(comp, BorderLayout.CENTER);
    			super.setRightComponent(panel);
    		} else
    			super.setRightComponent(comp);
    	} else {
    		JPanel panel = new JPanel();
			panel.add(comp);
			super.setRightComponent(panel);
    	}
    }
    
}

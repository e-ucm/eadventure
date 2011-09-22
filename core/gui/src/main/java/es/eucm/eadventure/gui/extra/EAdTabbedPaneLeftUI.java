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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;
import es.eucm.eadventure.utils.swing.SwingUtilities;

public class EAdTabbedPaneLeftUI extends BasicTabbedPaneUI implements FocusListener, ChangeListener, PropertyChangeListener {
	private int topTab =  Integer.MAX_VALUE;
	
	private int bottomTab = 0;
	
    public static ComponentUI createUI(JComponent c) {
        return new EAdTabbedPaneLeftUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI( c );
        tabPane.addFocusListener(this);
        
        this.tabAreaInsets = new Insets(3, 8, 6, 0);
        this.contentBorderInsets = new Insets(4, 4, 4, 4);

        this.selectedTabPadInsets = new Insets(6, 10, 0, 2);
        this.tabInsets = new Insets(3, 10, 0, 3);
    }
    
    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        g.setColor(tabPane.hasFocus() ? EAdGUILookAndFeel.getFocusColor() : EAdGUILookAndFeel.getForegroundColor());
        g.drawRect(rects[tabIndex].x + 6, rects[tabIndex].y, rects[tabIndex].width, rects[tabIndex].height);
    }
    
    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected ) {
        g.setColor( EAdGUILookAndFeel.getBackgroundColor() );
        g.fillRect(rects[tabIndex].x + 6, rects[tabIndex].y, rects[tabIndex].width, rects[tabIndex].height);
    }

    @Override
    public void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect)  {
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        super.paintTab( g, tabPlacement, rects, tabIndex, iconRect, textRect );

        topTab = rects[tabPane.getSelectedIndex()].y;
        bottomTab = rects[tabPane.getSelectedIndex()].y + rects[tabPane.getSelectedIndex()].height;
    }
    
    @Override
    public void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected)  {
    }

    @Override
    public void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        g.setColor(tabPane.hasFocus() ? EAdGUILookAndFeel.getFocusColor() : EAdGUILookAndFeel.getForegroundColor());
        g.drawLine(x, y - 1 + h, x + w - 1, y - 1 + h);
    }

    @Override
    public void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        g.setColor(tabPane.hasFocus() ? EAdGUILookAndFeel.getFocusColor() : EAdGUILookAndFeel.getForegroundColor());
        g.drawLine(x, y - 1, x + w - 1, y - 1);
    }

    @Override
    public void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        g.setColor(tabPane.hasFocus() ? EAdGUILookAndFeel.getFocusColor() : EAdGUILookAndFeel.getForegroundColor());
        g.drawLine(x, y - 1, x, topTab);
        g.drawLine(x, bottomTab, x, y - 1 + h);
    }

    @Override
    public void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        g.setColor(tabPane.hasFocus() ? EAdGUILookAndFeel.getFocusColor() : EAdGUILookAndFeel.getForegroundColor());
        g.drawLine(x + w - 1, y - 1, x + w - 1, y - 1 + h);
    }

    @Override
    public void focusGained( FocusEvent e ) {
        SwingUtilities.doInEDTNow( new Runnable() {
            public void run( ) {
            	tabPane.repaint();
            }
        });
    }

    @Override
    public void focusLost( FocusEvent e ) {
        SwingUtilities.doInEDTNow( new Runnable() {
            public void run( ) {
            	tabPane.repaint();
            }
        });
    }
    
    @Override
    public void stateChanged( ChangeEvent e ) {
        SwingUtilities.doInEDTNow( new Runnable() {
            public void run( ) {
            	tabPane.repaint();
            }
        });
    }

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				tabPane.repaint();
			}
		});
	}
}

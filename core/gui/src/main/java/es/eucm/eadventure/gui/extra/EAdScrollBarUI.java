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
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import es.eucm.eadventure.gui.EAdButton;
import es.eucm.eadventure.gui.EAdGUILookAndFeel;

public class EAdScrollBarUI extends BasicScrollBarUI {

    public static ComponentUI createUI(JComponent c) {
        c.setBackground(EAdGUILookAndFeel.getBackgroundColor());
        return new EAdScrollBarUI();
    }
    
    public EAdScrollBarUI() {
        super();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        decrButton = new EAdButton(orientation);
        return decrButton;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        incrButton = new EAdButton(orientation);
        return incrButton;
    } 

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)  
    {
        g.setColor(EAdGUILookAndFeel.getBackgroundColor());
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }


    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)  
    {
        if(thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        
        int w = thumbBounds.width;
        int h = thumbBounds.height;     

        g.translate(thumbBounds.x, thumbBounds.y);

        Color color = EAdGUILookAndFeel.getForegroundColor();
        Color newColor = new Color(color.getRed( ), color.getGreen( ), color.getBlue( ), 60);
        g.setColor( newColor );
        g.fillRect( 0, EAdBorder.BORDER, w-1-EAdBorder.BORDER, h-1-EAdBorder.BORDER );
        if (isDragging) {
            g.setColor( EAdGUILookAndFeel.getBackgroundColor() );
            g.fillRect( 2, EAdBorder.BORDER - 2, w-1-EAdBorder.BORDER, h-1-EAdBorder.BORDER );
            g.setColor( EAdGUILookAndFeel.getForegroundColor() );
            g.drawRect( 2, EAdBorder.BORDER - 2, w-1-EAdBorder.BORDER, h-1-EAdBorder.BORDER );
        } else {
            g.setColor(EAdGUILookAndFeel.getBackgroundColor() );
            g.fillRect( EAdBorder.BORDER, 0, w-1-EAdBorder.BORDER, h-1-EAdBorder.BORDER );
            g.setColor( EAdGUILookAndFeel.getForegroundColor() );
            g.drawRect( EAdBorder.BORDER, 0, w-1-EAdBorder.BORDER, h-1-EAdBorder.BORDER );
        }
        
        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

	public void setFocusable(boolean focusable) {
		this.decrButton.setFocusable(focusable);
		this.incrButton.setFocusable(focusable);
	}

}

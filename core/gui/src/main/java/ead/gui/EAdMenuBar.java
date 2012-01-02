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
package ead.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JMenuBar;


public class EAdMenuBar extends JMenuBar {

    private static final long serialVersionUID = -286866608587900325L;

    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
    	for (int i = 0; i < this.getMenuCount(); i++) {
    		this.getMenu(i).setPopupMenuVisible(false);
    		this.getMenu(i).setEnabled(enabled);
    	}
    	this.setBackground(EAdGUILookAndFeel.getBackgroundColor());
    }
    
    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        
    	super.paint(g);
    	
    	if (!this.isEnabled()) {
	    	Graphics2D g2 = (Graphics2D) g;

	    	Color c = g2.getColor();
	    	Composite comp = g2.getComposite();

	        g2.setColor( EAdGUILookAndFeel.getForegroundColor() );
	        g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.6f ) );
	    	
	    	g2.fillRect(0, 0, this.getWidth(), this.getHeight());
	    	
	    	g2.setColor(c);
	    	g2.setComposite(comp);
    	}
    }
}

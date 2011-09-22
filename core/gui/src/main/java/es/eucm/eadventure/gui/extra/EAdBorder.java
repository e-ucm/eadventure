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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.border.AbstractBorder;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;
import es.eucm.eadventure.utils.swing.SwingUtilities;

public class EAdBorder extends AbstractBorder {
    private static final long serialVersionUID = -9147851572724474559L;

    public static final int BORDER = 5;
    
    private int depth = 0;
    
    private Timer effectTimer;
    
    private Color color = EAdGUILookAndFeel.getForegroundColor();
        
    private Color borderColor = new Color(color.getRed( ), color.getGreen( ), color.getBlue( ), 40);
    	
    private JComponent component = null;
    
    private int width = 1;
    
    public EAdBorder() {
    }
    
    public EAdBorder(JComponent component) {
        this.component = component;
    }

    public void setColor(Color color) {
        this.color = color;
        borderColor = new Color(color.getRed( ), color.getGreen( ), color.getBlue( ), 40);
        if (component != null)
        	component.repaint( );
    }

    public void hightlight() {
    	setDepth(10);
    }
    
    public void dehighlight() {
    	setDepth(0);
    }
    
    private void setDepth(final int newDepth) {
        if (component != null) {
            if (effectTimer != null)
                effectTimer.stop( );
            effectTimer = new Timer(80, new ActionListener() { 
                public void actionPerformed(ActionEvent evt) { 
                    if (depth > newDepth)
                        depth--;
                    if (depth < newDepth)
                        depth++;
                    
                    if (depth == newDepth || depth <= 0 || depth >= BORDER  ) {
                        effectTimer.stop( );
                        effectTimer = null;
                    }
                    component.repaint( );
                } 
            }); 
            effectTimer.start();
        }
    }
    
    public void setInitialDept(int depth) {
    	this.depth = depth;
    }
    
    public int getDepth() {
        return depth;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
    {    	
        Graphics2D g2 = (Graphics2D) g.create();
        
        Color color = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), borderColor.getAlpha() + (20 * depth));
        
        g2.setColor(color);
        
        g2.setStroke(new BasicStroke(width));
        
        g2.drawRect(x - 1 + width, y  - 1 + width, w - width, h - width );
        
        g2.dispose();
   }

    @Override
    public Insets getBorderInsets(Component c) {
        if (c instanceof JPopupMenu)
            return new Insets(1, 1, 1, 1);
        return new Insets(6, 6, 6, 6);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets i) {
        return i;
    }
    
    @Override
    public boolean isBorderOpaque() { return true; }

    public Color getColor( ) {
        return color;
    }

	public void click() {
		width = 2;
    }

	public void unclick() {
		new Timer(200, new ActionListener() { 
            public void actionPerformed(ActionEvent evt) { 
                width = 1;
            } 
        }).start();
	}


}
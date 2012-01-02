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

package ead.gui.extra;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import ead.gui.EAdGUILookAndFeel;

public class EAdSplitPaneDivider extends BasicSplitPaneDivider {


	private static final long serialVersionUID = 739432631859464680L;
    
	public EAdSplitPaneDivider(BasicSplitPaneUI ui) {
		super(ui);
        this.setBackground(EAdGUILookAndFeel.getBackgroundColor());
	}
	
    protected JButton createLeftOneTouchButton() {
        JButton b = new JButton() {
			private static final long serialVersionUID = 1L;
			
			public void setBorder(Border b) {
            }
			
            public void paint(Graphics g) {
                if (splitPane != null) {
                    int[]   xs = new int[3];
                    int[]   ys = new int[3];
                    int     blockSize;

                    // Fill the background first ...
                    g.setColor(this.getBackground());
                    g.fillRect(0, 0, this.getWidth(),
                               this.getHeight());

                    // ... then draw the arrow.
                    g.setColor( EAdGUILookAndFeel.getForegroundColor() );
                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        blockSize = getHeight();
                        xs[0] = blockSize;
                        xs[1] = 0;
                        xs[2] = blockSize << 1;
                        ys[0] = 0;
                        ys[1] = ys[2] = blockSize;
                        g.drawPolygon(xs, ys, 3); // Little trick to make the
                                                  // arrows of equal size
                    }
                    else {
                        blockSize = getWidth();
                        xs[0] = xs[2] = blockSize;
                        xs[1] = 0;
                        ys[0] = 0;
                        ys[1] = blockSize;
                        ys[2] = blockSize << 1;
                    }
                    g.fillPolygon(xs, ys, 3);
                }
            }
            
            // Don't want the button to participate in focus traversable.
		    public boolean isFocusTraversable() {
		    	return false;
		    }
        };
        b.setMinimumSize(new Dimension((orientation == JSplitPane.VERTICAL_SPLIT ? getHeight() : getWidth()) , 
        		(orientation == JSplitPane.VERTICAL_SPLIT ? getHeight() : getWidth())));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        return b;
    }

    /**
     * Returns the size of the divider, that is the width if the splitpane
     * is HORIZONTAL_SPLIT, or the height of VERTICAL_SPLIT.
     */
    public int getDividerSize() {
        return dividerSize - 4;
    }


    /**
     * Creates and return an instance of JButton that can be used to
     * collapse the right component in the split pane.
     */
    protected JButton createRightOneTouchButton() {
        JButton b = new JButton() {

			private static final long serialVersionUID = 1L;
			
			public void setBorder(Border border) {
            }
			
            public void paint(Graphics g) {
                if (splitPane != null) {
                    int[]          xs = new int[3];
                    int[]          ys = new int[3];
                    int            blockSize;

                    // Fill the background first ...
                    g.setColor(this.getBackground());
                    g.fillRect(0, 0, this.getWidth(),
                               this.getHeight());

                    // ... then draw the arrow.
                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        blockSize = Math.min(getHeight(), dividerSize);
                        xs[0] = blockSize;
                        xs[1] = blockSize << 1;
                        xs[2] = 0;
                        ys[0] = blockSize;
                        ys[1] = ys[2] = 0;
                    }
                    else {
                        blockSize = Math.min(getWidth(), dividerSize);
                        xs[0] = xs[2] = 0;
                        xs[1] = blockSize;
                        ys[0] = 0;
                        ys[1] = blockSize;
                        ys[2] = blockSize << 1;
                    }
                    g.setColor(Color.black);
                    g.fillPolygon(xs, ys, 3);
                }
            }
		    // Don't want the button to participate in focus traversable.
		    public boolean isFocusTraversable() {
				return false;
		    }
	    };
        b.setMinimumSize(new Dimension(dividerSize, dividerSize));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        return b;
    }

    /**
     * Paints the divider.
     */
    public void paint(Graphics g) {
      super.paint(g);
      
      if (orientation == JSplitPane.VERTICAL_SPLIT) {
          int left = (this.getWidth() - 0) / 2 - 10;
          g.drawLine(left, 1, left + 20, 1);
          g.drawLine(left, 3, left + 20, 3);
          g.drawLine(left, 5, left + 20, 5);
      }
      else {
          int top = (this.getHeight() - 0) / 2 - 10;
          g.drawLine(1, top, 1, top + 20);
          g.drawLine(3, top, 3, top + 20);
          g.drawLine(5, top, 5, top + 20);
      }

    }

    
}

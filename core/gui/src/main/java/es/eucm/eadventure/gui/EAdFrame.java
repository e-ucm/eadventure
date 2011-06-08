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

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.jdesktop.swingx.image.GaussianBlurFilter;

import es.eucm.eadventure.gui.extra.EAdBorderLayout;
import es.eucm.eadventure.gui.extra.EAdGlassPane;
import es.eucm.eadventure.utils.swing.SwingUtilities;

public class EAdFrame extends JFrame {

    private static final long serialVersionUID = -3453655199252661334L;
    
    private EAdGlassPane glass;
    
    public BufferedImageOp op;

    private BufferedImage image;
    
    private BufferedImage temp1;
    
    private BufferedImage temp2;

    private JPanel contentPane;
    
    private Component focus = null;
    
    public EAdFrame() {
        setLayout(new EAdBorderLayout());
        setBackground(EAdGUILookAndFeel.getBackgroundColor());
        
        contentPane = new JPanel();
        contentPane.setBackground(EAdGUILookAndFeel.getBackgroundColor());
        contentPane.setLayout(new BorderLayout());
        super.add( contentPane, EAdBorderLayout.CENTER);
        
    	float[] matrix = new float[25];
    	for (int i = 0; i < 25; i++)
    		matrix[i] = 1.0f/25.0f;

    	op = new GaussianBlurFilter(5);

    	glass = new EAdGlassPane(this);
        super.add( glass, EAdBorderLayout.GLASS );
    }
    
    public void paintComponents(Graphics g) {
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponents(g);
    }
    
    public void addModalPanel( final JPanel modalPanel ) {
    	if (!glass.isVisible()) {
    		focus = this.getFocusOwner();
            SwingUtilities.doInEDT(new Runnable() {
    			@Override
    			public void run() {
    				image = null;
    				
    				if (temp1 == null || temp1.getWidth() != contentPane.getWidth() || temp1.getHeight() != contentPane.getHeight()) {
    					if (temp1 != null) temp1.flush();
    					if (temp2 != null) temp2.flush();
        				temp1 = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        				temp2 = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    				}
    	    		contentPane.paint(temp1.getGraphics());
    	    		
    	    		image = op.filter(temp1, temp2);
    	            Graphics2D g2 = (Graphics2D) image.getGraphics();
    	    		g2.setColor( EAdGUILookAndFeel.getForegroundColor() );
    	            g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.6f ) );
    	            g2.fillRect( 0, 0, getWidth( ), getHeight( ) + (getJMenuBar() != null ? getJMenuBar().getHeight() : 0));
    	            g2.dispose();

    	            contentPane.setVisible(false);
    			}
            });
    	}
        SwingUtilities.doInEDT(new Runnable() {
			@Override
			public void run() {
		        glass.addPanel( modalPanel );
		        glass.validate();
				glass.repaint();
		        if (getJMenuBar( ) != null)
		            getJMenuBar( ).setEnabled( false );
			}
        });
    }
    
    public BufferedImage getImage() {
    	return image;
    }
    
    public void removeModalPanel() {
        glass.removePanel( );
        contentPane.setVisible(!glass.isVisible());
        if (!glass.isVisible()) {
	        if (this.getJMenuBar( ) != null)
	            this.getJMenuBar( ).setEnabled( true );
	        if (focus != null) {
	        	focus.requestFocusInWindow();
	        	focus = null;
	        }
        }
    }
    
    public boolean isGlassShowing() {
        return glass.isVisible( );
    }

    @Override
    public void add(Component c, Object constraints) {
    	if (c instanceof JRootPane || c instanceof EAdGlassPane)
    		super.add(c, constraints);
    	else
    		contentPane.add(c, constraints);
    }

    @Override
    public Component add(Component c) {
    	if (c instanceof JRootPane || c instanceof EAdGlassPane)
    		return super.add(c);
    	else
    		return contentPane.add(c);
    }

    @Override
    public void remove(Component c) {
    	if (c instanceof JRootPane || c instanceof EAdGlassPane)
    		super.remove(c);
    	contentPane.remove(c);
    }

    @Override
	public void setRootPane(JRootPane root) 
    {
        if(rootPane != null) {
            remove(rootPane);
        }
        rootPane = root;
        if(rootPane != null) {
            boolean checkingEnabled = isRootPaneCheckingEnabled();
            try {
                setRootPaneCheckingEnabled(false);
                super.add(rootPane, BorderLayout.CENTER);
            }
            finally {
                setRootPaneCheckingEnabled(checkingEnabled);
            }
        }
    }
    
}

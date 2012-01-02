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
 * @author Moreno-Ger, P. & Fernández-Manjón, B. (directors)
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
package ead.gui.extra;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager2;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import ead.gui.EAdFrame;

public class EAdGlassPane extends JPanel implements LayoutManager2 {

    private static final long serialVersionUID = 6165507837490314345L;

    private Stack<JPanel> panels;
    
    private Stack<BufferedImage> previous;
    
    private Stack<Component> focusComponents;
    
    private EAdFrame frame;
    
    public EAdGlassPane(EAdFrame frame) {
        panels = new Stack<JPanel>();
        focusComponents = new Stack<Component>();
        previous = new Stack<BufferedImage>();
        MouseInputAdapter mia = new MouseInputAdapter() {};
        addMouseListener( mia );
        addMouseMotionListener( mia );
        setOpaque( true );
        setVisible( false );
        this.frame = frame;
        this.setLayout(this);
    }

    @Override
    public void paintComponent(Graphics g) {
    	g.drawImage(frame.getImage(), 0, 0, getWidth(), getHeight(),
    			0, 0, frame.getImage().getWidth(), frame.getImage().getHeight(), 
    			 null);
    	
    	for (BufferedImage image : previous) {
	        int width = image.getWidth( );
	        int height = image.getHeight( );    
	        int x = ((getParent( ).getWidth( ) - width) / 2);
	        int y = ((getParent( ).getHeight( ) - height) / 2);
	        g.drawImage(image, x, y, null);
    	}
    }
    
    public void addPanel(JPanel modalPanel) {
        if (!panels.isEmpty( )) {
        	if (panels.peek() instanceof EAdModalPanel)
        		focusComponents.add( ((EAdModalPanel) panels.peek( )).getFocusComponent() );
        	else
        		focusComponents.add(panels.peek());
            BufferedImage temp = new BufferedImage(panels.peek().getWidth(), panels.peek().getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			panels.peek().paint(temp.getGraphics());
    		
    		BufferedImage image = frame.op.filter(temp, new BufferedImage(panels.peek().getWidth(), panels.peek().getHeight(), BufferedImage.TYPE_4BYTE_ABGR));
    		temp.flush();
            Graphics2D g2 = (Graphics2D) image.getGraphics();
    		g2.setColor( Color.BLACK );
            g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.6f ) );
            g2.fillRect( 0, 0, panels.peek().getWidth(), panels.peek().getHeight());
            g2.dispose();
            
            previous.add(image);
            remove(panels.peek( ));
        }
        
        this.add( modalPanel, 0 );
        
        panels.add( modalPanel );
        modalPanel.setFocusCycleRoot( true );
        setVisible( true );
        
        if (modalPanel instanceof EAdModalPanel && ((EAdModalPanel) modalPanel).getFocusComponent( ) != null)
        	((EAdModalPanel) modalPanel).getFocusComponent( ).requestFocus( );
        else
        	modalPanel.requestFocus();
        int width = modalPanel.getWidth( );
        int height = modalPanel.getHeight( );
        int x = ((getParent( ).getWidth( ) - width) / 2);
        int y = ((getParent( ).getHeight( ) - height) / 2);
        modalPanel.setBounds( x, y, width, height );
    }
    
    public void removePanel() {
        if (!panels.isEmpty( )) {
            panels.peek( ).setFocusCycleRoot( false );
            remove(panels.pop( ));
        }
        if (panels.isEmpty( ))
            setVisible(false);
        else {
        	previous.pop();
            add(panels.peek( ));
            panels.peek( ).setFocusCycleRoot( true );
            focusComponents.pop( ).requestFocusInWindow( );
            panels.peek().repaint();
            validate();
        }
    }

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return null;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void layoutContainer(Container parent) {
		if (!panels.isEmpty()) {
	        int width = panels.peek().getWidth( );
	        int height = panels.peek().getHeight( );    
	        int x = ((parent.getWidth( ) - width) / 2);
	        int y = ((parent.getHeight( ) - height) / 2);
	        panels.peek().setBounds( x, y, width, height );
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return null;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return null;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		
	}

}

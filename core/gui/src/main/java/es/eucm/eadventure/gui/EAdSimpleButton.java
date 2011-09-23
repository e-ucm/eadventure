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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.AbstractBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.utils.swing.SwingUtilities;

public class EAdSimpleButton extends JButton {

	private static final long serialVersionUID = 7971695719028514636L;

	private static final Logger logger = LoggerFactory.getLogger(EAdSimpleButton.class);

	public static enum SimpleButton { UNDO, REDO, BACKWARD, FORWARD, SEARCH, PLUS, MINUS };
	
	private ImageIcon normal;
	
	private ImageIcon selected;
	
	public EAdSimpleButton(SimpleButton simpleButton) {
		super();
		InputStream normalIs = null;
		InputStream selectedIs = null;
		switch (simpleButton) {
		case REDO:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.redo_png);
			selectedIs = ClassLoader.getSystemResourceAsStream(R.Drawable.redo_selected_png);
			break;
		case UNDO:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.undo_png);
			selectedIs = ClassLoader.getSystemResourceAsStream(R.Drawable.undo_selected_png);
			break;
		case SEARCH:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.search_png);
			selectedIs = ClassLoader.getSystemResourceAsStream(R.Drawable.search_selected_png);
			break;
		case BACKWARD:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.back_png);
			selectedIs = ClassLoader.getSystemResourceAsStream(R.Drawable.back_selected_png);
			break;
		case FORWARD:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.forward_png);
			selectedIs = ClassLoader.getSystemResourceAsStream(R.Drawable.forward_selected_png);
			break;
		default:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.redo_png);
			selectedIs = ClassLoader.getSystemResourceAsStream(R.Drawable.redo_selected_png);
		}
		try {
			normal = new ImageIcon(ImageIO.read(normalIs));
			selected = new ImageIcon(ImageIO.read(selectedIs));
			initialize();
		} catch (IOException e) {
			logger.error("Cannot load icons", e);
		}
	}
	
	public EAdSimpleButton(ImageIcon normalIcon, ImageIcon selectedIcon) {
		super();
		this.normal = normalIcon;
		this.selected = selectedIcon;
		initialize();
	}
	
	private void initialize() {
		setIcon(normal);
		setPreferredSize(new Dimension(normal.getIconWidth(), normal.getIconHeight()));
        setContentAreaFilled( false );
        setFocusPainted( false );
        setBorder(new SimpleBorder());
        this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				SwingUtilities.doInEDTNow(new Runnable() {
					public void run() {
						repaint();
					}
				});
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				SwingUtilities.doInEDTNow(new Runnable() {
					public void run() {
						repaint();
					}
				});
			}
        	
        });
        this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (isEnabled())
					setIcon(selected);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				if (isEnabled())
					setIcon(normal);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
		SwingUtilities.doInEDTNow(new Runnable() {
			public void run() {
				validate();
			}
		});
	}
	
	private class SimpleBorder extends AbstractBorder {
		
		private static final long serialVersionUID = 4669318117131018414L;

		@Override
	    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
	    	Stroke s = ((Graphics2D) g).getStroke();
	    	Color color = g.getColor();
	    	((Graphics2D) g).setStroke(new BasicStroke(1.0f));
	    	
	    	g.setColor(isFocusOwner() ? EAdGUILookAndFeel.getFocusColor(): EAdGUILookAndFeel.getForegroundColor());
    		
	    	g.drawRect(x, y, w - 1, h - 1);
	    	
	    	((Graphics2D) g).setStroke(s);
	    	g.setColor(color);
	    }
	}

	
}

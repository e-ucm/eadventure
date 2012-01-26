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

package ead.gui.eadcanvaspanel.listeners;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ead.gui.eadcanvaspanel.EAdCanvasPanel;

/**
 * Drag listener for {@link EAdCanvasPanel}. It must be added to all elements
 * contained in an {@link EAdCanvasPanel} that can be moved, through mouse and
 * keyboard. One only {@link DragListener} can be shared by all elements
 * contained in the canvas
 * 
 */
public class DragListener implements KeyListener, MouseListener,
		MouseMotionListener {

	protected static final int DEFAULT_TRANSLATION = 5;

	/**
	 * Amount of pixels component will be moved through keyboard
	 */
	protected int translation;

	/**
	 * Container for the component that will have this listener
	 */
	protected EAdCanvasPanel container;

	protected Component component;

	protected int lastX;

	protected int lastY;

	/**
	 * Constructor for the listener
	 * 
	 * @param container
	 *            Container for the component that will have this listener
	 * @param translation
	 *            Amount of pixels component will be moved through keyboard
	 */
	public DragListener(EAdCanvasPanel container, int translation) {
		this.container = container;
		this.translation = translation;
	}

	/**
	 * Constructor for the listener. Sets the amount of pixels component will be
	 * moved through keyboard to DEFAULT_TRANSLATION
	 * 
	 * @param container
	 *            Container for the component that will have this listener
	 */
	public DragListener(EAdCanvasPanel container) {
		this(container, DEFAULT_TRANSLATION);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Convendría que estás teclas fueran configurables
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			e.consume();
			move(e.getComponent(), 0, -translation);
			break;
		case KeyEvent.VK_DOWN:
			e.consume();
			move(e.getComponent(), 0, translation);
			break;
		case KeyEvent.VK_LEFT:
			e.consume();
			move(e.getComponent(), -translation, 0);
			break;
		case KeyEvent.VK_RIGHT:
			e.consume();
			move(e.getComponent(), translation, 0);
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();
		p.translate(e.getComponent().getX(), e.getComponent().getY());
		Component componentPressed = container.getComponentAt(p);

		componentPressed.requestFocus();

		container.repaint();

		if (componentPressed != container) {
			component = componentPressed;
			lastX = e.getXOnScreen();
			lastY = e.getYOnScreen();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		component = null;
		e.consume();
		container.repaint();

	}

	/**
	 * Moves an component
	 * 
	 * @param c
	 *            Component to be moved
	 * @param deltaX
	 *            Distance to be moved in X axis
	 * @param deltaY
	 *            Distance to be moved in Y axis
	 */
	protected void move(Component c, int deltaX, int deltaY) {
		int newX = c.getX() + deltaX;
		int newY = c.getY() + deltaY;
		
		if ( container.getAutosize() ){
			newX = newX < 0 ? 0 : newX;
			newY = newY < 0 ? 0 : newY;
		}

		c.setLocation(newX, newY);
		container.updateBounds(c.getBounds());
		container.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (component != null) {
			int deltaX = e.getXOnScreen() - lastX;
			int deltaY = e.getYOnScreen() - lastY;

			move(component, deltaX, deltaY);

			lastX = e.getXOnScreen();
			lastY = e.getYOnScreen();
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
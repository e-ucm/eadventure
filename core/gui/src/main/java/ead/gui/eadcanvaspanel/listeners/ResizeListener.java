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
import java.awt.event.MouseEvent;

import ead.gui.eadcanvaspanel.EAdCanvasPanel;

/**
 * 
 * Resize listener for {@link EAdCanvasPanel}. It must be added to all elements
 * contained in an {@link EAdCanvasPanel} that can be resized or moved, through
 * mouse and keyboard. One only {@link ResizeListener} can be shared by all
 * elements contained in the canvas.
 * 
 * This listener extends {@link DragListener}, so it inherits all its
 * functionality
 * 
 */
public class ResizeListener extends DragListener {

	/**
	 * 
	 * Enum for all possibles resize points
	 * 
	 */
	public enum ResizePoint {
		NO_PLACE, TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT, TOP_MIDDLE, BOTTOM_MIDDLE, TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT;
	}

	/**
	 * Minimum size for resize
	 */
	private static final int MINIMUM_SIZE = 5;

	/**
	 * Amount of pixels that are considered a component border. A component
	 * border is the place when you can drag to resize
	 */
	private int margin = 5;

	/**
	 * Current resize point for this listener
	 */
	private ResizePoint resizePoint;

	/**
	 * Constructor
	 * 
	 * @param container
	 *            Canvas which contains element that can be resized
	 */
	public ResizeListener(EAdCanvasPanel container) {
		super(container);
	}

	/**
	 * Constructor for the listener
	 * 
	 * @param container
	 *            Container for the component that will have this listener
	 * @param translation
	 *            Amount of pixels component will be moved through keyboard
	 */
	public ResizeListener(EAdCanvasPanel container, int translation) {
		super(container, translation);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		ResizePoint place = this.getResizePlace(e.getPoint(), e.getComponent()
				.getWidth(), e.getComponent().getHeight());
		changeCursor(e.getComponent(), place);
	}

	public void mousePressed(MouseEvent e) {
		resizePoint = getResizePlace(e.getPoint(), e.getComponent().getWidth(),
				e.getComponent().getHeight());
		super.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		resizePoint = ResizePoint.NO_PLACE;
		super.mouseReleased(e);

	}

	public void mouseDragged(MouseEvent e) {
		if (resizePoint != null & resizePoint != ResizePoint.NO_PLACE) {
			int deltaX = lastX - e.getXOnScreen();
			int deltaY = lastY - e.getYOnScreen();
			resizeComponent(e.getComponent(), resizePoint, deltaX, deltaY);
			lastX = e.getXOnScreen();
			lastY = e.getYOnScreen();
			changeCursor(e.getComponent(), resizePoint);
		} else
			super.mouseDragged(e);
	}

	public void keyPressed(KeyEvent e) {
		if (e.isShiftDown()) {
			int deltaX = 0, deltaY = 0;
			ResizePoint place = ResizePoint.NO_PLACE;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				deltaY = e.isControlDown() ? -translation : translation;
				place = ResizePoint.TOP_MIDDLE;
				break;
			case KeyEvent.VK_DOWN:
				deltaY = e.isControlDown() ? translation : -translation;
				place = ResizePoint.BOTTOM_MIDDLE;
				break;
			case KeyEvent.VK_LEFT:
				deltaX = e.isControlDown() ? -translation : translation;
				place = ResizePoint.MIDDLE_LEFT;
				break;
			case KeyEvent.VK_RIGHT:
				deltaX = e.isControlDown() ? translation : -translation;
				place = ResizePoint.MIDDLE_RIGHT;
				break;
			}
			if (place != ResizePoint.NO_PLACE) {
				e.consume();
				resizeComponent(e.getComponent(), place, deltaX, deltaY);
			}
		} else
			super.keyPressed(e);
	}

	/**
	 * Given a point, and the component's dimensions, returns the
	 * {@link ResizePoint}
	 * 
	 * @param p
	 *            Point to be checked
	 * @param width
	 *            Component's width
	 * @param height
	 *            Component's height
	 * @return the {@link ResizePoint}
	 */
	private ResizePoint getResizePlace(Point p, int width, int height) {
		if (p.x <= margin) {
			if (p.y <= margin) {
				return ResizePoint.TOP_LEFT;
			} else if (p.y > height - margin) {
				return ResizePoint.BOTTOM_LEFT;
			} else {
				return ResizePoint.MIDDLE_LEFT;
			}
		} else if (p.y <= margin) {
			if (p.x >= width - margin) {
				return ResizePoint.TOP_RIGHT;
			} else {
				return ResizePoint.TOP_MIDDLE;
			}
		} else if (p.x >= width - margin) {
			if (p.y > height - margin) {
				return ResizePoint.BOTTOM_RIGHT;
			} else {
				return ResizePoint.MIDDLE_RIGHT;
			}
		} else if (p.y >= height - margin) {
			return ResizePoint.BOTTOM_MIDDLE;
		} else {
			return ResizePoint.NO_PLACE;
		}
	}

	/**
	 * Changes the cursor, depending on the ResizePoint
	 * @param c
	 * @param corner
	 */
	private void changeCursor(Component c, ResizePoint corner) {

		switch (corner) {
		case NO_PLACE:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			break;
		case TOP_LEFT:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			break;
		case TOP_MIDDLE:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			break;
		case TOP_RIGHT:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			break;
		case MIDDLE_LEFT:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			break;
		case MIDDLE_RIGHT:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			break;
		case BOTTOM_LEFT:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			break;
		case BOTTOM_MIDDLE:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			break;
		case BOTTOM_RIGHT:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			break;
		default:
			c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Resize elements in selection to fit given point
	 * 
	 * @param point
	 *            Point to fit
	 */
	protected void resizeComponent(Component component,
			ResizePoint resizePosition, int deltaX, int deltaY) {

		switch (resizePosition) {
		case TOP_LEFT:
			changeHeightTop(component, deltaY);
			changeWidthLeft(component, deltaX);
			break;
		case TOP_MIDDLE:
			changeHeightTop(component, deltaY);
			break;
		case TOP_RIGHT:
			changeHeightTop(component, deltaY);
			changeWidthRight(component, deltaX);
			break;
		case MIDDLE_LEFT:
			changeWidthLeft(component, deltaX);
			break;
		case MIDDLE_RIGHT:
			changeWidthRight(component, deltaX);
			break;
		case BOTTOM_LEFT:
			changeHeightBottom(component, deltaY);
			changeWidthLeft(component, deltaX);
			break;
		case BOTTOM_MIDDLE:
			changeHeightBottom(component, deltaY);
			break;
		case BOTTOM_RIGHT:
			changeHeightBottom(component, deltaY);
			changeWidthRight(component, deltaX);
			break;
		}

		if (resizePosition != ResizePoint.NO_PLACE) {
			container.updateBounds(component.getBounds());
		}

	}

	private void changeHeightTop(Component component, int deltaY) {

		int height = component.getHeight();
		int y = component.getY();
		height += deltaY;
		y -= deltaY;
		component.setLocation(component.getX(), height > MINIMUM_SIZE ? y
				: component.getY());
		component.setSize(component.getWidth(), height > MINIMUM_SIZE ? height
				: MINIMUM_SIZE);

	}

	private void changeHeightBottom(Component component, int deltaY) {

		int height = component.getHeight();
		height -= deltaY;
		component.setSize(component.getWidth(), height > MINIMUM_SIZE ? height
				: MINIMUM_SIZE);

	}

	private void changeWidthLeft(Component component, int deltaX) {

		int width = component.getWidth();
		int x = component.getX();
		width += deltaX;
		x -= deltaX;
		component.setSize(width > MINIMUM_SIZE ? width : MINIMUM_SIZE,
				component.getHeight());
		component.setLocation(width > MINIMUM_SIZE ? x : component.getX(),
				component.getY());
	}

	private void changeWidthRight(Component component, int deltaX) {

		int width = component.getWidth();
		width -= deltaX;
		component.setSize(width > MINIMUM_SIZE ? width : MINIMUM_SIZE,
				component.getHeight());

	}

}

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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.view.asset;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import javax.swing.JViewport;

/**
 * A zoomable JPanel. Catches mouse scrolling and translates it into zooming
 * (x-axis, y-axis or both) + translation within the containing JScrollPane
 * to keep the center point always centered.
 *
 * @author mfreire
 */
public class ZoomablePanel extends JPanel {

	private double zoomFactor = 1.08;
	private boolean zoomXAxis = true;
	private boolean zoomYAxis = true;

	protected int width;
	protected int height;

	protected int baseWidth;
	protected int baseHeight;

	private Point last = new Point();
	private boolean dragging = false;
	private Component maxSizeReference;

	public ZoomablePanel(boolean zoomXAxis, boolean zoomYAxis) {

		this.zoomXAxis = zoomXAxis;
		this.zoomYAxis = zoomYAxis;
		// mouse listener to detect scrollwheel events
		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				updatePreferredSize(e.getWheelRotation(), e.getPoint());
			}
		});

		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		MouseAdapter listener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dragging = false;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				dragging = true;
				last = e.getPoint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragging) {
					Point p = e.getPoint();
					int offX = p.x - last.x;
					int offY = p.y - last.y;
					Point next = new Point(((JViewport) getParent())
							.getViewPosition());
					next.x += offX;
					next.y += offY;

					// clamp
					next.x = Math.max(0, next.x);
					next.x = Math.min(width - getParent().getWidth(), next.x);
					next.y = Math.max(0, next.y);
					next.y = Math.min(height - getParent().getHeight(), next.y);

					((JViewport) getParent()).setViewPosition(next);

					last = p;
				}
			}
		};
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}

	public void setMaxSizeReference(Component maxSizeReference) {
		this.maxSizeReference = maxSizeReference;
	}

	public void resetZoom() {
		int pw = maxSizeReference.getWidth() - 4;
		int ph = maxSizeReference.getHeight() - 4;
		double zoomX = pw * 1.0 / baseWidth;
		double zoomY = ph * 1.0 / baseHeight;
		double z = Math.min(zoomX, zoomY);
		last = new Point();
		updateZoom(z);
	}

	public void resetZoomNative() {
		updateZoom(1);
	}

	private void updateZoom(double zoom) {
		width = (int) (baseWidth * zoom);
		height = (int) (baseHeight * zoom);
		zoom(last, 1, 1);
	}

	private void updatePreferredSize(int n, Point p) {
		double dx = (double) n * zoomFactor;
		double dy = (double) n * zoomFactor;
		dx = (n > 0) ? 1 / dx : -dx;
		dy = (n > 0) ? 1 / dy : -dy;
		if (!zoomXAxis) {
			dx = 1;
		}
		if (!zoomYAxis) {
			dy = 1;
		}

		width = (int) (getWidth() * dx);
		height = (int) (getHeight() * dy);
		zoom(p, dx, dy);
	}

	private void zoom(Point p, double dx, double dy) {
		System.err.println("to request size of " + width + "x" + height);
		setPreferredSize(new Dimension(width, height));

		if (p != null) {
			int offX = (int) (p.x * dx) - p.x;
			int offY = (int) (p.y * dy) - p.y;
			setLocation(getLocation().x - offX, getLocation().y - offY);
		}

		getParent().doLayout();
		last = p;
	}
}
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

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * EAdPolygon contains functionality for add points, remove points, move and
 * scale.
 * 
 * @author Ángel Serrano
 * 
 */
public class EAdPolygon extends Polygon {

	private static final long serialVersionUID = -8627942457116457302L;

	/**
	 * Constructor for a rectangular EAdPolygon
	 * 
	 * @param x
	 *            Coordinate x for top-left position of polygon
	 * @param y
	 *            Coordinate y for top-left position of polygon
	 * @param width
	 *            Polygon's width
	 * @param height
	 *            Polygon's height
	 */
	public EAdPolygon(int x, int y, int width, int height) {

		addPoint(x, y);
		addPoint(x + width, y);
		addPoint(x + width, y + height);
		addPoint(x, y + height);
	}

	/**
	 * Constructor for an empty polygon
	 */
	public EAdPolygon() {
		super();
	}

	public EAdPolygon(int[] xpoints, int[] ypoints, int npoints) {
		super(xpoints, ypoints, npoints);
	}

	public EAdPolygon(Polygon p) {
		for (int i = 0; i < p.npoints; i++) {
			addPoint(xpoints[i], ypoints[i]);
		}
	}

	/**
	 * Returns an EAdPoint list with all points forming the polygon
	 * 
	 * @return an EAdPoint list with all points forming the polygon
	 */
	public List<Point> getPoints() {

		ArrayList<Point> list = new ArrayList<Point>();
		for (int i = 0; i < npoints; i++) {
			list.add(new Point(xpoints[i], ypoints[i]));
		}
		return list;
	}

	/**
	 * Returns whether polygon is rectangular
	 * 
	 * @return Whether polygon is rectangular
	 */
	public boolean isRectangular() {

		return (npoints == 4 && ypoints[0] == ypoints[1] && xpoints[0] == xpoints[3] && ypoints[2] == ypoints[3] && xpoints[2] == xpoints[1]);

	}

	/**
	 * Returns polygon height
	 * 
	 * @return Polygon height
	 */
	public int getHeight() {

		return (int) getBounds().getHeight();
	}

	/**
	 * Returns polygon width
	 * 
	 * @return Polygon width
	 */
	public int getWidth() {

		return (int) getBounds().getWidth();
	}

	/**
	 * Returns polygon top-left corner X coordinate
	 * 
	 * @return Polygon top-left corner X coordinate
	 */
	public int getX() {

		return (int) getBounds().getX();
	}

	/**
	 * Returns polygon top-left corner Y coordinate
	 * 
	 * @return Polygon top-left corner Y coordinate
	 */
	public int getY() {

		return (int) getBounds().getY();
	}

	/**
	 * Sets bounds for polygon, translating and / or scaling if required
	 * 
	 * @param x
	 *            X coordinate for top-left position of polygon
	 * @param y
	 *            Y coordinate for top-left position of polygon
	 * @param width
	 *            Polygon's width
	 * @param height
	 *            Polygon's height
	 */
	public void setBounds(int x, int y, int width, int height) {

		float scaleX = (float) width / (float) getWidth();
		float scaleY = (float) height / (float) getHeight();

		scale(scaleX, scaleY);

		int deltaX = x - getX();
		int deltaY = y - getY();

		translate(deltaX, deltaY);

		updateBounds();
	}

	/**
	 * Scales polygon
	 * 
	 * @param scaleX
	 *            Scale for X axis
	 * @param scaleY
	 *            Scale for Y axis
	 */
	public void scale(float scaleX, float scaleY) {

		if (scaleX != 1.0f || scaleY != 1.0f) {
			for (int i = 0; i < npoints; i++) {
				xpoints[i] = Math.round(xpoints[i] * scaleX);
				ypoints[i] = Math.round(ypoints[i] * scaleY);
			}

			updateBounds();
		}
	}

	private void updateBounds() {

		if (npoints > 0) {
			int maxX, minX;
			int maxY, minY;
			maxX = minX = xpoints[0];
			maxY = minY = ypoints[0];

			for (int i = 1; i < npoints; i++) {
				if (xpoints[i] > maxX)
					maxX = xpoints[i];
				if (xpoints[i] < minX)
					minX = xpoints[i];
				if (ypoints[i] > maxY)
					maxY = ypoints[i];
				if (ypoints[i] < minY)
					minY = ypoints[i];
			}

			if (bounds == null) {
				bounds = new Rectangle();
			}

			bounds.x = minX;
			bounds.y = minY;
			bounds.width = maxX - minX;
			bounds.height = maxY - minY;
		}
	}

	/**
	 * Remove a point from polygon
	 * 
	 * @param index
	 *            Index of point to be removed
	 */
	public void removePoint(int index) {

		if (index >= 0 && index < npoints) {
			for (int i = index; i < npoints - 1; i++) {
				xpoints[i] = xpoints[i + 1];
				ypoints[i] = ypoints[i + 1];
			}
			npoints--;
			updateBounds();
		}
	}

	/**
	 * Sets X coordinate for top-left position of polygon
	 * 
	 * @param newX
	 *            X coordinate for top-left position of polygon
	 */
	public void setX(int newX) {

		int deltaX = newX - getX();
		translate(deltaX, 0);
	}

	/**
	 * Sets Y coordinate for top-left position of polygon
	 * 
	 * @param newY
	 *            Y coordinate for top-left position of polygon
	 */
	public void setY(int newY) {

		int deltaY = newY - getY();

		translate(0, deltaY);
	}

	/**
	 * Sets and existing point of the polygon in a new location
	 * 
	 * @param nPoint
	 *            Index of point to be relocated
	 * @param newLocation
	 *            New location of the point
	 */
	public void setPointLocation(int nPoint, Point newLocation) {
		if (nPoint < 0 || nPoint >= this.npoints) {
			throw new IllegalArgumentException("nPoint must be greater than 0 and less than npoints");
		} else {
			this.xpoints[nPoint] = newLocation.x;
			this.ypoints[nPoint] = newLocation.y;
			updateBounds();
		}
	}

	@Override
	public Object clone() {

		EAdPolygon p = new EAdPolygon();
		for (int i = 0; i < npoints; i++) {
			p.addPoint(xpoints[i], ypoints[i]);
		}
		return p;
	}

}

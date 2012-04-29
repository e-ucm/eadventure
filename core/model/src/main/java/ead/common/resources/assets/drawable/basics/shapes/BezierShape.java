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

package ead.common.resources.assets.drawable.basics.shapes;

import ead.common.interfaces.Param;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.params.fills.Paint;
import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.drawable.basics.EAdShape;
import ead.common.util.EAdPosition;

public class BezierShape implements EAdShape, Cloneable {

	@Param("paint")
	private EAdPaint paint;

	@Param("closed")
	private boolean closed;

	@Param("points")
	private EAdList<Integer> points;

	@Param("paintAsVector")
	private boolean paintAsVector;

	public BezierShape() {
		points = new EAdListImpl<Integer>(Integer.class);
		paint = Paint.TRANSPARENT;
		paintAsVector = false;
		closed = false;
	}

	public BezierShape(EAdPaint paint) {
		this();
		this.paint = paint;
	}

	/**
	 * @return Returns true if the bezier shape must be painted as a vector
	 *         image instead of a bitmap image. A bitmap image is faster, and it
	 *         is the best option if the shape is not going to be scaled. The
	 *         vector image is slower, and should be used when the shape is
	 *         going to be scaled during game time. The default value is false;
	 * 
	 */
	public boolean isPaintAsVector() {
		return paintAsVector;
	}

	/**
	 * Sets if the bezier shape must be rendered as a vector image
	 * (paintAsVector is true) or as a bitmap image (paintAsVector is false). A
	 * bitmap image is faster, and it is the best option if the shape is not
	 * going to be scaled. The vector image is slower, and should be used when
	 * the shape is going to be scaled during game time. The default value is
	 * false.
	 * 
	 * @param paintAsVector
	 */
	public void setPaintAsVector(boolean paintAsVector) {
		this.paintAsVector = paintAsVector;
	}

	public BezierShape(int x, int y) {
		this();
		moveTo(x, y);
	}

	/**
	 * Resets the shape and sets the initial point to x and y
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void moveTo(int x, int y) {
		points.add(x);
		points.add(y);
		closed = false;
	}

	/**
	 * <p>
	 * Return the color with which to draw the shape.
	 * </p>
	 * <p>
	 * The color can have alpha = 0 if the shape is expected to be invisible. As
	 * it uses {@link EAdBorderedColor} it can be used to paint just the outline
	 * of the shape.
	 * </p>
	 * 
	 * @return The color of the shape
	 */
	public EAdPaint getPaint() {
		return paint;
	}

	/**
	 * Sets the paint for the shape
	 * 
	 * @param paint
	 *            the paint
	 */
	public void setPaint(EAdPaint paint) {
		this.paint = paint;
	}

	public void lineTo(EAdPosition p) {
		checkMoveTo();
		points.add(1);
		points.add(p.getX());
		points.add(p.getY());
	}

	private void checkMoveTo() {
		if (points.size() == 0) {
			points.add(0);
			points.add(0);
		}
	}

	public void lineTo(int x, int y) {
		this.lineTo(new EAdPosition(x, y));
	}

	public void quadTo(EAdPosition p1, EAdPosition p2) {
		checkMoveTo();
		points.add(2);
		points.add(p1.getX());
		points.add(p1.getY());
		points.add(p2.getX());
		points.add(p2.getY());

	}

	public void curveTo(EAdPosition p1, EAdPosition p2, EAdPosition p3) {
		checkMoveTo();
		points.add(3);
		points.add(p1.getX());
		points.add(p1.getY());
		points.add(p2.getX());
		points.add(p2.getY());
		points.add(p3.getX());
		points.add(p3.getY());
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isClosed() {
		return closed;
	}

	public EAdList<Integer> getPoints() {
		return points;
	}

	public void quadTo(int x1, int y1, int x2, int y2) {
		checkMoveTo();
		quadTo(new EAdPosition(x1, y1), new EAdPosition(x2, y2));
	}

	public Object clone() {
		BezierShape s = new BezierShape();
		s.closed = closed;
		for (Integer p : points) {
			s.points.add(p);
		}
		s.paint = paint;
		return s;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public String toString() {
		String pointsText = "";
		for (Integer i : points) {
			pointsText += i + ";";
		}
		return this.paintAsVector + ";" + closed + ";"
				+ (paint != null ? paint.toStringData() : "") + ";"
				+ pointsText;
	}

	public boolean equals(Object o) {
		if (o instanceof BezierShape) {
			BezierShape b = (BezierShape) o;
			return this.toString().equals(b.toString());
		}
		return false;
	}

	public void setPoints(EAdList<Integer> points) {
		this.points = points;
	}

}

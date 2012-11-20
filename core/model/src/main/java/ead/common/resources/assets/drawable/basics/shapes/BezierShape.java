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
import ead.common.util.EAdPosition;

public class BezierShape extends AbstractShape {

	@Param("closed")
	private boolean closed;

	@Param("points")
	private EAdList<Integer> points;

	public BezierShape() {
		points = new EAdListImpl<Integer>(Integer.class);
		this.setPaint(Paint.TRANSPARENT);
		closed = false;
	}

	public BezierShape(EAdPaint paint) {
		this();
		this.setPaint(paint);
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
		curveTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3
				.getY());
	}

	public void curveTo(int x1, int y1, int x2, int y2, int x3, int y3) {
		checkMoveTo();
		points.add(3);
		points.add(x1);
		points.add(y1);
		points.add(x2);
		points.add(y2);
		points.add(x3);
		points.add(y3);
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
		s.setPaint(getPaint());
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
		return closed + ";"
				+ (getPaint() != null ? getPaint().toStringData() : "") + ";"
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

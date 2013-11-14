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

package es.eucm.ead.model.assets.drawable.basics.shapes;

import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.util.Position;

public class BezierShape extends AbstractShape {

	@Param
	private boolean closed;

	@Param
	private EAdList<Integer> points;

	public BezierShape() {
		points = new EAdList<Integer>();
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

	public void lineTo(Position p) {
		checkMoveTo();
		points.add(1);
		points.add((int) p.getX());
		points.add((int) p.getY());
	}

	private void checkMoveTo() {
		if (points.size() == 0) {
			points.add(0);
			points.add(0);
		}
	}

	public void lineTo(int x, int y) {
		this.lineTo(new Position(x, y));
	}

	public void quadTo(Position p1, Position p2) {
		checkMoveTo();
		points.add(2);
		points.add((int) p1.getX());
		points.add((int) p1.getY());
		points.add((int) p2.getX());
		points.add((int) p2.getY());

	}

	public void curveTo(Position p1, Position p2, Position p3) {
		curveTo((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2
				.getY(), (int) p3.getX(), (int) p3.getY());
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
		quadTo(new Position(x1, y1), new Position(x2, y2));
	}

	public void setPoints(EAdList<Integer> points) {
		this.points = points;
	}

	@Override
	public EAdShape copyShape() {
		BezierShape s = new BezierShape();
		s.closed = closed;
		for (Integer p : points) {
			s.points.add(p);
		}
		s.setPaint(getPaint());
		return s;
	}

	/**
	 * Translates the shape to match its top left corner with (0, 0)
	 * 
	 * @return the translation made in X and Y axis
	 */
	public Integer[] removeOffset() {
		int i = 0;
		int minX = points.get(i++);
		int minY = points.get(i++);
		while (i < getPoints().size()) {
			int points = getPoints().get(i++);
			boolean x = true;
			for (int j = 0; j < points * 2; j++) {
				int value = getPoints().get(i++);
				if (x) {
					minX = Math.min(value, minX);
				} else {
					minY = Math.min(value, minY);
				}
				x = !x;
			}
		}
		EAdList<Integer> newPoints = new EAdList<Integer>();
		i = 0;
		newPoints.add(points.get(i++) - minX);
		newPoints.add(points.get(i++) - minY);
		while (i < getPoints().size()) {
			int points = getPoints().get(i++);
			newPoints.add(points);
			boolean x = true;
			for (int j = 0; j < points * 2; j++) {
				int value = getPoints().get(i++);
				if (x) {
					newPoints.add(value - minX);
				} else {
					newPoints.add(value - minY);
				}
				x = !x;
			}
		}
		this.setPoints(newPoints);
		return new Integer[] { minX, minY };
	}
}

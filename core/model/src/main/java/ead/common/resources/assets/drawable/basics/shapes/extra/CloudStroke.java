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

package ead.common.resources.assets.drawable.basics.shapes.extra;

import ead.common.resources.assets.drawable.basics.shapes.BezierShape;

public class CloudStroke extends RectangleStroke {

	private static final int OFFSET = 20;

	private static final int STEP = 30;

	@Override
	public void addHorizontal(BezierShape shape, int x1, int x2, int y,
			int xOrigin, int yOrigin) {
		int xOffset = STEP / 2;
		int yOffset = -OFFSET;
		int xStep = STEP;
		int segments1 = Math.abs(x1 - xOrigin) / xStep;
		int diff = x2 - xOrigin;
		int x3 = xOrigin + diff / 10;
		int segments2 = Math.abs(x3 - x2) / STEP;
		if (x1 > x2) {
			yOffset = -yOffset;
			xStep = -xStep;
			xOffset = -xOffset;
		}
		addSegments(shape, x1, y, xOffset, yOffset, xStep, 0, segments1);
		addOrigin(shape, xOrigin, y, x3, y, xOrigin, yOrigin);
		addSegments(shape, x3, y, xOffset, yOffset, xStep, 0, segments2);

	}

	@Override
	public void addVertical(BezierShape shape, int x, int y1, int y2,
			int xOrigin, int yOrigin) {
		int yOffset = STEP / 2;
		int xOffset = OFFSET;
		int yStep = STEP;
		int segments1 = Math.abs(y1 - yOrigin) / yStep;
		int diff = y2 - yOrigin;
		int y3 = yOrigin + diff / 10;
		int segments2 = Math.abs(y3 - y2) / STEP;
		if (y2 < y1) {
			yStep = -yStep;
			xOffset = -xOffset;
			yOffset = -yOffset;
		}
		addSegments(shape, x, y1, xOffset, yOffset, 0, yStep, segments1);
		addOrigin(shape, x, yOrigin, x, y3, xOrigin, yOrigin);
		addSegments(shape, x, y3, xOffset, yOffset, 0, yStep, segments2);

	}

	@Override
	public void addHorizontal(BezierShape shape, int x1, int x2, int y) {
		int xOffset = STEP / 2;
		int yOffset = -OFFSET;
		int xStep = STEP;
		int segments = Math.abs(x1 - x2) / xStep;
		if (x1 > x2) {
			yOffset = -yOffset;
			xStep = -xStep;
			xOffset = -xOffset;
		}
		addSegments(shape, x1, y, xOffset, yOffset, xStep, 0, segments);

	}

	@Override
	public void addVertical(BezierShape shape, int x, int y1, int y2) {
		int yOffset = STEP / 2;
		int xOffset = OFFSET;
		int yStep = STEP;
		int segments = Math.abs(y1 - y2) / yStep;
		if (y2 < y1) {
			yStep = -yStep;
			xOffset = -xOffset;
			yOffset = -yOffset;
		}
		addSegments(shape, x, y1, xOffset, yOffset, 0, yStep, segments);

	}

	@Override
	public void addDiagonal(BezierShape shape, int x1, int x2, int y1,
			int y2, int xOrigin, int yOrigin) {
		this.addOrigin(shape, x1, y1, x2, y2, xOrigin, yOrigin);

	}

	protected void addSegments(BezierShape shape, int x, int y,
			int xOffset, int yOffset, int xStep, int yStep, int segments) {
		int oldX = x;
		int oldY = y;
		for (int i = 0; i < segments; i++) {
			x += xStep;
			y += yStep;
			addSegment(shape, oldX + xOffset, oldY + yOffset, x, y);
			oldX = x;
			oldY = y;
		}

	}

	protected void addSegment(BezierShape shape, int x1, int y1, int x2,
			int y2) {
		shape.quadTo(x1, y1, x2, y2);
	}

	@Override
	public void addDiagonal(BezierShape shape, int x1, int x2, int y1,
			int y2) {
		int corner[] = this.getCornerFromDiagonal(x1, x2, y1, y2);

		int xOffset = OFFSET;
		int yOffset = OFFSET;

		if (x2 == corner[0] && corner[1] < y2)
			yOffset = -yOffset;
		else if (y2 < corner[1] && x2 == corner[0]) {
			xOffset = -xOffset;
		} else if (x2 > corner[0] && corner[1] == y2) {
			xOffset = -xOffset;
			yOffset = -yOffset;
		}

		addSegment(shape, corner[0] + xOffset, corner[1] + yOffset, x2, y2);
	}

	protected void addOrigin(BezierShape shape, int x1, int y1, int x2,
			int y2, int xOrigin, int yOrigin) {
		shape.quadTo(xOrigin, yOrigin, x2, y2);
	}
}

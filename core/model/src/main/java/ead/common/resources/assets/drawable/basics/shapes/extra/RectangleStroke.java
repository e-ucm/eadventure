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

public class RectangleStroke implements BalloonStroke {

	@Override
	public void addHorizontal(BezierShape shape, int x1, int x2, int y,
			int xOrigin, int yOrigin) {
		shape.lineTo(xOrigin, y);
		shape.lineTo(xOrigin, yOrigin);
		int diff = x2 - xOrigin;
		diff = Math.abs(diff) > 20 ? 20 * (int) Math.signum(x2 - xOrigin) : diff;
		

		shape.lineTo(xOrigin + diff, y);
		shape.lineTo(x2, y);
	}

	@Override
	public void addHorizontal(BezierShape shape, int x1, int x2, int y) {
		shape.lineTo(x2, y);

	}

	@Override
	public void addVertical(BezierShape shape, int x, int y1, int y2) {
		shape.lineTo(x, y2);
	}

	@Override
	public void addVertical(BezierShape shape, int x, int y1, int y2,
			int xOrigin, int yOrigin) {
		shape.lineTo(x, yOrigin);
		shape.lineTo(xOrigin, yOrigin);
		int diff = y2 - yOrigin;

		shape.lineTo(x, yOrigin + diff );
		shape.lineTo(x, y2);

	}

	@Override
	public void addDiagonal(BezierShape shape, int x1, int x2, int y1,
			int y2) {
		int corner[] = this.getCornerFromDiagonal(x1, x2, y1, y2);
		shape.lineTo(corner[0], corner[1]);
		shape.lineTo(x2, y2);
	}

	@Override
	public void addDiagonal(BezierShape shape, int x1, int x2, int y1,
			int y2, int xOrigin, int yOrigin) {
		shape.lineTo(xOrigin, yOrigin);
		shape.lineTo(x2, y2);

	}

	protected int[] getCornerFromDiagonal(int x1, int x2, int y1, int y2) {
		int corner[] = new int[2];
		if ((x1 < x2 && y1 < y2) || (x1 > x2 && y1 > y2)) {
			corner[0] = x2;
			corner[1] = y1;
		} else if ((x1 > x2 && y1 < y2) || (x1 < x2 && y1 > y2)) {
			corner[0] = x1;
			corner[1] = y2;
		}
		return corner;
	}

}
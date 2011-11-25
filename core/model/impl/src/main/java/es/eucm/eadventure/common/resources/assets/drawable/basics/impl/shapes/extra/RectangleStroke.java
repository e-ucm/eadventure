package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra;

import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;

public class RectangleStroke implements BalloonStroke {

	@Override
	public void addHorizontal(BezierShape shape, int x1, int x2, int y,
			int xOrigin, int yOrigin) {
		shape.lineTo(xOrigin, y);
		shape.lineTo(xOrigin, yOrigin);
		int diff = x2 - xOrigin;
		diff = Math.abs(diff) > 40 ? 40 * (int) Math.signum(x2 - xOrigin) : diff;
		

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
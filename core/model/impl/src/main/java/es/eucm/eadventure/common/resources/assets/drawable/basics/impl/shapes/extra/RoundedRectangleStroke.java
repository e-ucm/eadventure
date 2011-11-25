package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra;

import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;

public class RoundedRectangleStroke extends RectangleStroke {

	@Override
	public void addDiagonal(BezierShape shape, int x1, int x2, int y1,
			int y2) {
		int corner[] = this.getCornerFromDiagonal(x1, x2, y1, y2);
		shape.cubeTo(corner[0], corner[1], x2, y2);
	}

}

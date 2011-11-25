package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra;

import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;

public class ElectricStroke extends CloudStroke {

	

	@Override
	protected void addSegment(BezierShape shape, int x1, int y1, int x2,
			int y2) {
		shape.lineTo(x1, y1);
		shape.lineTo(x2, y2);
	}

	@Override
	protected void addOrigin(BezierShape shape, int x1, int y1, int x2,
			int y2, int xOrigin, int yOrigin) {
		shape.lineTo(xOrigin, yOrigin);
		shape.lineTo(x2, y2);
	}

}
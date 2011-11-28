package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes;

import es.eucm.eadventure.common.interfaces.features.enums.Orientation;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra.BalloonStroke;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra.BalloonType;

public class BallonShape extends BezierShape {

	private static int CORNER_RADIUS = 10;

	public BallonShape(){
		
	}

	public BallonShape(int left, int top, int right, int bottom,
			BalloonType ballonType) {
		this(left, top, right, bottom, ballonType, false, -1, -1);
	}

	public BallonShape(int left, int top, int right, int bottom,
			BalloonType ballonType, int xOrigin, int yOrigin) {
		this(left, top, right, bottom, ballonType, true, xOrigin, yOrigin);
	}

	private BallonShape(int left, int top, int right, int bottom,
			BalloonType ballonType, boolean hasOrigin, int xOrigin, int yOrigin) {

		Orientation originSide = null;
		if (hasOrigin)
			originSide = getOriginLocation(left, top, right, bottom, xOrigin,
					yOrigin);

		hasOrigin = hasOrigin && originSide != null;

		BalloonStroke stroke = ballonType.getStroke();

		int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		for (Orientation side : Orientation.values()) {
			switch (side) {
			case N:
				y1 = top;
				y2 = top;
				x1 = left + CORNER_RADIUS;
				x2 = right - CORNER_RADIUS;
				moveTo(x1, y1);
				break;
			case S:
				y1 = bottom;
				y2 = bottom;
				x1 = right - CORNER_RADIUS;
				x2 = left + CORNER_RADIUS;
				break;
			case E:
				y1 = top + CORNER_RADIUS;
				y2 = bottom - CORNER_RADIUS;
				x1 = right;
				x2 = right;
				break;
			case W:
				y1 = bottom - CORNER_RADIUS;
				y2 = top + CORNER_RADIUS;
				x1 = left;
				x2 = left;
				break;
			case NE:
				x1 = right - CORNER_RADIUS;
				y1 = top;
				x2 = right;
				y2 = top + CORNER_RADIUS;
				break;
			case SE:
				x1 = right;
				y1 = bottom - CORNER_RADIUS;
				x2 = right - CORNER_RADIUS;
				y2 = bottom;
				break;
			case SW:
				x1 = left + CORNER_RADIUS;
				y1 = bottom;
				x2 = left;
				y2 = bottom - CORNER_RADIUS;
				break;
			case NW:
				x1 = left;
				y1 = top + CORNER_RADIUS;
				x2 = left + CORNER_RADIUS;
				y2 = top;
				break;
			}

			switch (side) {
			case N:
			case S:
				if (hasOrigin && originSide == side)
					stroke.addHorizontal(this, x1, x2, y1, xOrigin, yOrigin);
				else
					stroke.addHorizontal(this, x1, x2, y1);
				break;
			case E:
			case W:
				if (hasOrigin && originSide == side)
					stroke.addVertical(this, x1, y1, y2, xOrigin, yOrigin);
				else
					stroke.addVertical(this, x1, y1, y2);
				break;
			case NW:
			case SW:
			case NE:
			case SE:
				if (hasOrigin && originSide == side)
					stroke.addDiagonal(this, x1, x2, y1, y2, xOrigin, yOrigin);
				else
					stroke.addDiagonal(this, x1, x2, y1, y2);
				break;

			}
		}
		setClosed(true);
	}

	public static Orientation getOriginLocation(int left, int top, int right,
			int bottom, int xOrigin, int yOrigin) {
		if (xOrigin < left) {
			if (yOrigin < top)
				return Orientation.NW;
			else if (yOrigin > bottom)
				return Orientation.SW;
			else
				return Orientation.W;
		} else if (xOrigin > right) {
			if (yOrigin < top)
				return Orientation.NE;
			else if (yOrigin > bottom)
				return Orientation.SE;
			else
				return Orientation.E;
		} else if (yOrigin < top) {
			return Orientation.N;
		} else if (yOrigin > bottom) {
			return Orientation.S;
		} else
			return null;
	}

}

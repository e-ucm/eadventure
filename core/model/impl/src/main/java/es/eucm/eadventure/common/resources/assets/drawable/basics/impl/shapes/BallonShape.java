package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes;

import es.eucm.eadventure.common.interfaces.features.Oriented.Orientation;

public class BallonShape extends BezierShape {

	private static int CORNER_RADIUS = 10;

	public enum BalloonType {
		RECTANGLE, ROUNDED_RECTANGLE, CLOUD, ELECTRIC;

		public BalloonStroke getStroke() {
			switch (this) {
			case RECTANGLE:
				return new RectangleStroke();
			case ROUNDED_RECTANGLE:
				return new RoundedRectangleStroke();
			case CLOUD:
				return new CloudStroke();
			case ELECTRIC:
				return new ElectricStroke();
			}
			return null;
		}
		
	}
	
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

	public interface BalloonStroke {
		void addHorizontal(BezierShape shape, int x1, int x2, int y,
				int xOrigin, int yOrigin);

		void addHorizontal(BezierShape shape, int x1, int x2, int y);

		void addVertical(BezierShape shape, int x, int y1, int y2);

		void addVertical(BezierShape shape, int x, int y1, int y2, int xOrigin,
				int yOrigin);

		void addDiagonal(BezierShape shape, int x1, int x2, int y1, int y2);

		void addDiagonal(BezierShape shape, int x1, int x2, int y1, int y2,
				int xOrigin, int yOrigin);
	}

	public static class RectangleStroke implements BalloonStroke {

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

	public static class RoundedRectangleStroke extends RectangleStroke {

		@Override
		public void addDiagonal(BezierShape shape, int x1, int x2, int y1,
				int y2) {
			int corner[] = this.getCornerFromDiagonal(x1, x2, y1, y2);
			shape.cubeTo(corner[0], corner[1], x2, y2);
		}

	}

	public static class CloudStroke extends RectangleStroke {

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
			shape.cubeTo(x1, y1, x2, y2);
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
			shape.cubeTo(xOrigin, yOrigin, x2, y2);
		}
	}

	public static class ElectricStroke extends CloudStroke {

		

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

}

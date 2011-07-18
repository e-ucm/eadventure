package es.eucm.eadventure.common.resources.assets.drawable.impl;

import es.eucm.eadventure.common.interfaces.Oriented.Orientation;

public class BallonShape extends BezierShape {
	
	private int cornerRadius = 10;

	public enum BallonType {
		RECTANGLE;

		public BallonStroke getStroke() {
			switch (this) {
			case RECTANGLE:
				return new RectangleStroke();
			}
			return null;
		}
	}
	
	public BallonShape( int left, int top, int right, int bottom, BallonType ballonType ){
		this( left, top, right, bottom, ballonType, false, -1, -1 );
	}
	
	public BallonShape( int left, int top, int right, int bottom, BallonType ballonType, int xOrigin, int yOrigin ){
		this( left, top, right, bottom, ballonType, true, xOrigin, yOrigin );
	}

	private BallonShape(int left, int top, int right, int bottom,
			BallonType ballonType, boolean hasOrigin, int xOrigin, int yOrigin) {
		
		Orientation originSide = null;
		if (hasOrigin)
			originSide = getOriginLocation(left, top, right, bottom, xOrigin,
					yOrigin);
		
		hasOrigin = hasOrigin && originSide != null;

		BallonStroke stroke = ballonType.getStroke();
		
		
		int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		for (Orientation side : Orientation.values()) {
			switch (side) {
			case N:
				y1 = top;
				y2 = top;
				x1 = left + cornerRadius;
				x2 = right - cornerRadius;
				moveTo(x1, y1);
				break;
			case S:
				y1 = bottom;
				y2 = bottom;
				x1 = right - cornerRadius;
				x2 = left + cornerRadius;
				break;
			case E:
				y1 = top + cornerRadius;
				y2 = bottom - cornerRadius;
				x1 = right;
				x2 = right;
				break;
			case W:
				y1 = bottom - cornerRadius;
				y2 = top + cornerRadius;
				x1 = left;
				x2 = left;
				break;
			case NE:
				x1 = right - cornerRadius;
				y1 = top;
				x2 = right;
				y2 = top + cornerRadius;
				break;
			case SE:
				x1 = right;
				y1 = bottom - cornerRadius;
				x2 = right - cornerRadius;
				y2 = bottom;
				break;
			case SW:
				x1 = left + cornerRadius;
				y1 = bottom;
				x2 = left;
				y2 = bottom - cornerRadius;
				break;
			case NW:
				x1 = left;
				y1 = top + cornerRadius;
				x2 = left + cornerRadius;
				y2 = top;
				break;
			}
			
			switch (side) {
			case N:
			case S:
				if (hasOrigin && originSide == side)
					stroke.addHorizontal(this, x1, x2, y1, xOrigin,
							yOrigin);
				else
					stroke.addHorizontal(this, x1, x2, y1);
				break;
			case E:
			case W:
				if (hasOrigin && originSide == side)
					stroke.addVertical(this, x1, y1, y2, xOrigin,
							yOrigin);
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
		close();

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

	public interface BallonStroke {
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

	public static class RectangleStroke implements BallonStroke {

		@Override
		public void addHorizontal(BezierShape shape, int x1, int x2, int y,
				int xOrigin, int yOrigin) {
			shape.lineTo(xOrigin, y);
			shape.lineTo(xOrigin, yOrigin);
			int diff = x2 - xOrigin;
			
			shape.lineTo(xOrigin + diff / 10, y);
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
			
			shape.lineTo(x, yOrigin + diff / 10);
			shape.lineTo(x, y2);

		}

		@Override
		public void addDiagonal(BezierShape shape, int x1, int x2, int y1,
				int y2) {
			shape.lineTo(x2, y2);
		}

		@Override
		public void addDiagonal(BezierShape shape, int x1, int x2, int y1,
				int y2, int xOrigin, int yOrigin) {
			shape.lineTo(xOrigin, yOrigin );
			shape.lineTo(x2, y2);

		}

	}

}

package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes;

public class LineShape extends BezierShape {

	public LineShape(int x1, int y1, int x2, int y2, int width) {
		int vy = x2 - x1;
		int vx = y2 - y1;
		float module = (float) Math.sqrt(vx * vx + vy * vy);
		vy = (int) (((float) vy / module) * width);
		vx = (int) (((float) vx / module) * width);

		moveTo(x1 - vx, y1 - vy);
		lineTo(x1 + vx, y1 + vy);
		lineTo(x2 + vx, y2 + vy);
		lineTo(x2 - vx, y2 - vy);
		close();
	}

}

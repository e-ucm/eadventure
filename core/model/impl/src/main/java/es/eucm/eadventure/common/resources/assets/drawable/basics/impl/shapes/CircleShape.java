package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes;

public class CircleShape extends BezierShape {

	public CircleShape(int cx, int cy, int radius) {
		int segments = radius * radius;

		float angle = (float) (2 * Math.PI / segments);
		float accAngle = angle;
		moveTo(cx, cy + radius);

		for (int i = 0; i < segments - 2; i++) {
			int x = (int) (Math.sin(accAngle) * radius) + cx;
			int y = (int) (Math.cos(accAngle) * radius) + cy;
			lineTo(x, y);
			accAngle += angle;
		}

		close();
	}

}

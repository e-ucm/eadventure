package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes;

import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.paint.EAdFill;

public class CircleShape extends BezierShape {
	
	public CircleShape(){
		
	}

	public CircleShape(int cx, int cy, int radius, int segments, EAdFill fill) {
		super( fill );
		float angle = (float) (2 * Math.PI / segments);
		float accAngle = angle;

		for (int i = 0; i < segments; i++) {
			int x = (int) (Math.cos(accAngle) * radius) + cx;
			int y = (int) (Math.sin(accAngle) * radius) + cy;
			if (i == 0)
				moveTo(x, y);
			else
				lineTo(x, y);
			accAngle += angle;
		}

		close();
	}
	
	public CircleShape(int cx, int cy, int radius, int segments){
		this( cx, cy, radius, segments, EAdBorderedColor.WHITE_ON_BLACK);
	}

}

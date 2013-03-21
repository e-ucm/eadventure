package ead.converter.subconverters;

import java.awt.Point;

import com.google.inject.Singleton;

import ead.common.model.assets.drawable.basics.shapes.AbstractShape;
import ead.common.model.assets.drawable.basics.shapes.BezierShape;
import ead.common.model.assets.drawable.basics.shapes.RectangleShape;
import ead.common.model.params.paint.EAdPaint;
import es.eucm.eadventure.common.data.chapter.Rectangle;

@Singleton
public class RectangleConverter {

	public AbstractShape convert(Rectangle r, EAdPaint fill) {
		AbstractShape shape = null;
		if (r.isRectangular()) {
			shape = new RectangleShape(r.getWidth(), r.getHeight(), fill);
		} else {
			BezierShape bezier = new BezierShape(fill);
			boolean first = true;
			for (Point p : r.getPoints()) {
				if (first) {
					bezier.moveTo(p.x, p.y);
				} else {
					bezier.lineTo(p.x, p.y);
				}
				first = false;
			}
			shape = bezier;
		}
		return shape;
	}

}

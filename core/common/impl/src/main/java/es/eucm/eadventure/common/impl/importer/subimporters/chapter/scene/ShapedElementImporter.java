package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import java.awt.Point;

import es.eucm.eadventure.common.data.chapter.Rectangle;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.Shape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;

public class ShapedElementImporter {

	protected Shape importShape(Rectangle oldObject, EAdBasicSceneElement newElement) {
		Shape shape = null;
		if (oldObject.isRectangular()) {
			shape = new RectangleShape(oldObject.getWidth(), oldObject.getHeight());
			newElement.setPosition(new EAdPosition(EAdPosition.Corner.TOP_LEFT,
					oldObject.getX(), oldObject.getY()));
			
		} else {
			shape = null;
			int i = 0;
			for (Point p : oldObject.getPoints()) {
				if ( i == 0 )
					shape = new BezierShape(p.x, p.y);
				else
					((BezierShape) shape).lineTo(p.x, p.y);
				i++;
			}
			((BezierShape) shape).close();
			newElement.setPosition(new EAdPosition(EAdPosition.Corner.TOP_LEFT, 0,
					0));
		}
		// FIXME deleted when exits were working
		shape.setColor(EAdBorderedColor.BLACK_ON_WHITE);
		return shape;
	}
	
}

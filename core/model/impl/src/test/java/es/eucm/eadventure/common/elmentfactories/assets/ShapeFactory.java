package es.eucm.eadventure.common.elmentfactories.assets;

import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.Shape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.IrregularShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;

public class ShapeFactory {

	public enum ShapeType {
		RECTANGULAR_SHAPE, CIRCLE_SHAPE, TRIANGLE_SHAPE, IRREGULAR_RANDOM_SHAPE, IRREGULAR_SHAPE_1;
	}

	public Shape getElement(Enum<?> type, int width, int height,
			EAdBorderedColor color) {
		Shape s = null;
		if (type instanceof ShapeType) {
			switch ((ShapeType) type) {
			case CIRCLE_SHAPE:
				s = createCircle(width);
				break;
			case TRIANGLE_SHAPE:
				s = createTriangle(width, height);
				break;
			case IRREGULAR_RANDOM_SHAPE:
				s = createRandomIrregularShape( width, height );
				break;
			case IRREGULAR_SHAPE_1:
				s = createIrregularShape1( width, height );
				break;
			default:
				s = new RectangleShape(width, height);
			}
		} else {
			s = new RectangleShape(width, height);
		}
		((BezierShape) s).setColor(color);
		return s;
	}

	public Shape createCircle(int width) {
		IrregularShape circle = new IrregularShape();
		int points = width;
		float angle = (float) (2 * Math.PI / points);
		float acc = 0;
		// Radius
		width = width / 2;

		for (int i = 0; i < points; i++) {
			int x = (int) (Math.cos(acc) * width);
			int y = (int) (Math.sin(acc) * width);
			x += width;
			y += width;
			circle.getPositions().add(new EAdPosition(x, y));
			acc += angle;
		}
		return circle;
	}

	public Shape createTriangle(int width, int height) {
		IrregularShape triangle = new IrregularShape();
		triangle.getPositions().add(new EAdPosition(width / 2, 0));
		triangle.getPositions().add(new EAdPosition(0, height));
		triangle.getPositions().add(new EAdPosition(width, height));
		return triangle;
	}

	public Shape createRandomIrregularShape(int width, int height) {
		int nPoints = width / 5;
		IrregularShape shape = new IrregularShape();
		for (int i = 0; i < nPoints; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			shape.getPositions().add(new EAdPosition(x, y));
		}
		return shape;
	}
	
	public Shape createIrregularShape1(int width, int height) {
		IrregularShape shape = new IrregularShape();
		shape.getPositions().add(new EAdPosition( width / 5, 0 ));
		shape.getPositions().add(new EAdPosition( width - width / 5, 0));
		shape.getPositions().add(new EAdPosition( width, height));
		shape.getPositions().add(new EAdPosition( width - width / 5, height));
		shape.getPositions().add(new EAdPosition( width / 2, height / 2));
		shape.getPositions().add(new EAdPosition( width / 5, height));
		return shape;
	}

}

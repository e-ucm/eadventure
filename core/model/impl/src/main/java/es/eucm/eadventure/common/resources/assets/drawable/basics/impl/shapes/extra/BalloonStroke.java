package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra;

import com.gwtent.reflection.client.Reflectable;

import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;

@Reflectable
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
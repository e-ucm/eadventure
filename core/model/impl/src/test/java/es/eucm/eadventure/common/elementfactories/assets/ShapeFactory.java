/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.eadventure.common.elementfactories.assets;

import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class ShapeFactory {

	public enum ShapeType {
		RECTANGULAR_SHAPE, CIRCLE_SHAPE, TRIANGLE_SHAPE, IRREGULAR_RANDOM_SHAPE, IRREGULAR_SHAPE_1, DROP_SHAPE;
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
			case DROP_SHAPE:
				s = createDropShape( width, height );
				break;
			default:
				s = new RectangleShape(width, height);
			}
		} else {
			s = new RectangleShape(width, height);
		}
		((BezierShape) s).setFill(color);
		return s;
	}

	public Shape createCircle(int width) {
		
		int points = width;
		float angle = (float) (2 * Math.PI / points);
		float acc = 0;
		// Radius
		width = width / 2;

		BezierShape circle = new BezierShape(width * 2, width);
		for (int i = 1; i < points; i++) {
			acc += angle;
			int x = (int) (Math.cos(acc) * width);
			int y = (int) (Math.sin(acc) * width);
			x += width;
			y += width;
			circle.lineTo(x, y);
		}
		circle.close();
		return circle;
	}

	public Shape createTriangle(int width, int height) {
		BezierShape triangle = new BezierShape(width / 2, 0);
		triangle.lineTo(new EAdPositionImpl(width, height));
		triangle.lineTo(new EAdPositionImpl(0, height));
		triangle.close();
		return triangle;
	}

	public Shape createRandomIrregularShape(int width, int height) {
		int nPoints = width / 5;
		BezierShape shape = new BezierShape(0, 0);
		for (int i = 0; i < nPoints; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			shape.lineTo(new EAdPositionImpl(x, y));
		}
		
		shape.close();
		return shape;
	}
	
	public Shape createIrregularShape1(int width, int height) {
		BezierShape shape = new BezierShape(width / 5, 0);
		shape.lineTo(new EAdPositionImpl( width - width / 5, 0));
		shape.lineTo(new EAdPositionImpl( width, height));
		shape.lineTo(new EAdPositionImpl( width - width / 5, height));
		shape.lineTo(new EAdPositionImpl( width / 2, height / 2));
		shape.lineTo(new EAdPositionImpl( width / 5, height));
		shape.close();
		return shape;
	}
	
	public Shape createDropShape( int width, int height ){
		BezierShape shape = new BezierShape( width / 2, 0 );
		shape.lineTo(width, 2 * height / 3 );
		shape.cubeTo(width, height, width / 2, height );
		shape.cubeTo(0, height, 0, 2 * height / 3 );
		shape.close();
		return shape;
	}

}

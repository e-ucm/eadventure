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

package ead.demos.elementfactories.assets;

import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.BezierShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.CircleShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.util.Position;

public class ShapeFactory {

	public enum ShapeType {
		RECTANGULAR_SHAPE, CIRCLE_SHAPE, TRIANGLE_SHAPE, IRREGULAR_RANDOM_SHAPE, IRREGULAR_SHAPE_1, DROP_SHAPE;
	}

	public EAdShape getElement(Enum<?> type, int width, int height, Paint color) {
		EAdShape s = null;
		if (type instanceof ShapeType) {
			switch ((ShapeType) type) {
			case CIRCLE_SHAPE:
				s = createCircle(width);
				break;
			case TRIANGLE_SHAPE:
				s = createTriangle(width, height);
				break;
			case IRREGULAR_RANDOM_SHAPE:
				s = createRandomIrregularShape(width, height);
				break;
			case IRREGULAR_SHAPE_1:
				s = createIrregularShape1(width, height);
				break;
			case DROP_SHAPE:
				s = createDropShape(width, height);
				break;
			default:
				s = new RectangleShape(width, height);
			}
		} else {
			s = new RectangleShape(width, height);
		}
		((EAdShape) s).setPaint(color);
		return s;
	}

	public EAdShape createCircle(int width) {
		return new CircleShape(width / 2);
	}

	public BezierShape createTriangle(int width, int height) {
		BezierShape triangle = new BezierShape(width / 2, 0);
		triangle.lineTo(new Position(width, height));
		triangle.lineTo(new Position(0, height));
		triangle.setClosed(true);
		return triangle;
	}

	public BezierShape createRandomIrregularShape(int width, int height) {
		int nPoints = width / 5;
		BezierShape shape = new BezierShape(0, 0);
		for (int i = 0; i < nPoints; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			shape.lineTo(new Position(x, y));
		}

		shape.setClosed(true);
		return shape;
	}

	public BezierShape createIrregularShape1(int width, int height) {
		BezierShape shape = new BezierShape(width / 5, 0);
		shape.lineTo(new Position(width - width / 5, 0));
		shape.lineTo(new Position(width, height));
		shape.lineTo(new Position(width - width / 5, height));
		shape.lineTo(new Position(width / 2, height / 2));
		shape.lineTo(new Position(width / 5, height));
		shape.setClosed(true);
		return shape;
	}

	public BezierShape createDropShape(int width, int height) {
		BezierShape shape = new BezierShape(width / 2, 0);
		shape.lineTo(width, 2 * height / 3);
		shape.quadTo(width, height, width / 2, height);
		shape.quadTo(0, height, 0, 2 * height / 3);
		shape.setClosed(true);
		return shape;
	}

}

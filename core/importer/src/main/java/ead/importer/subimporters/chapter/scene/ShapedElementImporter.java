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

package ead.importer.subimporters.chapter.scene;

import java.awt.Point;

import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.BezierShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.eadventure.common.data.chapter.Rectangle;

public class ShapedElementImporter {

	public static EAdShape importShape(Rectangle oldObject) {
		EAdShape shape = null;
		int x = oldObject.getX();
		int y = oldObject.getY();
		if (oldObject.isRectangular() || oldObject.getPoints().size() == 0) {
			oldObject.setRectangular(true);
			shape = new RectangleShape(oldObject.getWidth(), oldObject
					.getHeight());

		} else {
			shape = null;
			int i = 0;

			for (Point p : oldObject.getPoints()) {
				if (i == 0)
					shape = new BezierShape(p.x - x, p.y - y);
				else
					((BezierShape) shape).lineTo(p.x - x, p.y - y);
				i++;
			}
			((BezierShape) shape).setClosed(true);
		}
		return shape;
	}

	public static es.eucm.ead.model.params.util.Rectangle getBounds(
			Rectangle oldObject) {
		if (oldObject.isRectangular() || oldObject.getPoints().size() == 0) {
			oldObject.setRectangular(true);
			return new es.eucm.ead.model.params.util.Rectangle(oldObject.getX(),
					oldObject.getY(), oldObject.getWidth(), oldObject
							.getHeight());

		} else {
			int maxX = Integer.MIN_VALUE;
			int minX = Integer.MAX_VALUE;
			int maxY = Integer.MIN_VALUE;
			int minY = Integer.MAX_VALUE;
			for (Point p : oldObject.getPoints()) {
				maxX = (int) (p.getX() > maxX ? p.getX() : maxX);
				maxY = (int) (p.getY() > maxY ? p.getY() : maxY);
				minX = (int) (p.getX() < minX ? p.getX() : minX);
				minY = (int) (p.getY() < minY ? p.getY() : minY);
			}

			return new es.eucm.ead.model.params.util.Rectangle(minX, minY, maxX
					- minX, maxY - minY);

		}
	}

}

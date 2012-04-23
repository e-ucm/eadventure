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

package ead.engine.core.platform.assets.drawable.basics;

import playn.core.Canvas;
import playn.core.Path;

import com.google.inject.Inject;

import ead.common.model.elements.extra.EAdList;
import ead.engine.core.EAdEngine;
import ead.engine.core.platform.assets.drawables.basics.RuntimeBezierShape;

public class PlayNBezierShape extends RuntimeBezierShape<Canvas> {

	private Path path;

	private EAdEngine eAdEngine;

	@Inject
	public PlayNBezierShape(EAdEngine eAdEngine) {
		this.eAdEngine = eAdEngine;
	}

	@Override
	public boolean loadAsset() {
		// FIXME support paint as bitmap image
		if (eAdEngine == null)
			return false;
		super.loadAsset();
		path = eAdEngine.getGraphics().createPath();
		
		EAdList<Integer> points = descriptor.getPoints();
		
		path.moveTo(points.get(0), points.get(1));

		int pointIndex = 2;
		float x1, y1, x2, y2, x3, y3;

		while (pointIndex < points.size()) {
			int length = points.get(pointIndex++);
			switch (length) {
			case 1:
				x1 = points.get(pointIndex++);
				y1 = points.get(pointIndex++);
				path.lineTo(x1, y1);
				break;
			case 2:
				x1 = points.get(pointIndex++);
				y1 = points.get(pointIndex++);
				x2 = points.get(pointIndex++);
				y2 = points.get(pointIndex++);
				path.quadraticCurveTo(x1, y1, x2, y2);
				break;
			case 3:
				x1 = points.get(pointIndex++);
				y1 = points.get(pointIndex++);
				x2 = points.get(pointIndex++);
				y2 = points.get(pointIndex++);
				x3 = points.get(pointIndex++);
				y3 = points.get(pointIndex++);
//				path.curveTo(x1, y1, x2, y2, x3, y3);
				break;
			default:

			}
		}

		if (descriptor.isClosed())
			path.close();

		this.loaded = true;
		return true;
	}

	public Path getShape() {
		return path;
	}

	@Override
	public boolean contains(int x, int y) {
		return x > 0 && y > 0 && x < getWidth() && y < getHeight();
	}

	@Override
	public void freeMemory() {
		if (path != null) {
			path.reset();
			path = null;
		}
	}

}

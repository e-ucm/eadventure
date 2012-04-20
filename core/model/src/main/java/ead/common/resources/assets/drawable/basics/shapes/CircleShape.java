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

package ead.common.resources.assets.drawable.basics.shapes;

import ead.common.params.fills.Paint;
import ead.common.params.paint.EAdPaint;

public class CircleShape extends BezierShape {
	
	public CircleShape( ){
		
	}

	public CircleShape(int cx, int cy, int radius, int segments, EAdPaint paint) {
		super(paint);
		int points = segments;
		float angle = (float) (2 * Math.PI / points);
		float acc = 0;

		moveTo(cx + radius, cy);
		for (int i = 0; i < points - 1; i++) {
			acc += angle;
			int x = (int) (Math.cos(acc) * radius);
			int y = (int) (Math.sin(acc) * radius);
			lineTo(x + cx, y + cy);
		}
		setClosed(true);
	}

	public CircleShape(int cx, int cy, int radius, int segments) {
		this(cx, cy, radius, segments, Paint.WHITE_ON_BLACK);
	}

}

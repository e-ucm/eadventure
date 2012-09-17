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

import ead.common.interfaces.Param;
import ead.common.params.fills.Paint;
import ead.common.params.paint.EAdPaint;

public class CircleShape extends AbstractShape {

	@Param("radius")
	private int radius;

	public CircleShape() {

	}

	public CircleShape(int radius, EAdPaint paint) {
		super(paint);
		this.radius = radius;
	}

	public CircleShape(int radius) {
		this(radius, Paint.WHITE_ON_BLACK);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int hashCode() {
		return (radius + "" + getPaint() + "").hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof CircleShape) {
			CircleShape c = (CircleShape) o;
			return c.radius == radius && getPaint() != null
					&& getPaint().equals(c.getPaint());
		}
		return false;
	}

}

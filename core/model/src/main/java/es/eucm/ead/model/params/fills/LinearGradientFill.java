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

package es.eucm.ead.model.params.fills;

import es.eucm.ead.model.params.AbstractParam;
import es.eucm.ead.model.params.paint.EAdFill;

/**
 * Linear gradient fill
 * 
 */
public class LinearGradientFill extends AbstractParam implements EAdFill {

	public static final String SEPARATOR = ";";

	public static LinearGradientFill BLACK = new LinearGradientFill(
			ColorFill.BLACK, ColorFill.BLACK, 0, 0, 1, 1);

	private float x0, y0, x1, y1;

	private ColorFill color1;

	private ColorFill color2;

	/**
	 * Creates a linear gradient fill with the default value
	 * {@link LinearGradientFill#BLACK}
	 */
	public LinearGradientFill() {
		this(BLACK.color1, BLACK.color2, BLACK.x0, BLACK.y0, BLACK.x1, BLACK.y1);
	}

	/**
	 * Constructs a linear gradient fill.
	 * 
	 * @param color1
	 *            Start color
	 * @param color2
	 *            End color
	 * @param vertical
	 *            if the gradient is vertical (or horizontal)
	 */
	public LinearGradientFill(ColorFill color1, ColorFill color2, float x0,
			float y0, float x1, float y1) {
		this.color1 = color1;
		this.color2 = color2;
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
	}

	public LinearGradientFill(String string) {
		parse(string);
	}

	public LinearGradientFill(ColorFill c1, ColorFill c2, int width,
			int height, boolean vertical) {
		this(c1, c2, 0, 0, vertical ? 0 : width, vertical ? height : 0);
	}

	public LinearGradientFill(ColorFill c1, ColorFill c2, int width, int height) {
		this(c1, c2, width, height, true);
	}

	/**
	 * Returns the first color
	 * 
	 * @return
	 */
	public ColorFill getColor1() {
		return color1;
	}

	/**
	 * Returns the second color
	 * 
	 * @return
	 */
	public ColorFill getColor2() {
		return color2;
	}

	public float getX0() {
		return x0;
	}

	public float getX1() {
		return x1;
	}

	public float getY0() {
		return y0;
	}

	public float getY1() {
		return y1;
	}

	@Override
	public EAdFill getBorder() {
		return null;
	}

	@Override
	public EAdFill getFill() {
		return this;
	}

	@Override
	public int getBorderWidth() {
		return 0;
	}

	public String toString() {
		return toStringData();
	}

	@Override
	public String toStringData() {
		return color1.toStringData() + SEPARATOR + color2.toStringData()
				+ SEPARATOR + x0 + SEPARATOR + y0 + SEPARATOR + x1 + SEPARATOR
				+ y1;
	}

	@Override
	public boolean parse(String data) {
		boolean error = false;

		if (data == null) {
			error = true;
		} else {
			String temp[] = data.split(SEPARATOR);
			if (temp.length == 6) {
				int i = 0;
				try {
					color1 = new ColorFill(temp[i++]);
					color2 = new ColorFill(temp[i++]);
					x0 = Float.parseFloat(temp[i++]);
					y0 = Float.parseFloat(temp[i++]);
					x1 = Float.parseFloat(temp[i++]);
					y1 = Float.parseFloat(temp[i++]);
				} catch (NumberFormatException e) {
					error = true;
				}
			} else {
				error = true;
			}
		}

		if (error) {
			color1 = BLACK.color1;
			color2 = BLACK.color2;
			x0 = BLACK.x0;
			x1 = BLACK.x1;
			y0 = BLACK.y0;
			y1 = BLACK.y1;
		}
		return !error;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + Float.floatToIntBits(this.x0);
		hash = 13 * hash + Float.floatToIntBits(this.y0);
		hash = 13 * hash + Float.floatToIntBits(this.x1);
		hash = 13 * hash + Float.floatToIntBits(this.y1);
		hash = 13 * hash + (this.color1 != null ? this.color1.hashCode() : 0);
		hash = 13 * hash + (this.color2 != null ? this.color2.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LinearGradientFill o = (LinearGradientFill) obj;
		return (x0 == o.x0) && (y0 == o.y0) && (x1 == o.x1) && (y1 == o.y1)
				&& color1.equals(o.color1) && color2.equals(o.color2);
	}
}

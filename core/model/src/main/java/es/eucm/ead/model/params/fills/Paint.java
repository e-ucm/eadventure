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
import es.eucm.ead.model.params.paint.EAdPaint;

/**
 * <p>
 * This class represents a paint, with a fill and a border
 * </p>
 */
public class Paint extends AbstractParam implements EAdPaint {

	public static final String SEPARATOR = ":";

	/**
	 * Basic black border and white center EAdBordered color
	 */
	public static final Paint BLACK_ON_WHITE = new Paint(ColorFill.WHITE,
			ColorFill.BLACK);

	/**
	 * Basic white border and black center EAdBordered color
	 */
	public static final Paint WHITE_ON_BLACK = new Paint(ColorFill.BLACK,
			ColorFill.WHITE);

	/**
	 * Transparent color
	 */
	public static final Paint TRANSPARENT = new Paint(ColorFill.TRANSPARENT,
			ColorFill.TRANSPARENT);

	/**
	 * The color of the center
	 */
	private EAdFill fill;

	/**
	 * The color of the border
	 */
	private EAdFill border;

	/**
	 * Border width
	 */
	private int width = 1;

	public Paint(String string) {
		parse(string);
	}

	public Paint(EAdFill fill, EAdFill border) {
		this(fill, border, 1);
	}

	public Paint(EAdFill center, EAdFill border, int width) {
		this.fill = center;
		this.border = border;
		this.width = width;
	}

	@Override
	public String toString() {
		return toStringData();
	}

	/**
	 * Set the color of the border
	 * 
	 * @param color
	 *            the color of the border
	 */
	public void setBorderColor(EAdFill color) {
		this.border = color;
	}

	/**
	 * Set the color of the center
	 * 
	 * @param fill
	 *            the color of the center
	 */
	public void setFill(EAdFill fill) {
		this.fill = fill;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public String toStringData() {
		return (fill != null ? fill : ColorFill.BLACK).toStringData()
				+ SEPARATOR
				+ (border != null ? border : ColorFill.BLACK).toStringData()
				+ SEPARATOR + width;
	}

	@Override
	public boolean parse(String data) {
		boolean error = data == null;
		if (!error) {
			String temp[] = data.split(SEPARATOR);
			if (temp.length == 3) {

				// First fill
				if (temp[0].length() == 10) {
					ColorFill c = new ColorFill();
					error = !c.parse(temp[0]) || error;
					setFill(c);
				} else {
					LinearGradientFill fill = new LinearGradientFill();
					error = !fill.parse(temp[0]) || error;
					setFill(fill);
				}

				// Second fill
				if (temp[1].length() == 10) {
					ColorFill c = new ColorFill();
					error = !c.parse(temp[1]) || error;
					setBorderColor(c);
				} else {
					LinearGradientFill fill = new LinearGradientFill();
					error = !fill.parse(temp[1]) || error;
					setBorderColor(fill);
				}

				// Width
				try {
					width = Integer.parseInt(temp[2]);
				} catch (NumberFormatException e) {
					error = true;
				}

			} else {
				error = true;
			}
		}

		if (error) {
			this.fill = BLACK_ON_WHITE.fill;
			this.border = BLACK_ON_WHITE.border;
			this.width = BLACK_ON_WHITE.width;
		}

		return !error;
	}

	@Override
	public EAdFill getBorder() {
		return border;
	}

	@Override
	public EAdFill getFill() {
		return fill;
	}

	@Override
	public int getBorderWidth() {
		return width;
	}

}

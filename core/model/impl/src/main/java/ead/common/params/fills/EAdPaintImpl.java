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

package ead.common.params.fills;

import java.util.logging.Logger;

import ead.common.params.EAdParamImpl;
import ead.common.params.paint.EAdFill;

/**
 * <p>
 * This class represents a paint, with a fill and a border
 * </p>
 */
public class EAdPaintImpl extends EAdParamImpl implements EAdFill {

	public static final String SEPARATOR = ":";

	private static final Logger logger = Logger.getLogger("EAdPaintImpl");

	/**
	 * Basic black border and white center EAdBordered color
	 */
	public static final EAdPaintImpl BLACK_ON_WHITE = new EAdPaintImpl(
			EAdColor.WHITE, EAdColor.BLACK);

	/**
	 * Basic white border and black center EAdBordered color
	 */
	public static final EAdPaintImpl WHITE_ON_BLACK = new EAdPaintImpl(
			EAdColor.BLACK, EAdColor.WHITE);

	/**
	 * Transparent color
	 */
	public static final EAdPaintImpl TRANSPARENT = new EAdPaintImpl(
			EAdColor.TRANSPARENT, EAdColor.TRANSPARENT);

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

	public EAdPaintImpl(String string) {
		parse(string);
	}

	public EAdPaintImpl(EAdFill fill, EAdFill border) {
		this(fill, border, 1);
	}

	public EAdPaintImpl(EAdFill center, EAdFill border, int width) {
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
		if (fill == null)
			logger.warning("Null fill");
		if (border == null)
			logger.warning("Null border");
		return (fill != null ? fill : EAdColor.BLACK).toStringData()
				+ SEPARATOR
				+ (border != null ? border : EAdColor.BLACK).toStringData()
				+ SEPARATOR + width;
	}

	@Override
	public void parse(String data) {
		String temp[] = data.split(SEPARATOR);
		if (temp[0].length() == 10)
			setFill(new EAdColor(temp[0]));
		else
			setFill(new EAdLinearGradient(temp[0]));
		if (temp[1].length() == 10)
			setBorderColor(new EAdColor(temp[1]));
		else
			setBorderColor(new EAdLinearGradient(temp[1]));
		width = Integer.parseInt(temp[2]);
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

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof EAdPaintImpl))
			return false;
		EAdPaintImpl paint = (EAdPaintImpl) object;
		if (paint.border.equals(border) && paint.fill.equals(fill)
				&& paint.width == width)
			return true;
		return false;
	}

}

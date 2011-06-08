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

package es.eucm.eadventure.common.model.params;

/**
 * <p>
 * This class represents a set of two colors, one representing the center and
 * one representing the border.
 * </p>
 */
public class EAdBorderedColor {

	//TODO Could add border thickness?
	
	/**
	 * Basic black border and white center EAdBordered color
	 */
	public static final EAdBorderedColor BLACK_ON_WHITE = new EAdBorderedColor(
			EAdColor.WHITE, EAdColor.BLACK);

	/**
	 * Basic white border and black center EAdBordered color
	 */
	public static final EAdBorderedColor WHITE_ON_BLACK = new EAdBorderedColor(
			EAdColor.BLACK, EAdColor.WHITE);

	/**
	 * Transparent color
	 */
	public static final EAdBorderedColor TRANSPARENT = new EAdBorderedColor(
			EAdColor.TRANSPARENT, EAdColor.TRANSPARENT);

	/**
	 * The color of the center
	 */
	private EAdColor centerColor;

	/**
	 * The color of the border
	 */
	private EAdColor borderColor;

	public EAdBorderedColor(EAdColor centerColor, EAdColor borderColor) {
		this.centerColor = centerColor;
		this.borderColor = borderColor;
	}

	@Override
	public String toString() {
		return centerColor.toString() + ":" + borderColor.toString();
	}

	/**
	 * Parses a string and returns the corresponding border color.
	 * 
	 * @param string
	 *            A string representing a bordered color
	 * @return The EAdBorderedColor
	 */
	public static EAdBorderedColor valueOf(String string) {
		String temp[] = string.split(":");
		EAdBorderedColor color = new EAdBorderedColor(
				EAdColor.valueOf(temp[0]), EAdColor.valueOf(temp[1]));
		return color;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof EAdBorderedColor) {
			EAdBorderedColor temp = (EAdBorderedColor) o;
			if (temp.borderColor.equals(borderColor)
					&& temp.centerColor.equals(centerColor))
				return true;
		}
		return false;

	}

	/**
	 * Returns the color of the border
	 * 
	 * @return the color of the border
	 */
	public EAdColor getBorderColor() {
		return borderColor;
	}

	/**
	 * Returns the color of the center
	 * 
	 * @return the color of the center
	 */
	public EAdColor getCenterColor() {
		return centerColor;
	}

	/**
	 * Set the color of the border
	 * 
	 * @param color
	 *            the color of the border
	 */
	public void setBorderColor(EAdColor color) {
		this.borderColor = color;
	}

	/**
	 * Set the color of the center
	 * 
	 * @param color
	 *            the color of the center
	 */
	public void setCenterColor(EAdColor color) {
		this.centerColor = color;
	}

}

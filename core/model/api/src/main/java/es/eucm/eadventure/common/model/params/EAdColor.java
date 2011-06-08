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
 * This class represents a basic color in the eAdventure model.
 * </p>
 * 
 */
public class EAdColor {

	/**
	 * White EAdColor
	 */
	public static EAdColor WHITE = new EAdColor(255, 255, 255);

	/**
	 * Black EAdColor
	 */
	public static EAdColor BLACK = new EAdColor(0, 0, 0);

	/**
	 * Transparent EAdColor
	 */
	public static EAdColor TRANSPARENT = new EAdColor(0, 0, 0, 0);

	/**
	 * Red EAdColor
	 */
	public static EAdColor RED = new EAdColor(255, 0, 0);

	/**
	 * The red value of the color
	 */
	private int red;

	/**
	 * The green value of the color
	 */
	private int green;

	/**
	 * The blue value of the color
	 */
	private int blue;

	/**
	 * The alpha value of the color
	 */
	private int alpha;

	/**
	 * Create a new white color
	 */
	public EAdColor() {
		this(255, 255, 255, 255);
	}

	/**
	 * Create a new color with the given RGB values
	 * 
	 * @param red
	 *            The red value
	 * @param green
	 *            The green value
	 * @param blue
	 *            The blue value
	 */
	public EAdColor(int red, int green, int blue) {
		this(red, green, blue, 255);
	}

	/**
	 * Create a new color with the given RGBA values
	 * 
	 * @param red
	 *            The red value
	 * @param green
	 *            The green value
	 * @param blue
	 *            The blue value
	 * @param alpha
	 *            The alpha value
	 */
	public EAdColor(int red, int green, int blue, int alpha) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
		setAlpha(alpha);
	}

	@Override
	public String toString() {
		String color = "0x";
		color += valueToString(red);
		color += valueToString(green);
		color += valueToString(blue);
		color += valueToString(alpha);
		return color;
	}

	private String valueToString(int value) {
		String temp = Integer.toHexString(value);
		if (temp.length() < 2)
			temp = "00" + temp;
		return temp.substring(temp.length() - 2);
	}

	/**
	 * Parse the value of a color from a string. If string is {@code null}
	 * returns {@link EAdColor#TRANSPARENT}
	 * 
	 * @param string
	 *            The string with the color value
	 * @return The EAdColor represented by the string
	 */
	public static EAdColor valueOf(String string) {
		if (string != null) {
			EAdColor color = new EAdColor();
			color.setRed(Integer.parseInt(string.substring(2, 4), 16));
			color.setGreen(Integer.parseInt(string.substring(4, 6), 16));
			color.setBlue(Integer.parseInt(string.substring(6, 8), 16));
			color.setAlpha(Integer.parseInt(string.substring(8, 10), 16));
			return color;
		} else
			return EAdColor.TRANSPARENT;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof EAdColor) {
			EAdColor temp = (EAdColor) o;
			if (temp.red == red && temp.blue == blue && temp.green == green
					&& temp.alpha == alpha)
				return true;
		}
		return false;

	}

	/**
	 * Returns the red value
	 * 
	 * @return the red value
	 */
	public int getRed() {
		return red;
	}

	/**
	 * Sets the red value
	 * 
	 * @param red
	 *            the red value of the color
	 */
	public void setRed(int red) {
		this.red = normalize(red);
	}

	/**
	 * Returns the green value
	 * 
	 * @return the green value of the color
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * Sets the green value
	 * 
	 * @param green
	 *            the green value of the color
	 */
	public void setGreen(int green) {
		this.green = normalize(green);
	}

	/**
	 * Returns the blue value
	 * 
	 * @return the blue value of the color
	 */
	public int getBlue() {
		return blue;
	}

	/**
	 * Sets the blue value
	 * 
	 * @param blue
	 *            the blue value of the color
	 */
	public void setBlue(int blue) {
		this.blue = normalize(blue);
	}

	/**
	 * Returns the alpha value
	 * 
	 * @return the alpha value of the color
	 */
	public int getAlpha() {
		return alpha;
	}

	/**
	 * Sets the alpha value of the color
	 * 
	 * @param alpha
	 *            the alpha value of the color
	 */
	public void setAlpha(int alpha) {
		this.alpha = normalize(alpha);
	}

	private int normalize(int value) {
		if (value < 0)
			return 0;
		else if (value > 255)
			return 255;
		else
			return value;
	}

}

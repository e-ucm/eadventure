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

package es.eucm.eadventure.common.params.fills.impl;

import es.eucm.eadventure.common.params.EAdFill;

/**
 * <p>
 * This class represents a basic color in the eAdventure model.
 * </p>
 * 
 */
public class EAdColor implements EAdFill {

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
	 * Blue EAdColor
	 */
	public static EAdColor BLUE = new EAdColor(0, 0, 255);

	/**
	 * Green EAdColor
	 */
	public static EAdColor GREEN = new EAdColor(0, 255, 0);

	/**
	 * Cyan EAdColor
	 */
	public static EAdColor CYAN = new EAdColor(0, 255, 255);

	/**
	 * Yellow EAdColor
	 */
	public static EAdColor YELLOW = new EAdColor(255, 255, 0);

	/**
	 * Orange EAdColor
	 */
	public static EAdColor ORANGE = new EAdColor(255, 125, 0);

	/**
	 * Magenta EAdColor
	 */
	public static EAdColor MAGENTA = new EAdColor(255, 0, 255);

	/**
	 * Gray EAdColor
	 */
	public static EAdColor GRAY = new EAdColor(125, 125, 125);

	/**
	 * Dark gray EAdColor
	 */
	public static EAdColor DARK_GRAY = new EAdColor(62, 62, 62);

	/**
	 * Light gray EAdColor
	 */
	public static EAdColor LIGHT_GRAY = new EAdColor(200, 200, 200);

	/**
	 * Brown EAdColor
	 */
	public static EAdColor BROWN = new EAdColor(200, 50, 0);

	/**
	 * Dark brown EAdColor
	 */
	public static EAdColor DARK_BROWN = new EAdColor(100, 50, 0);

	/**
	 * Light brown EAdColor
	 */
	public static EAdColor LIGHT_BROWN = new EAdColor(150, 75, 0);

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
	 * Creates a new white color, with the values of white
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

	/**
	 * Parse the value of a color from a string in the hexadecimal form
	 * {@code 0xRRGGBBAA}.
	 * 
	 * @param string
	 *            The string with the color value
	 * 
	 */
	public EAdColor(String data) {
		parse(data);
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

	@Override
	public String toString() {
		return toStringData();
	}

	private String valueToString(int value) {
		String temp = Integer.toHexString(value);
		if (temp.length() < 2)
			temp = "00" + temp;
		return temp.substring(temp.length() - 2);
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

	@Override
	public String toStringData() {
		String color = "0x";
		color += valueToString(red);
		color += valueToString(green);
		color += valueToString(blue);
		color += valueToString(alpha);
		return color;
	}

	@Override
	public void parse(String data) {
		if (data != null && data.length() == 10) {
			setRed(Integer.parseInt(data.substring(2, 4), 16));
			setGreen(Integer.parseInt(data.substring(4, 6), 16));
			setBlue(Integer.parseInt(data.substring(6, 8), 16));
			setAlpha(Integer.parseInt(data.substring(8, 10), 16));
		}
	}

}

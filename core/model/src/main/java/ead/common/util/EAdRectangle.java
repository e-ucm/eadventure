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

package ead.common.util;

import ead.common.params.AbstractParam;


/**
 * Represents a 2D rectangle. It has an ( x, y ) coordinate, width and height
 * 
 */
public class EAdRectangle extends AbstractParam {

	public static final String SEPARATOR = ";";

	/**
	 * x top left coordinate of the rectangle
	 */
	public int x;

	/**
	 * y top left coordinate of the rectangle
	 */
	public int y;

	/**
	 * rectangle's width
	 */
	public int width;

	/**
	 * rectangle's height
	 */
	public int height;

	/**
	 * Constructs a rectangle in (0, 0), with width and height 0.
	 */
	public EAdRectangle() {
		this(0, 0, 0, 0);
	}

	/**
	 * Creates a rectangle
	 * 
	 * @param x
	 *            x top left coordinate of the rectangle
	 * @param y
	 *            y top left coordinate of the rectangle
	 * @param width
	 *            rectangle's width
	 * @param height
	 *            rectangle's height
	 */
	public EAdRectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public EAdRectangle(String string) {
		parse(string);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean contains(int x, int y) {
		return (this.x <= x & x <= this.x + width && this.y <= y
				& y <= this.y + height);
	}

	private static EAdRectangle volatileEAdRectangle = new EAdRectangle(0, 0,
			0, 0);

	/**
	 * Returns a static rectangle that could be used to do some calculations,
	 * avoiding the creation of a new rectangle
	 * 
	 * @param x
	 *            x top left coordinate of the rectangle
	 * @param y
	 *            y top left coordinate of the rectangle
	 * @param width
	 *            rectangle's width
	 * @param height
	 *            rectangle's height
	 * @return
	 */
	public static EAdRectangle getVolatileEAdRectangle(int x, int y, int width,
			int height) {
		volatileEAdRectangle.x = x;
		volatileEAdRectangle.y = y;
		volatileEAdRectangle.width = width;
		volatileEAdRectangle.height = height;
		return volatileEAdRectangle;
	}

	/**
	 * Sets the position ( top left corner ) for this rectangle
	 * 
	 * @param position
	 *            new position
	 */
	public void setPosition(EAdPosition position) {
		this.x = position.getX();
		this.y = position.getY();

	}

	public String toString() {
		return toStringData();
	}

	@Override
	public String toStringData() {
		return x + SEPARATOR + y + SEPARATOR + width + SEPARATOR + height;
	}

	@Override
	public boolean parse(String data) {
		boolean error = data == null;

		if (!error) {
			String[] temp = data.split(SEPARATOR);
			if (temp.length == 4) {
				int i = 0;
				try {
					x = Integer.parseInt(temp[i++]);
					y = Integer.parseInt(temp[i++]);
					width = Integer.parseInt(temp[i++]);
					height = Integer.parseInt(temp[i++]);
				} catch (NumberFormatException e) {
					error = true;
				}
			} else {
				error = true;
			}

		}
		if (error) {
			x = y = width = height = 0;
		}

		return !error;
	}

}

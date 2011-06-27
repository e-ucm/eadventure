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
 * This class represents a position in the eAdventure model
 * </p>
 * <p>
 * Positions in the eAdventure model are not only represented by there (x, y)
 * coordinates but by the displacement of the center of the element
 * <p>
 * <p>
 * For instance, if the object A is positioned at (2, 2) in a TOP_LEFT corner,
 * and the object B is positioned at (7, 4) in a BOTTOM_CENTER corner:
 * </p>
 * <code>
 * + 0 1 2 3 4 5 6 7 8 9<br>
 * 0 . . . . . . . . . .<br>
 * 1 . . . . . . . . . .<br>
 * 2 . . + - - . - - - .<br>
 * 3 . . | A | . | B | .<br>
 * 4 . . - - - . - + - .<br>
 * 5 . . . . . . . . . .<br>
 * </code>
 */
public class EAdPosition {
	
	public static enum Corner {
		TOP_LEFT, BOTTOM_LEFT, BOTTOM_CENTER, CENTER, TOP_RIGHT
	}

	private int x;

	private int y;

	private float despX;

	private float despY;

	public EAdPosition(Corner corner, int x, int y) {
		this(x, y, 0f, 0f);
		setCorner(corner);
	}

	private void setCorner(Corner corner) {
		switch (corner) {
		case TOP_LEFT:
			despX = 0f;
			despY = 0f;
			break;
		case TOP_RIGHT:
			despX = 1f;
			despY = 0f;
			break;
		case BOTTOM_LEFT:
			despX = 0f;
			despY = 1f;
			break;
		case BOTTOM_CENTER:
			despX = 0.5f;
			despY = 1f;
			break;
		case CENTER:
			despX = 0.5f;
			despY = 0.5f;
			break;
		}
	}

	public EAdPosition(int x, int y) {
		this(Corner.TOP_LEFT, x, y);
	}

	public EAdPosition(int x, int y, float despX, float despY) {
		this.x = x;
		this.y = y;
		this.despX = despX;
		this.despY = despY;
	}

	public EAdPosition(EAdPosition position) {
		this(position.x, position.y, position.despX, position.despY);
	}

	private static EAdPosition volatileEAdPosition = new EAdPosition(0, 0);

	public static EAdPosition volatileEAdPosition(int x, int y) {
		return volatileEAdPosition(Corner.TOP_LEFT, x, y);
	}

	public static EAdPosition volatileEAdPosition(Corner corner, int x, int y) {
		volatileEAdPosition.x = x;
		volatileEAdPosition.y = y;
		volatileEAdPosition.setCorner(corner);
		return volatileEAdPosition;
	}

	public static EAdPosition volatileEAdPosition(int x, int y, float despX,
			float despY) {
		volatileEAdPosition.x = x;
		volatileEAdPosition.y = y;
		volatileEAdPosition.despX = despX;
		volatileEAdPosition.despY = despY;
		return volatileEAdPosition;
	}

	@Override
	public String toString() {
		return "" + x + ":" + y + ":" + despX + ":" + despY;
	}

	public static EAdPosition valueOf(String string) {
		String temp[] = string.split(":");
		int x = Integer.parseInt(temp[0]);
		int y = Integer.parseInt(temp[1]);
		float despX = Float.parseFloat(temp[2]);
		float despY = Float.parseFloat(temp[3]);
		return new EAdPosition(x, y, despX, despY);
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof EAdPosition) {
			EAdPosition temp = (EAdPosition) o;
			if (temp.x == x && temp.y == y && temp.despX == despX
					&& temp.despY == despY)
				return true;
		}
		return false;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the despX
	 */
	public float getDespX() {
		return despX;
	}

	/**
	 * @param despX
	 *            the despX to set
	 */
	public void setDespX(float despX) {
		this.despX = despX;
	}

	/**
	 * @return the despY
	 */
	public float getDespY() {
		return despY;
	}

	/**
	 * @param despY
	 *            the despY to set
	 */
	public void setDespY(float despY) {
		this.despY = despY;
	}

	/**
	 * Return the value of x in java coordinate system given a width
	 * 
	 * @param f
	 *            The width of the element
	 * @return the x value in the java coordinate system
	 */
	public int getJavaX(float f) {
		return (int) (x - despX * f);
	}

	/**
	 * Return the value of y in java coordinate system given a height
	 * 
	 * @param height
	 *            The height of the element
	 * @return the y value in the java coordinate system
	 */
	public int getJavaY(float height) {
		return (int) (y - despY * height);
	}

	/**
	 * Sets the position
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param corner
	 *            Corner which x and y coordinates are referred to
	 */
	public void set(int x, int y, Corner corner) {
		setX(x);
		setY(y);
		setCorner(corner);
	}

}

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
 * Represents a 2D rectangle. It has an ( x, y ) coordinate, width and height
 * 
 */
public class EAdRectangle {

	public int x;
	public int y;
	public int width;
	public int height;

	public EAdRectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns <b>true</b> if the given point is contained by this rectangle;
	 * <b>false</b> otherwise
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return <b>true</b> if the given point is contained by this rectangle;
	 *         <b>false</b> otherwise
	 */
	public boolean contains(int x, int y) {
		return (this.x <= x & x <= this.x + width && this.y <= y
				& y <= this.y + height);
	}

	private static EAdRectangle volatileEAdRectangle = new EAdRectangle(0, 0,
			0, 0);

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

}

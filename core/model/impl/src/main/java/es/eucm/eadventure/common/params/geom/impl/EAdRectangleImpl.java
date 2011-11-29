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

package es.eucm.eadventure.common.params.geom.impl;

import es.eucm.eadventure.common.params.EAdParamImpl;
import es.eucm.eadventure.common.params.geom.EAdRectangle;

/**
 * Represents a 2D rectangle. It has an ( x, y ) coordinate, width and height
 * 
 */
public class EAdRectangleImpl extends EAdParamImpl implements EAdRectangle {
	
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
	public EAdRectangleImpl(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public EAdRectangleImpl( String string ){
		parse( string );
	}
	
	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean contains(int x, int y) {
		return (this.x <= x & x <= this.x + width && this.y <= y
				& y <= this.y + height);
	}

	private static EAdRectangleImpl volatileEAdRectangle = new EAdRectangleImpl(0, 0,
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
	public static EAdRectangleImpl getVolatileEAdRectangle(int x, int y, int width,
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
	public void setPosition(EAdPositionImpl position) {
		this.x = position.getX();
		this.y = position.getY();

	}
	
	public String toString(){
		return toStringData();
	}

	@Override
	public String toStringData() {
		return x + SEPARATOR + y + SEPARATOR + width + SEPARATOR + height;
	}

	@Override
	public void parse(String data) {
		String[] temp = data.split(SEPARATOR);
		int i = 0;
		x = Integer.parseInt(temp[i++]);
		y = Integer.parseInt(temp[i++]);
		width = Integer.parseInt(temp[i++]);
		height = Integer.parseInt(temp[i++]);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof EAdRectangle))
			return false;
		EAdRectangle newRect = (EAdRectangle) object;
		if (newRect.getX() == x && newRect.getY() == y && newRect.getWidth() == width && newRect.getHeight() == height)
			return true;
		return false;
	}

	
}

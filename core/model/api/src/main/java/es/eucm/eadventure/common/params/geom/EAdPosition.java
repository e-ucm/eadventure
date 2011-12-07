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

package es.eucm.eadventure.common.params.geom;

import es.eucm.eadventure.common.params.EAdParam;

/**
 * General interface for eAdventure positions
 * 
 */
public interface EAdPosition extends EAdParam {

	/**
	 * @return the x coordinate
	 */
	int getX();

	/**
	 * @return the y coordinate
	 */
	int getY();

	/**
	 * Return the value of x in java coordinate system given a width. The
	 * calculation done is {@code x - dispX * width}
	 * 
	 * @param width
	 *            The width of the element
	 * @return the x value in the java coordinate system
	 */
	int getJavaX(float width);

	/**
	 * Return the value of y in java coordinate system given a height. The
	 * calculation done is {@code y - dispY * height}
	 * 
	 * @param height
	 *            The height of the element
	 * @return the y value in the java coordinate system
	 */
	int getJavaY(float height);

	/**
	 * Sets the x coordinate
	 * 
	 * @param x
	 *            the x to set
	 */
	void setX(int x);

	/**
	 * Sets the y coordinate
	 * 
	 * @param y
	 *            the y to set
	 */
	void setY(int y);

	/**
	 * Returns the displacement in the x axis
	 * 
	 * @return
	 */
	float getDispX();

	/**
	 * Returns the displacement in the y axis
	 * 
	 * @return
	 */
	float getDispY();

	/**
	 * Sets x and y coordinates for this position
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	void set(int x, int y);

}

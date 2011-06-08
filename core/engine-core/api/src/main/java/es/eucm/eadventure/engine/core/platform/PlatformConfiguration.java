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

package es.eucm.eadventure.engine.core.platform;

/**
 * This interface is implemented by classes that define the platform specific
 * configuration values.
 */
public interface PlatformConfiguration {

	/**
	 * Get the width of the actual representation.
	 * 
	 * @return The width in pixels of the representation.
	 */
	int getWidth();

	/**
	 * Get the height of the actual representation.
	 * 
	 * @return The height in pixels of the representation.
	 */
	int getHeight();

	/**
	 * Set the width of the actual representation.
	 * 
	 * @param width
	 *            The width in pixels of the representation.
	 */
	void setWidth(int width);

	/**
	 * Set the height of the actual representation.
	 * 
	 * @param height
	 *            The height in pixels of the representation.
	 */
	void setHeight(int height);

	/**
	 * Returns true if the game is fullscreen
	 * 
	 * @return true if the game is fullscreen
	 */
	boolean isFullscreen();

	/**
	 * Get the current drawing scale
	 * 
	 * @return the current drawing scale
	 */
	double getScale();

}

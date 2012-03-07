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

package ead.engine.core.platform;

import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.util.EAdTransformation;

/**
 * This interface is implemented by the class that implements the high level
 * drawing commands for a specific platform.
 */
public interface GUI {

	int VIRTUAL_WIDTH = 800;
	int VIRTUAL_HEIGHT = 600;

	/**
	 * Add an element to the scene. The order of the elements is used during
	 * painting.
	 * 
	 * @param go
	 *            the game object
	 * @param parentTransformation
	 *            parent transformation
	 */
	void addElement(DrawableGO<?> go, EAdTransformation parentTransformation);

	/**
	 * Show a special resource on the screen (e.g. video, HTML, etc.)
	 * 
	 * @param object
	 *            The resource to be shown
	 * @param x
	 *            The x coordinates of the resource
	 * @param y
	 *            The y coordinates of the resource
	 * @param fullscreen
	 *            Boolean indicating if the resource should be scaled to the
	 *            full game screen.
	 */
	void showSpecialResource(Object object, int x, int y, boolean fullscreen);

	/**
	 * <p>
	 * Do layout of the game elements, swap the game object manager to prepare
	 * for drawing and process the dragging element if needed.
	 * </p>
	 * 
	 * 
	 */
	void prepareGUI();

	/**
	 * <p>
	 * Commit the current game state into the screen
	 * </p>
	 * 
	 * @param interpolation
	 *            Value provided to smooth animations, between 0 and 1,
	 *            represents how much the game has advanced within the frame
	 *            cycle
	 */
	void commit(float interpolation);

	/**
	 * Initialize the GUI
	 */
	void initialize();

	/**
	 * Returns the main window's width
	 * 
	 * @return the main window's width
	 */
	int getWidth();

	/**
	 * Returns the window's height
	 * 
	 * @return the window's height
	 */
	int getHeight();

	/**
	 * Adds to transformation t1 transformation t2
	 * 
	 * @param t1
	 *            transformation 1
	 * @param t2
	 *            transformation 2
	 * @returns t1
	 */
	EAdTransformation addTransformation(EAdTransformation t1,
			EAdTransformation t2);

	/**
	 * Finalize the GUI, used when the game is stopped and finished.
	 */
	void finish();

	void setInitialTransformation(EAdTransformation initialTransformation);

	/**
	 * Returns the milliseconds since last update
	 * 
	 * @return
	 */
	int getSkippedMilliseconds();

	/**
	 * Returns tics per second in the game
	 * 
	 * @return
	 */
	int getTicksPerSecond();

}

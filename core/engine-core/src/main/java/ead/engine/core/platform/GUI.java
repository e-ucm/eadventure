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

import ead.engine.core.game.Game;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.util.EAdTransformation;

/**
 * This interface is implemented by the class that implements the high level
 * drawing commands for a specific platform.
 */
public interface GUI {

	/**
	 * Initialize the GUI. Creates the graphic context and and shows it.
	 * 
	 * @param the
	 *            game
	 */
	void initialize(Game game);

	/**
	 * Finalize the GUI. Destroy the graphic context. Once this method is
	 * called, the engine is unable to load more games
	 */
	void finish();

	/**
	 * Returns the milliseconds since last update
	 * 
	 * @return
	 */
	int getSkippedMilliseconds();

	/**
	 * Returns ticks per second in the game - the same as FPS, since one logic
	 * update produces one frame
	 * 
	 * @return
	 */
	int getTicksPerSecond();

	/**
	 * Add an element to the scene. The order of the elements is used during
	 * painting.
	 * 
	 * @param go
	 *            the game object
	 * @param parentTransformation
	 *            parent transformation
	 */
	void addElement(DrawableGO<?> go,
			EAdTransformation parentTransformation);

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
	void showSpecialResource(Object object, int x, int y,
			boolean fullscreen);

	/**
	 * <p>
	 * Commit the current game state into the screen
	 * </p>
	 * 
	 */
	void commit();

	/**
	 * Adds to transformation t1 transformation t2. Can be used externally for
	 * those interested
	 * 
	 * @param t1
	 *            transformation 1
	 * @param t2
	 *            transformation 2
	 * @returns t1
	 */
	EAdTransformation addTransformation(EAdTransformation t1,
			EAdTransformation t2);

}

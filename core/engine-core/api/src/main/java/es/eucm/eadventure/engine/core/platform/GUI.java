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

import java.util.List;

import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.huds.HudGO;

/**
 * This interface is implemented by the class that implements the high level
 * drawing commands for a specific platform.
 * 
 * @param <S>
 * 			  A parameter for the component element in the system, to allow for
 * 			  the representation of special content (e.g. in AWT Java it will be
 * 			  Component)
 */
public interface GUI<S> {

	/**
	 * The virtual height of eAdventure game representations
	 */
	static final int VIRTUAL_HEIGHT = 600;

	/**
	 * Add an element to the scene. The order of the elements is used during
	 * painting.
	 * 
	 * @param go
	 *            An EAdFunctional element.
	 */
	void addElement(GameObject<?> go, int offsetX, int offsetY);

	/**
	 * Returns the list of game objects in the GUI
	 * 
	 * @return
	 */
	List<GameObject<?>> getGameObjects();

	/**
	 * Add a new HUD (Head-up display) to the game
	 * 
	 * @param hud
	 */
	void addHUD(HudGO<?> hud);

	/**
	 * Remove a HUD (Head-up display) from the game
	 * 
	 * @param effectHUD
	 */
	void removeHUD(HudGO<?> effectHUD);

	/**
	 * Show a special resource on the screen (e.g. video, HTML, etc.)
	 * 
	 * @param component
	 *            The resource to be shown
	 * @param x
	 *            The x coordinates of the resource
	 * @param y
	 *            The y coordinates of the resource
	 * @param fullscreen
	 *            Boolean indicating if the resource should be scaled to the
	 *            full game screen.
	 */
	void showSpecialResource(S component, int x, int y,
			boolean fullscreen);

	/**
	 * <p>Do layout of the game elements, swap the game object manager to prepare for drawing
	 * and process the dragging element if needed.</p>
	 */
	void prepareGUI();

	/**
	 * <p>Commit the current game state into the screen</p>
	 * 
	 * @param interpolation
	 * Value provided to smooth animations, between 0 and 1, represents how much the game has advanced
	 * within the frame cycle
	 */
	void commit(float interpolation);

	/**
	 * <p>Commit the current game state into an image</p>
	 * @return
	 */
	RuntimeAsset<Image> commitToImage();

	/**
	 * Initialize the GUI
	 */
	void initilize();

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

}

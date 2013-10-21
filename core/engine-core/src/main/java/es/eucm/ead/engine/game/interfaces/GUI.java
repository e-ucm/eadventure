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

package es.eucm.ead.engine.game.interfaces;

import com.badlogic.gdx.scenes.scene2d.Stage;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;

/**
 * This interface is implemented by the class that implements the high level
 * drawing commands for a specific platform.
 */
public interface GUI {

	/**
	 * Id for the effects hud. Huds can be accessed via
	 * {@link GUI#getHUD(String)}
	 */
	public static final String EFFECTS_HUD_ID = "#engine.huds.effects";

	/**
	 * Id for the debuggers hud. Huds can be accessed via
	 * {@link GUI#getHUD(String)}
	 */
	public static final String DEBBUGERS_HUD_ID = "#engine.huds.debugger";

	void setStage(Stage stage);

	/**
	 * Adds a hud to the GUI
	 * 
	 * 
	 * @param hud
	 */
	void addHud(SceneElementGO hud);

	/**
	 * Removes a HUD to the GUI. This method shouldn't be used to control HUDs
	 * visibility, only to removed a HUD that is no longer needed
	 * 
	 * @param hud
	 */
	void removeHUD(SceneElementGO hud);

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
	 * Returns the first object that contains the given coordinates
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	SceneElementGO getGameObjectIn(int x, int y, boolean touchable);

	/**
	 * Returns the hud with the given id
	 * 
	 * @param id
	 *            the hud id
	 * @return
	 */
	SceneElementGO getHUD(String id);

	/**
	 * <p>
	 * Sets the current {@link es.eucm.ead.engine.gameobjects.sceneelements.SceneGO} of the game.
	 * </p>
	 * <p>
	 * Implementation should check if the scene is stackable (not all scenes
	 * are, such as video scenes, cutscenes, loading screens) and stack it if
	 * necessary.
	 * </p>
	 * 
	 * @param scene
	 *            the current {@link es.eucm.ead.engine.gameobjects.sceneelements.SceneGO}.
	 */
	void setScene(SceneGO scene);

	/**
	 * Returns the current game screen object. The {@link SceneGO} element is
	 * the root of all what is drawn to the screen.
	 * 
	 * @return a {@link SceneGO} object
	 */
	SceneGO getScene();

	/**
	 * @return the {@link Scene} previously visible in the game
	 */
	Scene getPreviousScene();

	/**
	 * Returns the scene element representing the given model element
	 * 
	 * @param element
	 * @return
	 */
	SceneElementGO getSceneElement(SceneElement element);

	SceneElementGO getRoot();

	SceneElementGO getGameObjectUnderPointer();

	/**
	 * Resets the GUI. The GUI recovers its initial state
	 */
	void reset();

}

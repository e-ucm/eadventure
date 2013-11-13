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

package es.eucm.ead.model.elements.operations;

public class SystemFields {

	/**
	 * X coordinate of the mouse in the window coordinate system
	 */
	public static final ElementField MOUSE_X = new ElementField(null, "mouse_x");

	/**
	 * Y coordinate of the mouse in the window coordinate system
	 */
	public static final ElementField MOUSE_Y = new ElementField(null, "mouse_y");

	/**
	 * X coordinate of the mouse in the scene coordinate system
	 */
	public static final ElementField MOUSE_SCENE_X = new ElementField(null,
			"mouse_scene_x");

	/**
	 * Y coordinate of the mouse in the scene coordinate system
	 */
	public static final ElementField MOUSE_SCENE_Y = new ElementField(null,
			"mouse_scene_y");

	public static final ElementField GAME_WIDTH = new ElementField(null,
			"width");

	public static final ElementField GAME_HEIGHT = new ElementField(null,
			"height");

	/**
	 * Loading percentage (0 - 100 )
	 */
	public static final ElementField LOADING = new ElementField(null, "loading");

	/**
	 * Variable containing the active element in the game
	 */
	public static final ElementField ACTIVE_ELEMENT = new ElementField(null,
			"active_element");

	public static final ElementField LANGUAGE = new ElementField(null,
			"language");

	public static final ElementField LANGUAGES = new ElementField(null,
			"languages");

	// Debugging variables
	public static final ElementField DEBUG_GAME_OBJECTS = new ElementField(
			null, "debug_game_objects");

	public static final ElementField DEBUG_ASSETS = new ElementField(null,
			"debug_assets");

	public static final ElementField DEBUG_HEAP_SIZE = new ElementField(null,
			"heap_size");

	public static final ElementField DEBUG_NATIVE_SIZE = new ElementField(null,
			"native_size");

	public static final ElementField SOUND_ON = new ElementField(null,
			"sound_on");
}

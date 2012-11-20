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

package ead.common.model.elements.variables;

import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.resources.assets.drawable.basics.Image;

public class SystemFields {

	/**
	 * X coordinate of the mouse in the window coordinate system
	 */
	public static final EAdField<Integer> MOUSE_X = new BasicField<Integer>(
			null, new VarDef<Integer>("mouse_x", Integer.class, 0));

	/**
	 * Y coordinate of the mouse in the window coordinate system
	 */
	public static final EAdField<Integer> MOUSE_Y = new BasicField<Integer>(
			null, new VarDef<Integer>("mouse_y", Integer.class, 0));

	/**
	 * X coordinate of the mouse in the scene coordinate system
	 */
	public static final EAdField<Integer> MOUSE_SCENE_X = new BasicField<Integer>(
			null, new VarDef<Integer>("mouse_scene_x", Integer.class, 0));

	/**
	 * Y coordinate of the mouse in the scene coordinate system
	 */
	public static final EAdField<Integer> MOUSE_SCENE_Y = new BasicField<Integer>(
			null, new VarDef<Integer>("mouse_scene_y", Integer.class, 0));

	public static final EAdField<Integer> GAME_WIDTH = new BasicField<Integer>(
			null, new VarDef<Integer>("gui_width", Integer.class, 800));

	public static final EAdField<Integer> GAME_HEIGHT = new BasicField<Integer>(
			null, new VarDef<Integer>("gui_height", Integer.class, 600));

	/**
	 * Variable containing the active element in the game
	 */
	public static final EAdField<EAdSceneElement> ACTIVE_ELEMENT = new BasicField<EAdSceneElement>(
			null, new VarDef<EAdSceneElement>("active_element",
					EAdSceneElement.class, null));

	public static final Image DEFAULT_MOUSE = new Image(
			"@drawable/default_cursor.png");

	public static final EAdField<Image> MOUSE_CURSOR = new BasicField<Image>(
			null, new VarDef<Image>("mouse_cursor", Image.class, DEFAULT_MOUSE));

	public static final EAdField<Boolean> SHOW_MOUSE = new BasicField<Boolean>(
			null, new VarDef<Boolean>("show_mouse", Boolean.class, true));

	public static final EAdField<Boolean> SHOW_INVENTORY = new BasicField<Boolean>(
			null, new VarDef<Boolean>("show_inventory", Boolean.class, true));

	public static final EAdField<Integer> ELAPSED_TIME_PER_UPDATE = new BasicField<Integer>(
			null, new VarDef<Integer>("elapsed_time_per_update", Integer.class,
					0));

	public static final EAdField<Boolean> PROCESS_INPUT = new BasicField<Boolean>(
			null, new VarDef<Boolean>("process_input", Boolean.class, true));

	/**
	 * If true, the player will only be able to interact with the elements in or
	 * above the basic HUD (like the effect HUD, or the actions HUD)
	 */
	public static final EAdField<Boolean> BASIC_HUD_OPAQUE = new BasicField<Boolean>(
			null, new VarDef<Boolean>("basic_hud_opaque", Boolean.class, false));

	/**
	 * Field holding the milliseconds since the game started 
	 */
	public static final EAdField<Long> GAME_TIME = new BasicField<Long>(null,
			new VarDef<Long>("game_time", Long.class, 0l));

}

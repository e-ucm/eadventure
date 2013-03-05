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

package ead.common.model.elements.operations;

import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.variables.VarDef;

public class SystemFields {

	/**
	 * X coordinate of the mouse in the window coordinate system
	 */
	public static final EAdField<Float> MOUSE_X = new BasicField<Float>(null,
			new VarDef<Float>("mouse_x", Float.class, 0.f));

	/**
	 * Y coordinate of the mouse in the window coordinate system
	 */
	public static final EAdField<Float> MOUSE_Y = new BasicField<Float>(null,
			new VarDef<Float>("mouse_y", Float.class, 0.f));

	/**
	 * X coordinate of the mouse in the scene coordinate system
	 */
	public static final EAdField<Float> MOUSE_SCENE_X = new BasicField<Float>(
			null, new VarDef<Float>("mouse_scene_x", Float.class, 0.f));

	/**
	 * Y coordinate of the mouse in the scene coordinate system
	 */
	public static final EAdField<Float> MOUSE_SCENE_Y = new BasicField<Float>(
			null, new VarDef<Float>("mouse_scene_y", Float.class, 0.f));

	public static final EAdField<Integer> GAME_WIDTH = new BasicField<Integer>(
			null, new VarDef<Integer>("width", Integer.class, 800));

	public static final EAdField<Integer> GAME_HEIGHT = new BasicField<Integer>(
			null, new VarDef<Integer>("height", Integer.class, 600));

	/**
	 *  Loading percentage (0 - 100 )
	 */
	public static final EAdField<Integer> LOADING = new BasicField<Integer>(
			null, new VarDef<Integer>("loading", Integer.class, 0));

	/**
	 * Variable containing the active element in the game
	 */
	public static final EAdField<EAdSceneElement> ACTIVE_ELEMENT = new BasicField<EAdSceneElement>(
			null, new VarDef<EAdSceneElement>("active_element",
					EAdSceneElement.class, null));

	public static final EAdField<Integer> ELAPSED_TIME_PER_UPDATE = new BasicField<Integer>(
			null, new VarDef<Integer>("elapsed_time_per_update", Integer.class,
					0));

	/**
	 * Field holding the milliseconds since the game started
	 */
	public static final EAdField<Long> GAME_TIME = new BasicField<Long>(null,
			new VarDef<Long>("game_time", Long.class, 0l));

	public static final EAdField<Boolean> FULLSCREEN = new BasicField<Boolean>(
			null, new VarDef<Boolean>("fullscreen", Boolean.class, false));

	public static final EAdField<Boolean> EXIT_WHEN_CLOSE = new BasicField<Boolean>(
			null, new VarDef<Boolean>("exitWhenClose", Boolean.class, true));

	public static EAdField<String> LANGUAGE = new BasicField<String>(null,
			new VarDef<String>("language", String.class, ""));

	public static EAdField<String> LANGUAGES = new BasicField<String>(null,
			new VarDef<String>("languages", String.class, ""));

}

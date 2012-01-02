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

import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.variables.EAdField;
import ead.common.resources.assets.drawable.basics.ImageImpl;

public class SystemFields {

	/**
	 * X coordinate of the mouse in the window coordinate system
	 */
	public static final EAdField<Integer> MOUSE_X = new FieldImpl<Integer>(
			null, new VarDefImpl<Integer>("mouse_x", Integer.class, 0));

	/**
	 * Y coordinate of the mouse in the window coordinate system
	 */
	public static final EAdField<Integer> MOUSE_Y = new FieldImpl<Integer>(
			null, new VarDefImpl<Integer>("mouse_y", Integer.class, 0));

	/**
	 * X coordinate of the mouse in the scene coordinate system
	 */
	public static final EAdField<Integer> MOUSE_SCENE_X = new FieldImpl<Integer>(
			null, new VarDefImpl<Integer>("mouse_scene_x", Integer.class, 0));

	/**
	 * Y coordinate of the mouse in the scene coordinate system
	 */
	public static final EAdField<Integer> MOUSE_SCENE_Y = new FieldImpl<Integer>(
			null, new VarDefImpl<Integer>("mouse_scene_y", Integer.class, 0));

	public static final EAdField<Integer> GAME_WIDTH = new FieldImpl<Integer>(
			null, new VarDefImpl<Integer>("gui_width", Integer.class, 800));

	public static final EAdField<Integer> GAME_HEIGHT = new FieldImpl<Integer>(
			null, new VarDefImpl<Integer>("gui_height", Integer.class, 600));

	/**
	 * Variable containing the active element in the game
	 */
	public static final EAdField<EAdSceneElement> ACTIVE_ELEMENT = new FieldImpl<EAdSceneElement>(
			null, new VarDefImpl<EAdSceneElement>("active_element",
					EAdSceneElement.class, null));

	public static final ImageImpl DEFAULT_MOUSE = new ImageImpl(
			"@drawable/default_cursor.png");

	public static final EAdField<ImageImpl> MOUSE_CURSOR = new FieldImpl<ImageImpl>(
			null, new VarDefImpl<ImageImpl>("mouse_cursor", ImageImpl.class,
					DEFAULT_MOUSE));

	public static final EAdField<Boolean> SHOW_MOUSE = new FieldImpl<Boolean>(
			null, new VarDefImpl<Boolean>("show_mouse", Boolean.class, true));

	public static final EAdField<Integer> ELAPSED_TIME_PER_UPDATE = new FieldImpl<Integer>(
			null, new VarDefImpl<Integer>("elapsed_time_per_update",
					Integer.class, 0));

}

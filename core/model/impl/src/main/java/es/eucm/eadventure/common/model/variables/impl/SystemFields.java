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

package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class SystemFields {

	/**
	 * X coordinate of the mouse in the window coordinate system
	 */
	public static final EAdField<Integer> MOUSE_X = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("mouse_x", Integer.class, 0));

	/**
	 * Y coordinate of the mouse in the window coordinate system
	 */
	public static final EAdField<Integer> MOUSE_Y = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("mouse_y", Integer.class, 0));

	/**
	 * X coordinate of the mouse in the scene coordinate system
	 */
	public static final EAdField<Integer> MOUSE_SCENE_X = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("mouse_scene_x", Integer.class, 0));

	/**
	 * Y coordinate of the mouse in the scene coordinate system
	 */
	public static final EAdField<Integer> MOUSE_SCENE_Y = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("mouse_scene_y", Integer.class, 0));

	public static final EAdField<Integer> GAME_WIDTH = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("gui_width", Integer.class, 800));

	public static final EAdField<Integer> GAME_HEIGHT = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("gui_height", Integer.class, 600));

	/**
	 * Variable containing the active element in the game
	 */
	public static final EAdField<EAdSceneElement> ACTIVE_ELEMENT = new EAdFieldImpl<EAdSceneElement>(
			null, new EAdVarDefImpl<EAdSceneElement>("active_element",
					EAdSceneElement.class, null));

	public static final Image DEFAULT_MOUSE = new ImageImpl(
			"@drawable/default_cursor.png");

	public static final EAdField<Image> MOUSE_CURSOR = new EAdFieldImpl<Image>(
			null, new EAdVarDefImpl<Image>("mouse_cursor", Image.class,
					DEFAULT_MOUSE));

	public static final EAdField<Boolean> SHOW_MOUSE = new EAdFieldImpl<Boolean>(
			null, new EAdVarDefImpl<Boolean>("show_mouse", Boolean.class, true));

	public static final EAdField<Integer> ELAPSED_TIME_PER_UPDATE = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("elapsed_time_per_update",
					Integer.class, 0));

}

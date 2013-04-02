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

package ead.common.model.elements.huds;

import ead.common.interfaces.Element;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.util.Position.Corner;

@Element
public class MouseHud extends BasicScene {

	public static final String MOUSE_HUD_ID = "#predefined.hud.mouse";

	public static final String CURSOR_ID = "#engine.cursor";

	public static final String TAKE_CURSOR = "take";
	public static final String EXAMINE_CURSOR = "examine";
	public static final String DEFAULT_CURSOR = "default";
	public static final String TALK_CURSOR = "talk";

	public MouseHud() {
		this.setId(MOUSE_HUD_ID);
		this.setBackground(null);
		EAdDrawable cursor = new Image("@drawable/default_cursor.png");
		EAdDrawable takeCursor = new Image("@drawable/take_cursor.png");
		EAdDrawable examineCursor = new Image("@drawable/examine_cursor.png");
		EAdDrawable talkCursor = new Image("@drawable/talk_cursor.png");
		SceneElement mouse = new SceneElement(cursor);
		mouse.setAppearance(TAKE_CURSOR, takeCursor);
		mouse.setAppearance(EXAMINE_CURSOR, examineCursor);
		mouse.setAppearance(TALK_CURSOR, talkCursor);
		mouse.setId(CURSOR_ID);
		mouse.setInitialEnable(false);
		mouse.setInitialVisible(false);
		mouse.setPosition(Corner.TOP_LEFT, 0, 0);
		mouse.setVarInitialValue(SceneElement.VAR_Z, 10000);
		getSceneElements().add(mouse);
	}

}

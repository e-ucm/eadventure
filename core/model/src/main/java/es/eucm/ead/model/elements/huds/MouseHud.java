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

package es.eucm.ead.model.elements.huds;

import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.params.util.Position;

@Element
public class MouseHud extends Scene {
	public static final String MOUSE_HUD_ID = "#predefined.hud.mouse";

	public static final String CURSOR_ID = "#engine.cursor";

	public static final BasicElement MOUSE_REF = new BasicElement(CURSOR_ID);

	public static final String TAKE_CURSOR = "take";
	public static final String EXAMINE_CURSOR = "examine";
	public static final String DEFAULT_CURSOR = "default";
	public static final String TALK_CURSOR = "talk";
	public static final String EXIT_CURSOR = "exit";
	public static final EAdDrawable cursor = new Image(
			"@drawable/default_cursor.png");
	public static final EAdDrawable takeCursor = new Image(
			"@drawable/take_cursor.png");
	public static final EAdDrawable examineCursor = new Image(
			"@drawable/examine_cursor.png");
	public static final EAdDrawable talkCursor = new Image(
			"@drawable/talk_cursor.png");
	public static final EAdDrawable exitCursor = new Image("@drawable/exit.png");

	public MouseHud() {
		this.setId(MOUSE_HUD_ID);
		this.setBackground(null);
		SceneElement mouse = new SceneElement(cursor);
		mouse.setAppearance(DEFAULT_CURSOR, cursor);
		mouse.setAppearance(TAKE_CURSOR, takeCursor);
		mouse.setAppearance(EXAMINE_CURSOR, examineCursor);
		mouse.setAppearance(TALK_CURSOR, talkCursor);
		mouse.setAppearance(EXIT_CURSOR, exitCursor);
		mouse.setId(CURSOR_ID);
		mouse.setEnable(false);
		//	mouse.setVisible(false);
		mouse.setPosition(Position.Corner.TOP_LEFT, 0, 0);
		mouse.putProperty(SceneElement.VAR_Z, 10000);
		getSceneElements().add(mouse);
	}

}

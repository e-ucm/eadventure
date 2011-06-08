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

package es.eucm.eadventure.gui.dragpanel.img;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class ResourceFactory {

	public static enum Cursors {
		CURSOR_EDIT_NORMAL, CURSOR_EDIT_SELECT, CURSOR_EDIT_ADD, CURSOR_EDIT_CLOSE, CURSOR_EDIT_MOVE, CURSOR_DELETE_ELEMENT, CURSOR_DELETE_POINT, CURSOR_ZOOM_IN, CURSOR_ZOOM_OUT, CURSOR_EDIT_ON_ELEMENT, CURSOR_NORMAL, CURSOR_CROSS;

		public String getCursorPath() {
			switch (this) {
			case CURSOR_EDIT_NORMAL:
				return "/es/eucm/eadventure/gui/dragpanel/img/editselect.png";
			case CURSOR_DELETE_ELEMENT:
				return "/es/eucm/eadventure/gui/dragpanel/img/delete.png";
			case CURSOR_EDIT_ON_ELEMENT:
				return "/es/eucm/eadventure/gui/dragpanel/img/editselecton.png";
			case CURSOR_EDIT_ADD:
				return "/es/eucm/eadventure/gui/dragpanel/img/editadd.png";
			case CURSOR_EDIT_MOVE:
				return "/es/eucm/eadventure/gui/dragpanel/img/editmove.png";
			case CURSOR_EDIT_CLOSE:
				return "/es/eucm/eadventure/gui/dragpanel/img/editclose.png";
			case CURSOR_DELETE_POINT:
				return "/es/eucm/eadventure/gui/dragpanel/img/deletepoint.png";
			case CURSOR_ZOOM_IN:
				return "/es/eucm/eadventure/gui/dragpanel/img/zoomin.png";
			case CURSOR_ZOOM_OUT:
				return "/es/eucm/eadventure/gui/dragpanel/img/zoomout.png";
			case CURSOR_NORMAL:
				return "/es/eucm/eadventure/gui/dragpanel/img/default_arrow.png";
			case CURSOR_CROSS:
				return "/es/eucm/eadventure/gui/dragpanel/img/crosscursor.png";
			}
			return null;
		}

		public Point getClickPoint() {
			switch (this) {
			case CURSOR_CROSS:
				return new Point(8, 8);

			default:
				return new Point(0, 0);
			}
		}
	}

	private static Cursor cursors[] = new Cursor[Cursors.values().length];

	public static Cursor getCursor(Cursors cursor) {
		if (cursors[cursor.ordinal()] == null) {
			String path = cursor.getCursorPath();
			cursors[cursor.ordinal()] = Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(Toolkit.getDefaultToolkit().getClass().getResource(path)), cursor.getClickPoint(), "EAdVentureCursor" + cursor);
		}
		return cursors[cursor.ordinal()];
	}

	public static enum Icons {
		SELECT_ICON, ZOOM_IN, ZOOM_OUT, DELETE_ICON, EDIT_ICON, LAYERS, INSERT_RECTANGLE, INSERT_POLYGON, GO_UP, GO_DOWN, GO_BACK, GO_TOP;

		public String getPath() {
			switch (this) {
			case SELECT_ICON:
				return "/es/eucm/eadventure/gui/dragpanel/img/selecticon.png";
			case ZOOM_IN:
				return "/es/eucm/eadventure/gui/dragpanel/img/zoomin.png";
			case ZOOM_OUT:
				return "/es/eucm/eadventure/gui/dragpanel/img/zoomout.png";
			case DELETE_ICON:
				return "/es/eucm/eadventure/gui/dragpanel/img/delete.png";
			case EDIT_ICON:
				return "/es/eucm/eadventure/gui/dragpanel/img/editicon.png";
			case LAYERS:
				return "/es/eucm/eadventure/gui/dragpanel/img/layers.png";
			case INSERT_RECTANGLE:
				return "/es/eucm/eadventure/gui/dragpanel/img/selecticon.png";
			case INSERT_POLYGON:
				return "/es/eucm/eadventure/gui/dragpanel/img/editicon.png";
			case GO_UP:
				return "/es/eucm/eadventure/gui/dragpanel/img/goup.png";
			case GO_DOWN:
				return "/es/eucm/eadventure/gui/dragpanel/img/godown.png";
			case GO_BACK:
				return "/es/eucm/eadventure/gui/dragpanel/img/goback.png";
			case GO_TOP:
				return "/es/eucm/eadventure/gui/dragpanel/img/gotop.png";
			}
			return null;
		}
	}

	private static ImageIcon icons[] = new ImageIcon[Icons.values().length];

	public static ImageIcon getIcon(Icons icon) {
		if (icons[icon.ordinal()] == null) {
			String path = icon.getPath();
			icons[icon.ordinal()] = new ImageIcon(icon.getClass().getResource(path));
		}
		return icons[icon.ordinal()];
	}
}

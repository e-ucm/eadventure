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

package ead.gui.listpanel.extra;

import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import ead.gui.CommonGUIMessages;
import ead.gui.R;
import ead.utils.i18n.Resource;

public class ListButton extends JButton {

	private static final long serialVersionUID = 1L;

	public static enum Type {ADD, DELETE, MOVE_UP, MOVE_DOWN, DUPLICATE};

	public ListButton(Type type) {
		super();

        setIcon(new ImageIcon(Resource.loadImage(getImageName(type))));

		setContentAreaFilled(false);
		setMargin(new Insets(0, 0, 0, 0));
		setToolTipText(getToolTip(type));
	}

	public String getImageName(Type type) {
		switch(type) {
            case ADD:       return R.Drawable.add_png;
            case DELETE:	return R.Drawable.delete_png;
            case MOVE_UP:	return R.Drawable.move_up_png;
            case MOVE_DOWN:	return R.Drawable.move_down_png;
            case DUPLICATE:	return R.Drawable.duplicate_png;
		}
		return null;
	}


	public String getToolTip(Type type) {
		switch(type) {
		case ADD:
			return CommonGUIMessages.add_new;
		case DELETE:
			return CommonGUIMessages.delete;
		case MOVE_UP:
			return CommonGUIMessages.move_up;
		case MOVE_DOWN:
			return CommonGUIMessages.move_down;
		case DUPLICATE:
			return CommonGUIMessages.duplicate;
		}
		return null;
	}
}

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

package ead.gui;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EAdSimpleButton extends JButton {

	private static final long serialVersionUID = 7971695719028514636L;

	private static final Logger logger = LoggerFactory.getLogger(EAdSimpleButton.class);

	public static enum SimpleButton { UNDO, REDO, BACKWARD, FORWARD, SEARCH };
	
	public EAdSimpleButton(SimpleButton simpleButton) {
		super(getIcon(simpleButton));
		setToolTipText(getToolTip(simpleButton));
	}
	
	public static String getToolTip(SimpleButton simpleButton) {
		switch (simpleButton) {
		case REDO:
			return CommonGUIMessages.redo;
		case UNDO:
			return CommonGUIMessages.undo;
		case SEARCH:
			return CommonGUIMessages.search;
		case BACKWARD:
			return CommonGUIMessages.back;
		case FORWARD:
			return CommonGUIMessages.forward;
		default:
			return null;
		}
	}

	private static ImageIcon getIcon(SimpleButton simpleButton) {
		ImageIcon normal = null;
		InputStream normalIs = null;
		switch (simpleButton) {
		case REDO:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.redo_png);
			break;
		case UNDO:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.undo_png);
			break;
		case SEARCH:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.search_png);
			break;
		case BACKWARD:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.backward_png);
			break;
		case FORWARD:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.forward_png);
			break;
		default:
			normalIs = ClassLoader.getSystemResourceAsStream(R.Drawable.redo_png);
		}
		try {
			normal = new ImageIcon(ImageIO.read(normalIs));
		} catch (IOException e) {
			logger.error("Cannot load icons", e);
		}
		return normal;
	}
}

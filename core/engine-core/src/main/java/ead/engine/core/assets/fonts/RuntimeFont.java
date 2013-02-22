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

package ead.engine.core.assets.fonts;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.google.inject.Inject;

import ead.common.model.assets.text.EAdFont;
import ead.common.model.params.util.Rectangle;
import ead.engine.core.assets.AbstractRuntimeAsset;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.AssetHandlerImpl;

/**
 * Represents a runtime font. Unlike {@link EAdFont}, which only contains static
 * information about the font, this class contains information that would be
 * required during the game execution, such as font measurements.
 * 
 * {@link RuntimeFont} is born from an {@link EAdFont}
 */
public class RuntimeFont extends AbstractRuntimeAsset<EAdFont> {

	private BitmapFont bitmapFont;

	private static final String defaultFont = "@font/droid-12.fnt";
	private static final String defaultFontPng = "@font/droid-12.png";

	@Inject
	public RuntimeFont(AssetHandler assetHandler) {
		super(assetHandler);
	}

	public boolean loadAsset() {
		super.loadAsset();
		String fileName = null;
		if (descriptor.getUri() != null) {
			fileName = descriptor.getUri();
		} else {
			int size = Math.round(descriptor.getSize());
			String modifier = "";
			switch (descriptor.getStyle()) {
			case BOLD:
				modifier = "bold";
				break;
			case ITALIC:
				modifier = "italic";
				break;
			default:
				modifier = "";
			}

			fileName = "@font/droid-" + size
					+ (modifier.equals("") ? "" : "-" + modifier);
		}

		String fontData = defaultFont;
		String fontPng = defaultFontPng;

		AssetHandlerImpl ah = (AssetHandlerImpl) assetHandler;
		FileHandle fntHandle = ah.getFileHandle(fileName + ".fnt");
		FileHandle pngHandle = ah.getFileHandle(fileName + ".png");

		if (fntHandle.exists() && pngHandle.exists()) {
			fontData = fileName + ".fnt";
			fontPng = fileName + ".png";
		}
		bitmapFont = new BitmapFont(ah.getFileHandle(fontData), ah
				.getFileHandle(fontPng), true);
		return true;
	}

	/**
	 * Returns the string width with the given font in the current context
	 * 
	 * @param string
	 *            String to be measured
	 * @param font
	 *            Font used in string measurement
	 * @return the string width with the given font in the current context
	 */
	public int stringWidth(String string) {
		return Math.round(bitmapFont.getBounds(string).width);
	}

	/**
	 * Returns one line's height with the given font
	 * 
	 * @param font
	 *            Font used in string measurement
	 * @return one line's height with the given font
	 */
	public int lineHeight() {
		return Math.round(bitmapFont.getLineHeight());
	}

	/**
	 * Returns the string bounds
	 * 
	 * @param string
	 *            string to be measured
	 * @return the string bounds
	 */
	public Rectangle stringBounds(String string) {
		TextBounds b = bitmapFont.getBounds(string);
		return new Rectangle(0, 0, Math.round(b.width), Math.round(b.height));
	}

	public BitmapFont getBitmapFont() {
		return bitmapFont;
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		bitmapFont.dispose();
	}

	@Override
	public void refresh() {
		// Do nothing
	}

	public EAdFont getEAdFont() {
		return descriptor;
	}
}

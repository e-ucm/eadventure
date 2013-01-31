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

package ead.engine.core.gdx.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.google.inject.Inject;

import ead.common.model.params.util.Rectangle;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.text.BasicRuntimeFont;

public class GdxFont extends BasicRuntimeFont {

	private BitmapFont bitmapFont;

	private static final String defaultFont = "@font/droid-12.fnt";
	private static final String defaultFontPng = "@font/droid-12.png";

	@Inject
	public GdxFont(AssetHandler assetHandler) {
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

		GdxAssetHandler ah = (GdxAssetHandler) assetHandler;
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

	@Override
	public int stringWidth(String string) {
		return Math.round(bitmapFont.getBounds(string).width);
	}

	@Override
	public int lineHeight() {
		return Math.round(bitmapFont.getLineHeight());
	}

	@Override
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
}

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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.text.BasicFont;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.params.util.Rectangle;
import ead.engine.core.assets.AssetHandler;

@Singleton
public class FontHandlerImpl implements FontHandler {

	protected Logger logger = LoggerFactory.getLogger("FontCacheImpl");

	protected Map<EAdFont, RuntimeFont> fontHandler;

	protected AssetHandler assetHandler;

	@Inject
	public FontHandlerImpl(AssetHandler assetHandler) {
		logger.info("New instance of FontHandler");
		this.fontHandler = new HashMap<EAdFont, RuntimeFont>();
		this.assetHandler = assetHandler;
	}

	/**
	 * Puts a runtime font in the cache
	 * 
	 * @param font
	 *            {@link BasicFont}
	 * @param rFont
	 *            {@link RuntimeFont} associated to the given {@link BasicFont}
	 */
	@Override
	public void put(EAdFont font, RuntimeFont rFont) {
		fontHandler.put(font, rFont);
	}

	/**
	 * Returns {@link RuntimeFont} associated to the given {@link BasicFont}
	 * 
	 * @param font
	 *            the {@link BasicFont}
	 * @return {@link RuntimeFont} associated to the given {@link BasicFont}
	 */
	@Override
	public RuntimeFont get(EAdFont font) {
		if (!fontHandler.containsKey(font)) {
			this.addEAdFont(font);
		}
		return fontHandler.get(font);
	}

	/**
	 * Returns the string width with the given font in the current context, -1
	 * if font is not present in the cache
	 * 
	 * @param string
	 *            String to be measured
	 * @param font
	 *            Font used in string measurement
	 * @return the string width with the given font in the current context, -1
	 *         if font is not present in the cache
	 */
	@Override
	public int stringWidth(String string, EAdFont font) {
		if (fontHandler.containsKey(font))
			return fontHandler.get(font).stringWidth(string);
		else
			return -1;
	}

	/**
	 * Returns one line's height with the given font, -1 if font is not present
	 * in the cache
	 * 
	 * @param font
	 *            Font used in string measurement
	 * @return one line's height with the given font, -1 if font is not present
	 *         in the cache
	 */
	@Override
	public int lineHeight(EAdFont font) {
		if (fontHandler.containsKey(font))
			return fontHandler.get(font).lineHeight();
		else
			return -1;
	}

	/**
	 * Returns the string bounds with the given {@link BasicFont}, <b>null</b>
	 * if font is not present in the cache
	 * 
	 * @param string
	 *            string to be measured
	 * @return the string bounds, <b>null</b> if font is not present in the
	 *         cache
	 */
	@Override
	public Rectangle stringBounds(String string, EAdFont font) {
		if (fontHandler.containsKey(font))
			return fontHandler.get(font).stringBounds(string);
		else
			return null;
	}

	/**
	 * Adds a new {@link RuntimeFont} to cache based on the given
	 * {@link BasicFont}
	 * 
	 * @param font
	 *            given {@link BasicFont}
	 */
	@Override
	public void addEAdFont(EAdFont font) {
		RuntimeFont runtimeFont = (RuntimeFont) assetHandler
				.getRuntimeAsset(font);
		if (!fontHandler.containsKey(font)) {
			fontHandler.put(font, runtimeFont);
		}
	}

}

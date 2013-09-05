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

package es.eucm.ead.engine.assets.fonts;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.assets.text.EAdFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class FontHandler {

	protected Logger logger = LoggerFactory.getLogger("FontCache");

	protected Map<EAdFont, RuntimeFont> cache;

	protected AssetHandler assetHandler;

	@Inject
	public FontHandler(AssetHandler assetHandler) {
		logger.info("New instance of FontHandler");
		this.cache = new HashMap<EAdFont, RuntimeFont>();
		this.assetHandler = assetHandler;
		assetHandler.setFontHandler(this);
	}

	/**
	 * Puts a runtime font in the cache
	 *
	 * @param font
	 *            {@link BasicFont}
	 * @param rFont
	 *            {@link RuntimeFont} associated to the given {@link BasicFont}
	 */
	public void put(EAdFont font, RuntimeFont rFont) {
		cache.put(font, rFont);
	}

	/**
	 * Returns {@link RuntimeFont} associated to the given {@link BasicFont}
	 *
	 * @param font
	 *            the {@link BasicFont}
	 * @return {@link RuntimeFont} associated to the given {@link BasicFont}
	 */
	public RuntimeFont get(EAdFont font) {
		if (!cache.containsKey(font)) {
			this.addEAdFont(font);
		}
		return cache.get(font);
	}

	/**
	 * Adds a new {@link RuntimeFont} to cache based on the given
	 * {@link BasicFont}
	 *
	 * @param font
	 *            given {@link BasicFont}
	 */
	public void addEAdFont(EAdFont font) {
		RuntimeFont runtimeFont = (RuntimeFont) assetHandler
				.getRuntimeAsset(font);
		if (!cache.containsKey(font)) {
			cache.put(font, runtimeFont);
		}
	}

	/**
	 * Cleans the fonts in the cache
	 */
	public void clean() {
		this.cache.clear();
	}
}

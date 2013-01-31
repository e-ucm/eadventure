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

package ead.engine.core.platform;

import ead.common.model.assets.text.EAdFont;
import ead.common.model.params.util.Rectangle;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeFont;

/**
 * Interface for a cache of the system dependent {@link RuntimeFont}s
 * 
 */
public interface FontHandler {

	/**
	 * Puts a runtime font in the cache
	 * 
	 * @param font
	 *            {@link EAdFont}
	 * @param rFont
	 *            {@link RuntimeFont} associated to the given {@link EAdFont}
	 */
	public void put(EAdFont font, RuntimeFont rFont);

	/**
	 * Returns {@link RuntimeFont} associated to the given {@link EAdFont}
	 * 
	 * @param font
	 *            the {@link EAdFont}
	 * @return {@link RuntimeFont} associated to the given {@link EAdFont}
	 */
	public RuntimeFont get(EAdFont font);

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
	public int stringWidth(String string, EAdFont font);

	/**
	 * Returns one line's height with the given font, -1 if font is not present
	 * in the cache
	 * 
	 * @param font
	 *            Font used in string measurement
	 * @return one line's height with the given font, -1 if font is not present
	 *         in the cache
	 */
	public int lineHeight(EAdFont font);

	/**
	 * Returns the string bounds with the given {@link EAdFont}, <b>null</b> if
	 * font is not present in the cache
	 * 
	 * @param string
	 *            string to be measured
	 * @return the string bounds, <b>null</b> if font is not present in the
	 *         cache
	 */
	public Rectangle stringBounds(String string, EAdFont font);

	/**
	 * Adds a new {@link RuntimeFont} to cache based on the given
	 * {@link EAdFont}
	 * 
	 * @param font
	 *            given {@link EAdFont}
	 */
	public void addEAdFont(EAdFont font);

	/**
	 * @param abstractAssetHandler the asset handler for the platform
	 */
	public void setAssetHandler(AssetHandler abstractAssetHandler);

}

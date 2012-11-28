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

package ead.engine.core.platform.assets;

import ead.common.resources.assets.text.EAdFont;
import ead.common.util.EAdRectangle;

/**
 * Represents a runtime font. Unlike {@link EAdFont}, which only contains static
 * information about the font, this class contains information that would be
 * required during the game execution, such as font measurements.
 * 
 * {@link RuntimeFont} is born from an {@link EAdFont}
 */
public interface RuntimeFont extends RuntimeAsset<EAdFont> {

	/**
	 * Returns the {@link EAdFont} linked to this object
	 * 
	 * @return the {@link EAdFont} linked to this object
	 */
	EAdFont getEAdFont();

	/**
	 * Returns the string width with the given font in the current context
	 * 
	 * @param string
	 *            String to be measured
	 * @param font
	 *            Font used in string measurement
	 * @return the string width with the given font in the current context
	 */
	int stringWidth(String string);

	/**
	 * Returns one line's height with the given font
	 * 
	 * @param font
	 *            Font used in string measurement
	 * @return one line's height with the given font
	 */
	int lineHeight();

	/**
	 * Returns the string bounds
	 * 
	 * @param string
	 *            string to be measured
	 * @return the string bounds
	 */
	EAdRectangle stringBounds(String string);

}

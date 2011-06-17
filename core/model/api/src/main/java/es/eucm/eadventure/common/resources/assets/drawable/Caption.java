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

package es.eucm.eadventure.common.resources.assets.drawable;

import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdFont;
import es.eucm.eadventure.common.resources.EAdString;

/**
 * General interface for texts to be shown during the game
 * 
 * 
 */
public interface Caption extends Drawable {

	/**
	 * Constant that can be used to define infinite maximum width/height for the
	 * caption
	 */
	public static final int INFINITE_SIZE = -1;

	/**
	 * Constant that can be used to define infinite maximum/minimum width/height
	 * for the caption
	 */
	public static final int SCREEN_SIZE = -2;

	/**
	 * Returns the {@link EAdString} defining the text
	 * 
	 * @return
	 */
	public EAdString getText();

	/**
	 * Returns the font for the text
	 * 
	 * @return
	 */
	public EAdFont getFont();

	/**
	 * Returns the color for the text
	 * 
	 * @return
	 */
	public EAdBorderedColor getTextColor();

	/**
	 * Returns whether this caption has a bubble
	 * 
	 * @return
	 */
	public boolean hasBubble();

	/**
	 * Returns the padding used in bubble drawing
	 * 
	 * @return the padding used in bubble drawing
	 */
	public int getPadding();

	/**
	 * Returns the color for the bubble containing the text
	 * 
	 * @return
	 */
	public EAdBorderedColor getBubbleColor();

	/**
	 * Returns the alpha value for the caption
	 * 
	 * @return
	 */
	public float getAlpha();

	/**
	 * Returns the maximum width for this text, and integer greater than zero,
	 * or the constants {@link Caption#INFINITE_SIZE} or
	 * {@link Caption#SCREEN_SIZE}
	 * 
	 * @return
	 */
	int getMaximumWidth();

	/**
	 * Returns the maximum height for this text, and integer greater than zero,
	 * or the constants {@link Caption#INFINITE_SIZE} or
	 * {@link Caption#SCREEN_SIZE}
	 * 
	 * @return
	 */
	int getMaximumHeight();

	/**
	 * Returns the minimum width for this text, and integer greater than zero,
	 * or the constants {@link Caption#INFINITE_SIZE} or
	 * {@link Caption#SCREEN_SIZE}
	 * 
	 * @return
	 */
	int getMinimumWidth();

	/**
	 * Returns the minimum height for this text, and integer greater than zero,
	 * or the constants {@link Caption#INFINITE_SIZE} or
	 * {@link Caption#SCREEN_SIZE}
	 * 
	 * @return
	 */
	int getMinimumHeight();

}
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

package ead.common.resources.assets.drawable.basics;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.params.paint.EAdPaint;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.enums.Alignment;
import ead.common.resources.assets.text.EAdFont;

/**
 * General interface for texts to be shown during the game
 */
public interface EAdCaption extends EAdBasicDrawable {

	/**
	 * Constant that can be used to define infinite maximum width/height for the
	 * caption
	 */
	public static final int AUTO_SIZE = -1;

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
	EAdString getText();

	/**
	 * Returns the font for the text
	 * 
	 * @return
	 */
	EAdFont getFont();

	/**
	 * Sets the font for the caption
	 * 
	 * @param font
	 *            the font
	 */
	void setFont(EAdFont font);

	/**
	 * Returns the color for the text
	 * 
	 * @return
	 */
	EAdPaint getTextPaint();

	/**
	 * Returns whether this caption has a bubble
	 * 
	 * @return
	 */
	boolean hasBubble();

	/**
	 * Returns the padding used in bubble drawing
	 * 
	 * @return the padding used in bubble drawing
	 */
	int getPadding();

	/**
	 * Sets the padding for the caption
	 * 
	 * @param padding
	 *            the padding
	 */
	void setPadding(int padding);

	/**
	 * Returns the color for the bubble containing the text
	 * 
	 * @return
	 */
	EAdPaint getBubblePaint();

	/**
	 * Returns the preferred width for this text, an integer greater than zero,
	 * or the constants {@link EAdCaption#AUTO_SIZE} or
	 * {@link EAdCaption#SCREEN_SIZE}
	 * 
	 * @return
	 */
	int getPreferredWidth();

	/**
	 * Sets the preferred width for the caption, an integer greater than zero,
	 * or the constants {@link EAdCaption#AUTO_SIZE} or
	 * {@link EAdCaption#SCREEN_SIZE}
	 * 
	 * @param preferredWidth
	 *            the preferred width
	 */
	void setPreferredWidth(int preferredWidth);

	/**
	 * Returns the preferred height for this text, an integer greater than zero,
	 * or the constants {@link EAdCaption#AUTO_SIZE} or
	 * {@link EAdCaption#SCREEN_SIZE}
	 * 
	 * @return
	 */
	int getPreferredHeight();

	/**
	 * Returns a list of fields to be shown within this caption. "#0" in the
	 * text will be substituted by the first element of this list, "#1" for the
	 * second, and so on.
	 * 
	 * @return a list of fields
	 */
	EAdList<EAdOperation> getFields();

	/**
	 * Returns the alignment for the text
	 * 
	 * @return the alignment for the text
	 */
	Alignment getAlignment();

	/**
	 * Sets the preferred height for the caption, an integer greater than zero,
	 * or the constants {@link EAdCaption#AUTO_SIZE} or
	 * {@link EAdCaption#SCREEN_SIZE}
	 * 
	 * @param preferredHeight
	 *            the preferred height
	 */
	void setPreferredHeight(int preferredHeight);

}

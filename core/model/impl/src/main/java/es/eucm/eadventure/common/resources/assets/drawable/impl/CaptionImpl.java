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

package es.eucm.eadventure.common.resources.assets.drawable.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.model.params.EAdFont;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;

public class CaptionImpl implements Caption {

	@Param("label")
	private EAdString label;

	@Param("font")
	private EAdFont font;

	@Param("textColor")
	private EAdBorderedColor textColor;

	@Param("hasBubble")
	private boolean hasBubble;

	@Param("bubbleColor")
	private EAdBorderedColor bubbleColor;

	@Param("alpha")
	private float alpha;

	@Param("maxWidth")
	private int maxWidth;

	@Param("maxHeight")
	private int maxHeight;

	@Param("minWidth")
	private int minWidth;

	@Param("minHeight")
	private int minHeight;

	public CaptionImpl() {
		this(null);
	}

	public CaptionImpl(EAdString label) {
		this.label = label;
		textColor = new EAdBorderedColor(EAdColor.BLACK, EAdColor.WHITE);
		bubbleColor = new EAdBorderedColor(EAdColor.WHITE, EAdColor.BLACK);
		this.font = EAdFont.BIG;
		this.hasBubble = false;
		this.bubbleColor = null;
		maxHeight = -1;
		maxWidth = -1;
		minWidth = 0;
		minHeight = 0;
		alpha = 1.0f;
	}

	@Override
	public EAdString getText() {
		return label;
	}

	@Override
	public void setText(EAdString label) {
		this.label = label;
	}

	@Override
	public EAdFont getFont() {
		return font;
	}

	public void setFont(EAdFont font) {
		this.font = font;
	}

	@Override
	public EAdBorderedColor getTextColor() {
		return textColor;
	}

	public void setTextColor(EAdBorderedColor textColor) {
		this.textColor = textColor;
	}

	@Override
	public boolean hasBubble() {
		return hasBubble;
	}

	@Override
	public EAdBorderedColor getBubbleColor() {
		return bubbleColor;
	}

	public void setBubbleColor(EAdBorderedColor bubbleColor) {
		this.bubbleColor = bubbleColor;
		setHasBubble(bubbleColor != null);
	}

	@Override
	public float getAlpha() {
		return alpha;
	}

	@Override
	public int getMaximumWidth() {
		return maxWidth;
	}

	@Override
	public int getMaximumHeight() {
		return maxHeight;
	}

	@Override
	public int getMinimumWidth() {
		return minWidth;
	}

	@Override
	public int getMinimumHeight() {
		return minHeight;
	}

	/**
	 * Sets maximum width for this text. Could be a positive number or
	 * {@link EAdTextImpl#INFINITE}. Zero or any negative number will be
	 * considered as infinite
	 * 
	 * @param maxWidth
	 *            maximum width for this text. Could be a positive number or
	 *            {@link EAdTextImpl#INFINITE}. Zero or any negative number will
	 *            be considered as infinite
	 */
	public void setMaximumWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * Sets maximum height for this text. Could be a positive number or
	 * {@link EAdTextImpl#INFINITE}. Zero or any negative number will be
	 * considered as infinite
	 * 
	 * @param maxHeight
	 *            maximum height for this text. Could be a positive number or
	 *            {@link EAdTextImpl#INFINITE}. Zero or any negative number will
	 *            be considered as infinite
	 */
	public void setMaximumHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	/**
	 * Sets minimum width for this text. This width mostly affects to the bubble
	 * size
	 * 
	 * @param minWidth
	 *            minimum width for the text
	 */
	public void setMinimumWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	/**
	 * Sets minimum width for this text. This width mostly affects to the bubble
	 * size
	 * 
	 * @param minWidth
	 *            minimum width for the text
	 */
	public void setMinimumHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	public void setHasBubble(boolean b) {
		this.hasBubble = b;
	}

}

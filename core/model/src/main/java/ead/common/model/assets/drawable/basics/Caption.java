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

package ead.common.model.assets.drawable.basics;

import ead.common.interfaces.Param;
import ead.common.model.assets.AbstractAssetDescriptor;
import ead.common.model.assets.drawable.basics.enums.Alignment;
import ead.common.model.assets.text.BasicFont;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.paint.EAdPaint;
import ead.common.model.params.text.EAdString;

/**
 * A caption asset. It's represented by a text, and a bubble
 * 
 */
public class Caption extends AbstractAssetDescriptor implements EAdCaption {

	/**
	 * Default padding for bubbles
	 */
	private static final int DEFAULT_PADDING = 30;

	@Param
	private EAdString label;

	@Param
	private EAdFont font;

	@Param
	private EAdPaint textPaint;

	@Param
	private boolean hasBubble;

	@Param
	private EAdPaint bubblePaint;

	@Param
	private int preferredWidth;

	@Param
	private int preferredHeight;

	@Param
	private int padding;

	@Param
	private EAdList<EAdOperation> fields;

	@Param
	private Alignment alignment;

	/**
	 * Constructs an empty caption. DO NOT USE this constructor. It is only used
	 * by the reader
	 */
	public Caption() {
		textPaint = new Paint(ColorFill.BLACK, ColorFill.WHITE);
		bubblePaint = new Paint(ColorFill.WHITE, ColorFill.BLACK);
		this.font = BasicFont.BIG;
		this.hasBubble = false;
		this.bubblePaint = null;
		preferredHeight = AUTO_SIZE;
		preferredWidth = AUTO_SIZE;
		padding = DEFAULT_PADDING;
		alignment = Alignment.CENTER;
		fields = new EAdList<EAdOperation>();
	}

	/**
	 * Constructs a caption with the given label
	 * 
	 * @param label
	 *            the label
	 */
	public Caption(EAdString label) {
		this();
		this.label = label;
	}

	/**
	 * Constructs a caption with the given string
	 * 
	 * @param string
	 */
	public Caption(String string) {
		this(new EAdString(string));
	}

	public Caption(String stringId, EAdFont font) {
		this(stringId);
		this.setFont(font);
	}

	@Override
	public EAdString getText() {
		return label;
	}

	@Override
	public EAdFont getFont() {
		return font;
	}

	@Override
	public void setFont(EAdFont font) {
		this.font = font;
	}

	@Override
	public EAdPaint getTextPaint() {
		return textPaint;
	}

	public void setTextPaint(EAdPaint textColor) {
		this.textPaint = textColor;
	}

	@Override
	public boolean hasBubble() {
		return hasBubble;
	}

	@Override
	public EAdPaint getBubblePaint() {
		return bubblePaint;
	}

	public void setBubblePaint(EAdPaint bubbleColor) {
		this.bubblePaint = bubbleColor;
		setHasBubble(bubbleColor != null);
	}

	@Override
	public int getPreferredWidth() {
		return preferredWidth;
	}

	@Override
	public int getPreferredHeight() {
		return preferredHeight;
	}

	/**
	 * Sets preferred width for this text. Could be a positive number or
	 * {@link EAdCaption#AUTO_SIZE} or {@link EAdCaption#SCREEN_SIZE}
	 * 
	 * @param maxWidth
	 *            the width
	 * 
	 */
	@Override
	public void setPreferredWidth(int maxWidth) {
		this.preferredWidth = maxWidth;
	}

	/**
	 * Sets preferred height for this text. Could be a positive number or
	 * {@link EAdCaption#AUTO_SIZE} or {@link EAdCaption#SCREEN_SIZE}
	 * 
	 * @param maxHeight
	 *            the height
	 */
	@Override
	public void setPreferredHeight(int maxHeight) {
		this.preferredHeight = maxHeight;
	}

	public void setHasBubble(boolean b) {
		this.hasBubble = b;
	}

	@Override
	public int getPadding() {
		return padding;
	}

	@Override
	public void setPadding(int padding) {
		this.padding = padding;
	}

	@Override
	public EAdList<EAdOperation> getFields() {
		return fields;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	@Override
	public Alignment getAlignment() {
		return alignment;
	}

	public EAdString getLabel() {
		return label;
	}

	public void setLabel(EAdString label) {
		this.label = label;
	}

	public boolean isHasBubble() {
		return hasBubble;
	}

}

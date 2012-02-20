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

import ead.common.interfaces.Param;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.params.BasicFont;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.PaintFill;
import ead.common.params.paint.EAdPaint;
import ead.common.params.text.EAdFont;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.EAdCaption;
import ead.common.resources.assets.drawable.basics.enums.Alignment;

public class Caption implements EAdCaption {

	/**
	 * Default padding for bubbles
	 */
	private static final int DEFAULT_PADDING = 30;

	@Param("label")
	private EAdString label;

	@Param("font")
	private EAdFont font;

	@Param("textColor")
	private EAdPaint textPaint;

	@Param("hasBubble")
	private boolean hasBubble;

	@Param("bubbleColor")
	private EAdPaint bubblePaint;

	@Param("preferredWidth")
	private int preferredWidth;

	@Param("preferredHeight")
	private int preferredHeight;

	@Param("padding")
	private int padding;

	@Param("fields")
	private EAdList<EAdOperation> fields;

	@Param("alignment")
	private Alignment alignment;

	public Caption() {
		this(EAdString.newRandomEAdString("label"));
	}

	public Caption(EAdString label) {
		this.label = label;
		textPaint = new PaintFill(ColorFill.BLACK, ColorFill.WHITE);
		bubblePaint = new PaintFill(ColorFill.WHITE, ColorFill.BLACK);
		this.font = BasicFont.BIG;
		this.hasBubble = false;
		this.bubblePaint = null;
		preferredHeight = AUTO_SIZE;
		preferredWidth = AUTO_SIZE;
		padding = DEFAULT_PADDING;
		alignment = Alignment.CENTER;
		fields = new EAdListImpl<EAdOperation>(EAdOperation.class);
	}

	public Caption(String string) {
		this(EAdString.newEAdString(string));
	}

	@Override
	public EAdString getText() {
		return label;
	}

	@Override
	public EAdFont getFont() {
		return font;
	}

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

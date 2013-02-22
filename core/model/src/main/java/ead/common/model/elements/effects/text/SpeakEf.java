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

package ead.common.model.elements.effects.text;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.enums.Alignment;
import ead.common.model.assets.drawable.basics.shapes.extra.BalloonType;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.elements.effects.AbstractEffect;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.paint.EAdPaint;
import ead.common.model.params.text.EAdString;

/**
 * 
 * An {@code EAdSpeakEffect} shows a comic balloon with a text, with the origin
 * at a given position
 * 
 */
@Element
public class SpeakEf extends AbstractEffect {

	private static final Paint BUBBLE_PAINT = new Paint(new ColorFill(255, 255,
			255, 220), ColorFill.BLACK, 2);

	@Param
	private EAdOperation x;

	@Param
	private EAdOperation y;

	@Param
	private Caption caption;

	@Param
	private EAdPaint bubbleColor;

	@Param
	private BalloonType ballonType;

	@Param
	private EAdField<String> stateField;

	public SpeakEf() {

	}

	/**
	 * Creates an speak effect, with no text and no position, with text color of
	 * {@link Paint#WHITE_ON_BLACK} and bubble color of
	 * {@link Paint#BLACK_ON_WHITE}
	 */
	public SpeakEf(EAdString string) {
		super();
		caption = new Caption(string);
		caption.setTextPaint(ColorFill.BLACK);
		bubbleColor = BUBBLE_PAINT;
		ballonType = BalloonType.ROUNDED_RECTANGLE;
	}

	public SpeakEf(String stringId) {
		this(new EAdString(stringId));
	}

	public SpeakEf(String stringId, EAdFont font) {
		this(new EAdString(stringId));
		caption.setFont(font);
	}

	/**
	 * Sets the variable that determines the origin of the balloon
	 * 
	 * @param x
	 *            x coordinate for the speaker
	 * @param y
	 *            y coordinate for the speaker
	 * 
	 */
	public void setPosition(EAdOperation x, EAdOperation y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the color of the balloon
	 * 
	 * @param textPaint
	 *            text color
	 * @param bubbleColor
	 *            bubble color
	 */
	public void setColor(EAdPaint textPaint, EAdPaint bubbleColor) {
		caption.setTextPaint(textPaint);
		this.bubbleColor = bubbleColor;
	}

	public void setBalloonType(BalloonType type) {
		this.ballonType = type;
	}

	public BalloonType getBallonType() {
		return ballonType;
	}

	public void setFont(EAdFont font) {
		this.caption.setFont(font);
	}

	public Caption getCaption() {
		return caption;
	}

	public void setCaption(Caption caption) {
		this.caption = caption;
	}

	public EAdPaint getBubbleColor() {
		return bubbleColor;
	}

	public EAdOperation getX() {
		return x;
	}

	public EAdOperation getY() {
		return y;
	}

	public void setAlignment(Alignment alignment) {
		this.caption.setAlignment(alignment);
	}

	public void setX(EAdOperation x) {
		this.x = x;
	}

	public void setY(EAdOperation y) {
		this.y = y;
	}

	public void setBubbleColor(EAdPaint bubbleColor) {
		this.bubbleColor = bubbleColor;
	}

	public void setBallonType(BalloonType ballonType) {
		this.ballonType = ballonType;
	}

	public EAdString getString() {
		return caption.getLabel();
	}

	public void setStateField(EAdField<String> stateField) {
		this.stateField = stateField;
	}

	public EAdField<String> getStateField() {
		return this.stateField;
	}

}

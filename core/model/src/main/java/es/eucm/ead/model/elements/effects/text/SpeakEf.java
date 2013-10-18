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

package es.eucm.ead.model.elements.effects.text;

import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.enums.Alignment;
import es.eucm.ead.model.assets.drawable.basics.shapes.extra.BalloonType;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.text.EAdString;

/**
 * 
 * An {@code EAdSpeakEffect} shows a comic balloon with a text, with the origin
 * at a given position
 * 
 */
@Element
public class SpeakEf extends Effect {

	private static final Paint BUBBLE_PAINT = new Paint(new ColorFill(255, 255,
			255, 220), ColorFill.BLACK, 2);

	@Param
	private Operation x;

	@Param
	private Operation y;

	@Param
	private Caption caption;

	@Param
	private EAdPaint bubbleColor;

	@Param
	private BalloonType ballonType;

	@Param
	private ElementField<String> stateField;

	@Param
	private int time;

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
		caption.setFont(BasicFont.BIG);
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
	public void setPosition(Operation x, Operation y) {
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

	public Operation getX() {
		return x;
	}

	public Operation getY() {
		return y;
	}

	public void setAlignment(Alignment alignment) {
		this.caption.setAlignment(alignment);
	}

	public void setX(Operation x) {
		this.x = x;
	}

	public void setY(Operation y) {
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

	public void setStateField(ElementField<String> stateField) {
		this.stateField = stateField;
	}

	public ElementField<String> getStateField() {
		return this.stateField;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}

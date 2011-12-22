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

package es.eucm.eadventure.common.model.elements.effects.text;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.effects.AbstractEffect;
import es.eucm.eadventure.common.model.elements.variables.EAdOperation;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdPaintImpl;
import es.eucm.eadventure.common.params.paint.EAdPaint;
import es.eucm.eadventure.common.params.text.EAdFont;
import es.eucm.eadventure.common.params.text.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.enums.Alignment;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.extra.BalloonType;

/**
 * 
 * An {@code EAdSpeakEffect} shows a comic balloon with a text, with the origin
 * at a given position
 * 
 */
@Element(runtime = SpeakEf.class, detailed = SpeakEf.class)
public class SpeakEf extends AbstractEffect {

	@Param("x")
	private EAdOperation x;

	@Param("y")
	private EAdOperation y;
	
	@Param("caption")
	private CaptionImpl caption;

	@Param("bubbleColor")
	private EAdPaint bubbleColor;

	@Param(value="ballonType", defaultValue="ROUNDED_RECTANGLE")
	private BalloonType ballonType;

	/**
	 * Creates an speak effect, with no text and no position, with text color of
	 * {@link EAdPaintImpl#WHITE_ON_BLACK} and bubble color of
	 * {@link EAdPaintImpl#BLACK_ON_WHITE}
	 */
	public SpeakEf() {
		this(EAdString.newEAdString("string"));
	}

	public SpeakEf(EAdString text) {
		super();
		caption = new CaptionImpl(text);
		caption.setTextPaint(EAdColor.BLACK);
		bubbleColor = EAdPaintImpl.BLACK_ON_WHITE;
		caption.setFont(EAdFontImpl.REGULAR);
		ballonType = BalloonType.ROUNDED_RECTANGLE;
		caption.setAlignment(Alignment.LEFT);
		setQueueable(true);
		setOpaque(true);
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
	
	public Caption getCaption( ){
		return caption;
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
	
	
}

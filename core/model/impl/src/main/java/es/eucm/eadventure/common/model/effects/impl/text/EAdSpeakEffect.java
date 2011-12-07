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

package es.eucm.eadventure.common.model.effects.impl.text;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.paint.EAdPaint;
import es.eucm.eadventure.common.resources.assets.drawable.basics.enums.Alignment;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra.BalloonType;

/**
 * 
 * An {@code EAdSpeakEffect} shows a comic balloon with a text, with the origin
 * at a given position
 * 
 */
@Element(runtime = EAdSpeakEffect.class, detailed = EAdSpeakEffect.class)
public class EAdSpeakEffect extends AbstractEAdEffect {

	@Param("x")
	private EAdOperation x;

	@Param("y")
	private EAdOperation y;

	@Param("string")
	private EAdString string;

	@Param("textColor")
	private EAdPaint textColor;

	@Param("bubbleColor")
	private EAdPaint bubbleColor;

	@Param(value="font", defaultValue="Arial:25.0:PLAIN")
	private EAdFontImpl font;

	@Param(value="ballonType", defaultValue="ROUNDED_RECTANGLE")
	private BalloonType ballonType;

	@Param("alignment")
	private Alignment alignment;

	/**
	 * Creates an speak effect, with no text and no position, with text color of
	 * {@link EAdPaintImpl#WHITE_ON_BLACK} and bubble color of
	 * {@link EAdPaintImpl#BLACK_ON_WHITE}
	 */
	public EAdSpeakEffect() {
		this(EAdString.newEAdString("string"));
	}

	public EAdSpeakEffect(EAdString text) {
		super();
		textColor = EAdColor.BLACK;
		bubbleColor = EAdPaintImpl.BLACK_ON_WHITE;
		font = EAdFontImpl.REGULAR;
		ballonType = BalloonType.ROUNDED_RECTANGLE;
		string = text;
		alignment = Alignment.LEFT;
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
	 * @param textColor
	 *            text color
	 * @param bubbleColor
	 *            bubble color
	 */
	public void setColor(EAdPaint textColor, EAdPaint bubbleColor) {
		this.textColor = textColor;
		this.bubbleColor = bubbleColor;
	}

	public void setBalloonType(BalloonType type) {
		this.ballonType = type;
	}

	public BalloonType getBallonType() {
		return ballonType;
	}

	public void setFont(EAdFont font) {
		this.font = (EAdFontImpl) font;
	}

	public EAdString getString() {
		return string;
	}

	public EAdPaint getTextColor() {
		return textColor;
	}

	public EAdPaint getBubbleColor() {
		return bubbleColor;
	}

	public EAdFont getFont() {
		return font;
	}

	public EAdOperation getX() {
		return x;
	}

	public EAdOperation getY() {
		return y;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public void setX(EAdOperation x) {
		this.x = x;
	}

	public void setY(EAdOperation y) {
		this.y = y;
	}

	public void setTextColor(EAdPaint textColor) {
		this.textColor = textColor;
	}

	public void setBubbleColor(EAdPaint bubbleColor) {
		this.bubbleColor = bubbleColor;
	}

	public void setBallonType(BalloonType ballonType) {
		this.ballonType = ballonType;
	}

	public void setString(EAdString string) {
		this.string = string;
	}

	
	
	
}

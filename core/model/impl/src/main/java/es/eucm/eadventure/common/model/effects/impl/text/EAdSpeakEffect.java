package es.eucm.eadventure.common.model.effects.impl.text;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.paint.EAdPaint;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BallonShape.BalloonType;

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
	private final EAdString string;

	@Param("textColor")
	private EAdPaint textColor;

	@Param("bubbleColor")
	private EAdPaint bubbleColor;

	@Param("font")
	private EAdFontImpl font;

	@Param("ballonType")
	private BalloonType ballonType;

	/**
	 * Creates an speak effect, with no text and no position, with text color of
	 * {@link EAdPaintImpl#WHITE_ON_BLACK} and bubble color of
	 * {@link EAdPaintImpl#BLACK_ON_WHITE}
	 * 
	 * @param id
	 */
	public EAdSpeakEffect(String id) {
		super(id);
		textColor = EAdColor.BLACK;
		bubbleColor = EAdPaintImpl.BLACK_ON_WHITE;
		font = EAdFontImpl.REGULAR;
		ballonType = BalloonType.ROUNDED_RECTANGLE;
		string = EAdString.newEAdString("string");
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
	public void setColor(EAdPaintImpl textColor, EAdPaintImpl bubbleColor) {
		this.textColor = textColor;
		this.bubbleColor = bubbleColor;
	}

	public void setBalloonType(BalloonType type) {
		this.ballonType = type;
	}

	public BalloonType getBallonType() {
		return ballonType;
	}

	public void setFont(EAdFontImpl font) {
		this.font = font;
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

	public EAdFontImpl getFont() {
		return font;
	}
	
	public EAdOperation getX(){
		return x;
	}
	
	public EAdOperation getY(){
		return y;
	}

}

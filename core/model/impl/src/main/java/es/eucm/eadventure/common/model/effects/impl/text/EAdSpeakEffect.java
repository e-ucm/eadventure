package es.eucm.eadventure.common.model.effects.impl.text;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
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
	private EAdField<Integer> x;
	
	@Param("y")
	private EAdField<Integer> y;
	
	@Param("dispX")
	private EAdField<Float> dispX;
	
	@Param("dispY")
	private EAdField<Float> dispY;
	
	@Param("width")
	private EAdField<Integer> width;
	
	@Param("height")
	private EAdField<Integer> height;

	@Param("stateVar")
	private EAdField<String> stateVar;

	@Param("string")
	private EAdString string;

	@Param("textColor")
	private EAdBorderedColor textColor;
	
	@Param("bubbleColor")
	private EAdBorderedColor bubbleColor;

	@Param("font")
	private EAdFontImpl font;
	
	@Param("ballonType")
	private BalloonType ballonType;

	@Param("scale")
	private EAdField<Float> scale;

	/**
	 * Creates an speak effect, with no text and no position, with text color of
	 * {@link EAdBorderedColor#WHITE_ON_BLACK} and bubble color of
	 * {@link EAdBorderedColor#BLACK_ON_WHITE}
	 * 
	 * @param id
	 */
	public EAdSpeakEffect(String id) {
		super(id);
		textColor = EAdBorderedColor.WHITE_ON_BLACK;
		bubbleColor = EAdBorderedColor.BLACK_ON_WHITE;
		font = EAdFontImpl.REGULAR;
		ballonType = BalloonType.RECTANGLE;
	}

	/**
	 * Sets the text for this effect
	 * 
	 * @param string
	 */
	public void setText(EAdString string) {
		this.string = string;
	}

	/**
	 * Sets the variable that determines the origin of the balloon
	 * 
	 * @param position
	 *            variable holding the position 
	 */
	public void setPosition(EAdField<Integer> x, EAdField<Integer> y, EAdField<Float> dispX, EAdField<Float> dispY) {
		this.x = x;
		this.y = y;
		this.dispX = dispX;
		this.dispY = dispY;
	}
	
	public void setDimensions( EAdField<Integer> width, EAdField<Integer> height, EAdField<Float> scale ){
		this.width = width;
		this.height = height;
		this.scale = scale;
	}

	/**
	 * Sets the color of the balloon
	 * 
	 * @param textColor
	 *            text color
	 * @param bubbleColor
	 *            bubble color
	 */
	public void setColor(EAdBorderedColor textColor,
			EAdBorderedColor bubbleColor) {
		this.textColor = textColor;
		this.bubbleColor = bubbleColor;
	}

	/**
	 * Sets the state variable to be changed to talking state when this effect
	 * happens
	 * 
	 * @param stateVar the state var
	 */
	public void setStateVar(EAdField<String> stateVar) {
		this.stateVar = stateVar;
	}
	
	public void setBalloonType( BalloonType type ){
		this.ballonType = type;
	}
	
	public BalloonType getBallonType( ){
		return ballonType;
	}
	
	public EAdField<String> getStateVar(){
		return stateVar;
	}

	public void setFont(EAdFontImpl font) {
		this.font = font;
	}

	public EAdString getString() {
		return string;
	}

	public EAdBorderedColor getTextColor() {
		return textColor;
	}

	public EAdBorderedColor getBubbleColor() {
		return bubbleColor;
	}

	public EAdFontImpl getFont() {
		return font;
	}
	
	public EAdField<Integer> getWidth(){
		return width;
	}
	
	public EAdField<Integer> getHeight(){
		return height;
	}
	
	public EAdField<Float> getScale(){
		return scale;
	}
	
	public EAdField<Integer> getX() {
		return x;
	}

	public EAdField<Integer> getY() {
		return y;
	}

	public EAdField<Float> getDispX() {
		return dispX;
	}

	public EAdField<Float> getDispY() {
		return dispY;
	}

}

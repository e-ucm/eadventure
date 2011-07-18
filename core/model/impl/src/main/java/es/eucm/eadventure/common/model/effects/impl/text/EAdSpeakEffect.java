package es.eucm.eadventure.common.model.effects.impl.text;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdFont;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.resources.EAdString;

/**
 * 
 * An {@code EAdSpeakEffect} shows a comic balloon with a text, with the origin
 * at a given position
 * 
 */
@Element(runtime = EAdShowText.class, detailed = EAdShowText.class)
public class EAdSpeakEffect extends AbstractEAdEffect {

	private EAdVar<Integer> posX, posY;

	private EAdVar<String> stateVar;

	private EAdString string;

	private EAdBorderedColor textColor, bubbleColor;

	private EAdFont font;

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
		font = EAdFont.REGULAR;
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
	 * Sets the variables that determines the origin of the balloon
	 * 
	 * @param posX
	 *            variable for x coordinate
	 * @param posY
	 *            variable for y coordinate
	 */
	public void setPosition(EAdVar<Integer> posX, EAdVar<Integer> posY) {
		this.posX = posX;
		this.posY = posY;
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
	public void setStateVar(EAdVar<String> stateVar) {
		this.stateVar = stateVar;
	}
	
	public EAdVar<String> getStateVar(){
		return stateVar;
	}

	public void setFont(EAdFont font) {
		this.font = font;
	}

	public EAdVar<Integer> getPosX() {
		return posX;
	}

	public EAdVar<Integer> getPosY() {
		return posY;
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

	public EAdFont getFont() {
		return font;
	}

}

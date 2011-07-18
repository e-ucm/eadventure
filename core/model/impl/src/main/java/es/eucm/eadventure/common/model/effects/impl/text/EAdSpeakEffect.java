package es.eucm.eadventure.common.model.effects.impl.text;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.resources.EAdString;

@Element(runtime = EAdShowText.class, detailed = EAdShowText.class)
public class EAdSpeakEffect extends AbstractEAdEffect {
	
	private EAdVar<Integer> posX, posY;
	
	private EAdString string;
	
	private EAdBorderedColor textColor, bubbleColor;

	public EAdSpeakEffect(String id) {
		super(id);
		textColor = EAdBorderedColor.WHITE_ON_BLACK;
		bubbleColor = EAdBorderedColor.BLACK_ON_WHITE;
	}
	
	public void setText( EAdString string ){
		this.string = string;
	}
	
	public void setPosition( EAdVar<Integer> posX, EAdVar<Integer> posY ){
		this.posX = posX;
		this.posY = posY;
	}
	
	public void setColor( EAdBorderedColor textColor, EAdBorderedColor bubbleColor ){
		this.textColor = textColor;
		this.bubbleColor = bubbleColor;
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
	
	

}

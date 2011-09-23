package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;

/**
 * Effect that changes the cursor (this effect only works in some platforms)
 * 
 */
public class EAdChangeCursorEffect extends AbstractEAdEffect {

	@Param("image")
	private Image image;
	
	public EAdChangeCursorEffect(Image image){
		super("changeCursorEffect");
		this.image = image;
	}
	
	public Image getImage(){
		return image;
	}

}

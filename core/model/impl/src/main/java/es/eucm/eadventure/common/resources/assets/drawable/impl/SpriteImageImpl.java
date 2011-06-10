package es.eucm.eadventure.common.resources.assets.drawable.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.common.resources.assets.drawable.SpriteImage;

public class SpriteImageImpl implements SpriteImage {

	private static final Logger logger = Logger.getLogger("SpriteImageImpl");
	
	@Param("image")
	private Image image;
	
	@Param("totalSprites")
	private int totalSprites;

	@Param("sprite")
	private int sprite;

	public SpriteImageImpl(Image image, int totalSprites, int sprite) {
		this.image = image;
		this.totalSprites = totalSprites;
		this.sprite = sprite;
		if (sprite >= totalSprites)
			logger.log(Level.SEVERE, "Sprite number is invalid for number of sprites");
	}
	
	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public int getTotalSprites() {
		return totalSprites;
	}

	@Override
	public int getSprite() {
		return sprite;
	}

}

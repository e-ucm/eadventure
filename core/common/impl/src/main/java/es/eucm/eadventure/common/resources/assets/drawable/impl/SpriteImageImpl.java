package es.eucm.eadventure.common.resources.assets.drawable.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.common.resources.assets.drawable.SpriteImage;

public class SpriteImageImpl implements SpriteImage {

	private static final Logger logger = LoggerFactory.getLogger("SpriteImageImpl");
	
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
			logger.error("Sprite number is invalid for number of sprites");
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

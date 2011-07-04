package es.eucm.eadventure.engine.core.platform.assets.impl;

import java.awt.image.BufferedImage;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.core.platform.AssetHandler;

public class DesktopEngineSpriteImage extends RuntimeSpriteImage {

	@Inject
	public DesktopEngineSpriteImage(AssetHandler assetHandler) {
		super(assetHandler);
	}

	public BufferedImage getImage() {
		return ((DesktopEngineImage) assetHandler.getRuntimeAsset(descriptor.getImage())).getImage();
	}

	public int getSprite() {
		return descriptor.getSprite();
	}

	public int getTotalSprites() {
		return descriptor.getTotalSprites();
	}



}

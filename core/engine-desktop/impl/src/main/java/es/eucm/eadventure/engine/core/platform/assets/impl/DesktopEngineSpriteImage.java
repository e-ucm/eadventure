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
		return ((DesktopEngineImage) assetHandler.getRuntimeAsset(assetDescriptor.getImage())).getImage();
	}

	public int getSprite() {
		return assetDescriptor.getSprite();
	}

	public int getTotalSprites() {
		return assetDescriptor.getTotalSprites();
	}



}

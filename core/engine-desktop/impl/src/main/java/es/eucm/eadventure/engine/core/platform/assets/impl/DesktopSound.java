package es.eucm.eadventure.engine.core.platform.assets.impl;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.core.platform.AssetHandler;

public class DesktopSound extends RuntimeSound {


	@Inject
	public DesktopSound(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		// FIXME Use some library to play most of sounds (VLC could do that?)
		return true;
	}

	@Override
	public void freeMemory() {
        
	}

	@Override
	public boolean isLoaded() {
		return true;
	}

	@Override
	public void play() {
		
	}

}

package es.eucm.eadventure.engine.core.platform.assets.impl;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.core.platform.AssetHandler;

public class PlayNSound extends RuntimeSound {


	@Inject
	public PlayNSound(AssetHandler assetHandler) {
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

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}

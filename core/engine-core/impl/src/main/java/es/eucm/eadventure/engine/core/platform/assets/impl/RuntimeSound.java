package es.eucm.eadventure.engine.core.platform.assets.impl;

import com.google.inject.Inject;

import es.eucm.eadventure.common.resources.assets.multimedia.Sound;
import es.eucm.eadventure.engine.core.platform.AssetHandler;

public abstract class RuntimeSound extends AbstractRuntimeAsset<Sound> {

	protected AssetHandler assetHandler;

	@Inject
	public RuntimeSound(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
	}

	@Override
	public void update() {

	}

	/**
	 * Plays the sound
	 */
	public abstract void play();

	/**
	 * Stops playing the sound
	 */
	public abstract void stop();

}

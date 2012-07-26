package ead.engine.core.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.google.inject.Inject;

import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.multimedia.RuntimeSound;

public class GdxSound extends RuntimeSound {

	private Sound sound;
	
	private long id;

	@Inject
	public GdxSound(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		sound = Gdx.audio.newSound(Gdx.files.internal("data/"
				+ this.descriptor.getUri().toString().substring(1)));
		return true;
	}

	@Override
	public void freeMemory() {
		sound.dispose();
	}

	@Override
	public boolean isLoaded() {
		return sound != null;
	}

	@Override
	public void play() {
		id = sound.play(1);

	}

	@Override
	public void stop() {
		sound.stop(id);
	}

}

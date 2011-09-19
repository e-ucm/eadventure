package es.eucm.eadventure.engine.core.platform.assets.impl.sound;

import java.io.IOException;
import java.io.InputStream;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeSound;
import es.eucm.eadventure.engine.core.platform.impl.DesktopAssetHandler;

public class DesktopSound extends RuntimeSound {

	private InputStream inputStream;

	private Sound sound;

	@Inject
	public DesktopSound(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		String path = getAssetDescriptor().getURI().getPath();
		inputStream = ((DesktopAssetHandler) assetHandler)
				.getResourceAsStream(path);
		if (path.endsWith(".mid") || path.endsWith(".midi")) {
			sound = new SoundMidi(inputStream, false);
		} else if (path.endsWith(".mp3")) {
			sound = new SoundMp3(inputStream, false);
		}

		return inputStream != null;
	}

	@Override
	public void freeMemory() {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isLoaded() {
		return inputStream != null;
	}

	@Override
	public void play() {
		if (sound != null)
			sound.playOnce();
	}

	@Override
	public void stop() {
		if (sound != null)
			sound.startPlaying();
	}

}

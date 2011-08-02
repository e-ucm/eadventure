package es.eucm.eadventure.engine.assets;

import java.io.File;

import android.media.MediaPlayer;
import android.net.Uri;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.AndroidAssetHandler;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeSound;

public class AndroidSound extends RuntimeSound {

	private MediaPlayer mediaPlayer;

	@Inject
	public AndroidSound(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		Uri uri = Uri.fromFile(new File(assetHandler
				.getAbsolutePath(this.descriptor.getURI())));
		mediaPlayer = MediaPlayer.create(
				((AndroidAssetHandler) assetHandler).getContext(), uri);

		return mediaPlayer != null;
	}

	@Override
	public void freeMemory() {
		mediaPlayer.release();
		mediaPlayer = null;
	}

	@Override
	public boolean isLoaded() {
		return mediaPlayer != null;
	}

	@Override
	public void play() {
		mediaPlayer.start();
	}

}

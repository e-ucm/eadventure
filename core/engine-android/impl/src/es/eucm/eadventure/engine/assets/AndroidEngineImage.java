package es.eucm.eadventure.engine.assets;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeImage;

public class AndroidEngineImage extends RuntimeImage {

	public Bitmap image;
	
	private static final Logger logger = Logger.getLogger("AndroidEngineImage");
	
	@Inject
	public AndroidEngineImage(AssetHandler assetHandler) {
		 super(assetHandler);
		
	}		
	
	public AndroidEngineImage(Bitmap image) {
		super(null);
		this.image = image;
	}

	public Bitmap getImage() {
		return image;
	}

	@Override
	public int getWidth() {
		if (image != null)
			return image.getWidth();
		return 1;
	}

	@Override
	public int getHeight() {
		if (image != null)
			return image.getHeight();
		return 1;
	}
	
	@Override
	public boolean loadAsset() {
		BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
	
		image = BitmapFactory.decodeFile(assetHandler.getAbsolutePath(descriptor.getURI()), sBitmapOptions);

		logger.info("New instance, loaded = " + (image != null));
		return image != null;
	}
	
	@Override
	public void freeMemory() {
		if (image != null) {
			image.recycle();
			logger.log(Level.INFO, "Image recycled: " + (descriptor != null ? descriptor.getURI() : "no descriptor"));
		}
		image = null;
	}

	@Override
	public boolean isLoaded() {
		return image != null;
	}
	
}

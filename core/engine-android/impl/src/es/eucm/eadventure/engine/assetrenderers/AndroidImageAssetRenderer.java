package es.eucm.eadventure.engine.assetrenderers;

import java.util.logging.Logger;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.assets.AndroidEngineImage;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;

import android.graphics.Canvas;
import android.graphics.Matrix;

@Singleton
public class AndroidImageAssetRenderer implements AssetRenderer<Canvas, AndroidEngineImage> {

	private static final Logger logger = Logger.getLogger("AndroidImageAssetRenderer");

	@Override
	public void render(Canvas graphicContext, AndroidEngineImage asset,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		if (asset != null && asset.getImage() != null) {
			int x = position.getJavaX(asset.getWidth() * scale) + offsetX;
			int y = position.getJavaY(asset.getHeight() * scale) + offsetY;

			if (scale == 1.0f){
				graphicContext.drawBitmap(asset.getImage(), x, y, null);
			}
			else {					
				Matrix matrix = new Matrix();					
				matrix.postScale(scale, scale);
				matrix.postTranslate(x, y);
				graphicContext.drawBitmap(asset.getImage(), matrix, null);
			}
		} else {
			if (asset == null)
				logger.info("Null asset..." );
			else {
				logger.info("Null image: " + asset.getAssetDescriptor().getURI());
			}
		}
	}	

	public void sizeChanged(int width, int height) {	        
	}

	@Override
	public boolean contains(int x, int y, AndroidEngineImage asset) {
		if (asset != null && asset.getImage() != null) {
			if (x < asset.getWidth() && y < asset.getHeight())
				return true;
		}
		return false;
	}


}



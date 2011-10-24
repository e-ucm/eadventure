package es.eucm.eadventure.engine.assets.specialassetrenderers;

import android.app.Activity;
import android.content.Intent;

import com.google.inject.Inject;

import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.engine.AndroidAssetHandler;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.SpecialAssetRenderer;

public class AndroidVideoRenderer implements SpecialAssetRenderer<Video, Object> {
	
	private AndroidAssetHandler assetHandler;
	
	private Intent i;
	
	public static boolean finished;
	
	@Inject
	public AndroidVideoRenderer(AssetHandler assetHandler) {
		this.assetHandler = (AndroidAssetHandler) assetHandler;
		finished = false;
	}

	@Override
	public Object getComponent(Video asset) {
		
		if (assetHandler != null) {

			String path = assetHandler.getAbsolutePath(asset.getURI().getPath());
			i = new Intent(assetHandler.getContext(), AndroidVideoActivity.class);
			i.putExtra("media_path", path);
			return i;
		}
		
		return null;
	}

	@Override
	public boolean isFinished() {

		return finished;
	}

	@Override
	public boolean start() {
		
		finished = false;		
		((Activity)assetHandler.getContext()).startActivityForResult(i, 0);
		return true;
	}

}

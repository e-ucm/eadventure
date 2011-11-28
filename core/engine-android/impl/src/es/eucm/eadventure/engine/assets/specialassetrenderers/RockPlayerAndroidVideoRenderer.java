package es.eucm.eadventure.engine.assets.specialassetrenderers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.google.inject.Inject;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.engine.AndroidAssetHandler;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.SpecialAssetRenderer;

public class RockPlayerAndroidVideoRenderer implements SpecialAssetRenderer<Video, Object> {
	
	private AndroidAssetHandler assetHandler;
	
	private Intent i;
	
	public static boolean finished;
	
	@Inject
	public RockPlayerAndroidVideoRenderer(AssetHandler assetHandler) {
		this.assetHandler = (AndroidAssetHandler) assetHandler;
		finished = false;
	}

	@Override
	public Object getComponent(Video asset) {
		
		if (assetHandler != null){
			
			String destAddr = assetHandler.getAbsolutePath(asset.getUri().getPath());
			String pack="com.redirectin.rockplayer.android.unified.lite";
			Uri uri = Uri.parse(destAddr.toString());
			i = new Intent("android.intent.action.VIEW");		 
			i.setPackage(pack);
			i.setDataAndType(uri, "video/*");
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
		((Activity)assetHandler.getContext()).startActivityForResult(i, 1);
		return true;
	}

}

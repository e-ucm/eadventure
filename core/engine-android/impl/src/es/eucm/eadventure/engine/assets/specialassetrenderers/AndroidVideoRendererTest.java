package es.eucm.eadventure.engine.assets.specialassetrenderers;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.common.resources.assets.multimedia.impl.VideoImpl;
import es.eucm.eadventure.engine.AndroidAssetHandler;
import es.eucm.eadventure.engine.R;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.extra.AndroidAssetHandlerModule;
import es.eucm.eadventure.engine.extra.AndroidModule;

public class AndroidVideoRendererTest extends Activity {
	
	static final int ANDROID_PLAYER_RESULT = 0;
	
	static final int ROCKPLAYER_RESULT = 1;	
	
	private String sdcard;

	private Injector injector;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.main);

		injector = Guice.createInjector(new AndroidAssetHandlerModule(), new AndroidModule(),
				new BasicGameModule());

		// TODO fix this
		AndroidAssetHandler aah = (AndroidAssetHandler) injector
				.getInstance(AssetHandler.class);
		aah.setResources(getResources());
		aah.setContext(this);
		
		sdcard = Environment.getExternalStorageDirectory().toString();
		File file = new File(sdcard + "/eAd2/flame.mp4");
		//File file = new File(sdcard + "/eAd2/flame.mpg");
		Video video = new VideoImpl(file.getAbsolutePath());
		final AndroidVideoRenderer androidVideoRenderer = new AndroidVideoRenderer(aah);
		//final RockPlayerAndroidVideoRenderer androidVideoRenderer = new RockPlayerAndroidVideoRenderer(aah);
		androidVideoRenderer.getComponent(video);
		androidVideoRenderer.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		System.gc();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ANDROID_PLAYER_RESULT) {
        	AndroidVideoRenderer.finished = true;
        	this.finish();
        }
        else if (resultCode == ROCKPLAYER_RESULT){
        	RockPlayerAndroidVideoRenderer.finished = true;
        	this.finish();
        }
 
    }

}



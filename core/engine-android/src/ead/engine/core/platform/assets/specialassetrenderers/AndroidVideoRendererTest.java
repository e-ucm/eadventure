/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.engine.core.platform.assets.specialassetrenderers;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Video;
import ead.engine.R;
import ead.engine.core.platform.AndroidAssetHandler;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.modules.AndroidAssetHandlerModule;
import ead.engine.core.platform.modules.AndroidModule;
import ead.engine.core.platform.modules.BasicGameModule;

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
		EAdVideo video = new Video(file.getAbsolutePath());
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



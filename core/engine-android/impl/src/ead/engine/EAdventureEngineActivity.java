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

package ead.engine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.util.StringHandler;
import ead.elementfactories.EAdElementsFactory;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameController;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.AndroidAssetHandler;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.specialassetrenderers.AndroidVideoRenderer;
import ead.engine.core.platform.assets.specialassetrenderers.RockPlayerAndroidVideoRenderer;
import ead.engine.core.platform.extra.EAdventureSurfaceView;
import ead.engine.core.platform.modules.AndroidAssetHandlerModule;
import ead.engine.core.platform.modules.AndroidModule;
import ead.engine.core.platform.modules.BasicGameModule;

public class EAdventureEngineActivity extends Activity {

	static final int ANDROID_PLAYER_RESULT = 0;

	static final int ROCKPLAYER_RESULT = 1;

	private GameController gameController;

	private DisplayMetrics dm;

	private Injector injector;

	private EAdventureSurfaceView surfaceView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		injector = Guice.createInjector(new AndroidAssetHandlerModule(),
				new AndroidModule(), new BasicGameModule());

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		EngineConfiguration config = injector
				.getInstance(EngineConfiguration.class);

		int height, width;
		height = dm.heightPixels;
		width = dm.widthPixels;

		// In case wrong display metrics like Samsung GT 10.1
		if (height > width) {
			config.setSize(height, width);
		} else {
			config.setSize(width, height);
		}
		config.setFullscreen(true);

		// TODO fix this
		AndroidAssetHandler aah = (AndroidAssetHandler) injector
				.getInstance(AssetHandler.class);
		aah.setResources(getResources());
		aah.setContext(this);

		@SuppressWarnings("unchecked")
		Class<? extends EAdScene> demoClass = (Class<? extends EAdScene>) getIntent()
				.getExtras().getSerializable("demo");

		BasicScene sceneImpl = (BasicScene) injector.getInstance(demoClass);

		StringHandler sh = injector.getInstance(StringHandler.class);
		sh.addStrings(EAdElementsFactory.getInstance().getStringFactory()
				.getStrings());

		EAdAdventureModel model = new BasicAdventureModel();
		BasicChapter c1 = new BasicChapter();
		c1.setId("chapter1");
		c1.getScenes().add(sceneImpl);
		c1.setInitialScene(sceneImpl);
		model.getChapters().add(c1);
		Game g = injector.getInstance(Game.class);
		g.setGame(model, c1);

		surfaceView = new EAdventureSurfaceView(this);
		setContentView(surfaceView);
		surfaceView.start(injector.getInstance(GUI.class), config,
				injector.getInstance(InputHandler.class));

		gameController = injector.getInstance(GameController.class);
		// FIXME no null, something else
		gameController.start(null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		gameController.pause();
		surfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		gameController.resume();
		surfaceView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gameController.stop();

		System.gc();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ANDROID_PLAYER_RESULT) {
			AndroidVideoRenderer.finished = true;
		} else if (resultCode == ROCKPLAYER_RESULT) {
			RockPlayerAndroidVideoRenderer.finished = true;
		}

	}

}
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

package es.eucm.eadventure.engine;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.common.elmentfactories.scenedemos.BasicScene;
import es.eucm.eadventure.common.elmentfactories.scenedemos.CharacterScene;
import es.eucm.eadventure.common.elmentfactories.scenedemos.ShapeScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;
import es.eucm.eadventure.engine.core.GameController;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.extra.AndroidAssetHandlerModule;
import es.eucm.eadventure.engine.extra.AndroidAssetRendererModule;
import es.eucm.eadventure.engine.extra.AndroidModule;
import es.eucm.eadventure.engine.extra.EAdventureSurfaceView;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class EAdventureEngineActivity extends Activity {

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
        
        surfaceView = new EAdventureSurfaceView(this);
        setContentView(surfaceView);

        injector = Guice.createInjector(new AndroidAssetHandlerModule(), new AndroidAssetRendererModule(null), new AndroidModule(), new BasicGameModule());

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        AndroidPlatformConfiguration config = (AndroidPlatformConfiguration) injector.getInstance(PlatformConfiguration.class);
        config.setWidth(dm.widthPixels);
        config.setHeight(dm.heightPixels);
        config.setFullscreen(true);

        //TODO fix this
        AndroidAssetHandler aah = (AndroidAssetHandler) injector.getInstance(AssetHandler.class);
        aah.setResources(getResources());

		LoadingScreen loadingScreen = injector.getInstance(LoadingScreen.class);

		EAdSceneImpl sceneImpl = new EAdSceneImpl("scene");
		sceneImpl.getBackground().getResources().addAsset(sceneImpl.getBackground().getInitialBundle(), EAdBasicSceneElement.appearance, new ImageImpl("@drawable/background1.png"));

		sceneImpl = new CharacterScene();
		
		loadingScreen.setInitialScreen(sceneImpl);

        surfaceView.start(injector.getInstance(GUI.class),
        		config,
        		injector.getInstance(MouseState.class));
        
        gameController = injector.getInstance(GameController.class);
        gameController.start();        
    }

	@Override
    protected void onPause() {
		super.onPause();
		gameController.pause();
    }
    
	@Override
    protected void onResume() {
    	super.onResume();
    	gameController.resume();
    }
    
	@Override
	protected void onDestroy() {
        super.onDestroy();
        gameController.stop();

        Runtime r = Runtime.getRuntime();
        r.gc();
    }
	
}
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

package ead.engine.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdField;
import ead.common.params.fills.ColorFill;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition.Corner;
import ead.engine.android.core.platform.AndroidAssetHandler;
import ead.engine.android.core.platform.AndroidInputListener;
import ead.engine.android.gl.EAdRenderer;
import ead.engine.android.gl.GLAssetHandler;
import ead.engine.android.gl.GLAssetHandlerModule;
import ead.engine.android.gl.GLCanvas;
import ead.engine.android.gl.GLGUI;
import ead.engine.android.gl.GLModule;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameController;
import ead.engine.core.game.GameLoop;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.java.core.platform.modules.JavaBasicGameModule;

public class EAdventureGLActivity extends Activity {

	private GLSurfaceView glSurfaceView;
	
	private GLAssetHandler assetHandler;
	
	private GameLoop gameLoop;

	private Injector injector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		initInjector();
		
		AndroidAssetHandler aah = (AndroidAssetHandler) injector
				.getInstance(AssetHandler.class);
		aah.setResources(getResources());
		aah.setContext(this);
		
		glSurfaceView = new GLSurfaceView(this);

		// Check if the system supports OpenGL ES 2.0.
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager
				.getDeviceConfigurationInfo();
		boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			glSurfaceView.setEGLContextClientVersion(2);
			glSurfaceView.setRenderer(new EAdRenderer(injector
					.getInstance(GLCanvas.class), injector
					.getInstance(GameObjectManager.class)));
		} else {
			return;
		}
		
		InputHandler inputHandler = injector.getInstance(InputHandler.class);
		glSurfaceView.setOnTouchListener(new AndroidInputListener(inputHandler));
		
		gameLoop = injector.getInstance(GameLoop.class);
		gameLoop.setTicksPerSecond(60);
		
		assetHandler = injector.getInstance(GLAssetHandler.class);
		
		GLGUI gui = injector.getInstance(GLGUI.class);
		glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		gui.setGLSurfaceView(glSurfaceView);
		setContentView(glSurfaceView);
		
		
		
		EAdAdventureModel model = new BasicAdventureModel();
		BasicChapter c1 = new BasicChapter();
		c1.setId("chapter1");
		c1.getScenes().add(getScene());
		c1.setInitialScene(getScene());
		model.getChapters().add(c1);
		Game g = injector.getInstance(Game.class);
		g.setGame(model, c1);
		
		GameController gameController = injector.getInstance(GameController.class);
		gameController.start(null);

		

	}

	private void initInjector() {
		injector = Guice.createInjector(new JavaBasicGameModule(), new GLModule(),
				new GLAssetHandlerModule());
	}
	
	private EAdScene getScene( ){
		Image i = new Image("@drawable/loading.png");
		EAdScene scene = new BasicScene(i);
		SceneElement element = new SceneElement(new Image("@drawable/mole1.png"));
		element.setPosition(Corner.CENTER, 400, 300);
		scene.getSceneElements().add(element);
		
		EAdField<Float> rotationField = new BasicField<Float>(element, SceneElement.VAR_ROTATION);
		EAdField<Float> scaleField = new BasicField<Float>(element, SceneElement.VAR_SCALE);
		
		InterpolationEf ef = new InterpolationEf(rotationField, 0.0f, (float) (2 * Math.PI), 500, InterpolationLoopType.REVERSE, InterpolationType.ACCELERATE );
		InterpolationEf ef2 = new InterpolationEf(scaleField, 0.0f, 2.0f, 200, InterpolationLoopType.REVERSE, InterpolationType.BOUNCE_END );
		
		SceneElementEv event = new SceneElementEv( );
//		event.addEffect(SceneElementEvType.ADDED_TO_SCENE, ef);
//		event.addEffect(SceneElementEvType.ADDED_TO_SCENE, ef2);
		
		scene.getEvents().add(event);
		
		element.setInitialAlpha(0.0f);
		EAdField<Float> alpha = new BasicField<Float>(element, SceneElement.VAR_ALPHA);
		InterpolationEf aplhaef = new InterpolationEf(alpha, 0.0f, 1.0f, 5000, InterpolationLoopType.REVERSE, InterpolationType.LINEAR );
		event.addEffect(SceneElementEvType.ADDED_TO_SCENE, aplhaef);
		
		element.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, ef);
		
		SceneElement e2 = new SceneElement(new RectangleShape( 600, 500, new ColorFill(255, 100, 100, 100)));
		e2.setPosition(20, 20);
		scene.getSceneElements().add(e2);
		
		return scene;
		
//		return new NgMainScreen(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
//		glSurfaceView.queueEvent(new Runnable( ){
//
//			@Override
//			public void run() {
//				assetHandler.onResume();
//			}
//			
//		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
		gameLoop.stop();
		
	}

}

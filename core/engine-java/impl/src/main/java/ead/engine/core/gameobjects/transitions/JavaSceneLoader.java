package ead.engine.core.gameobjects.transitions;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.scene.EAdScene;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.go.transitions.SceneLoaderListener;
import ead.engine.core.platform.AssetHandler;

@Singleton
public class JavaSceneLoader extends AbstractSceneLoader {

	@Inject
	public JavaSceneLoader(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory) {
		super(assetHandler, sceneElementFactory);
	}

	@Override
	public void loadScene(EAdScene scene, SceneLoaderListener listener) {
		super.loadScene(scene, listener);
		new Thread( ){
			public void run( ){
				loadScene();
				sceneLoaderListener.sceneLoaded(sceneGO);
			}
		}.start();
	}
	
	public void freeUnusedAssets(SceneGO<?> currentScene){
		super.freeUnusedAssets(currentScene);
		new Thread( ){
			public void run( ){
				freeScene();
			}
		}.start();
	}
	
	

}

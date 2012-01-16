package ead.engine.core.gameobjects.transitions;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.scene.EAdScene;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.go.transitions.SceneLoaderListener;
import ead.engine.core.platform.AssetHandler;

@Singleton
public class PlayNSceneLoader extends AbstractSceneLoader {

	@Inject
	public PlayNSceneLoader(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory) {
		super(assetHandler, sceneElementFactory);
	}

	@Override
	public void loadScene(EAdScene scene, SceneLoaderListener listener) {
		super.loadScene(scene, listener);
		loadScene();
		sceneLoaderListener.sceneLoaded(sceneGO);
	}
	
	public void freeUnusedAssets(SceneGO<?> currentScene) {
		super.freeUnusedAssets(currentScene);
		super.freeScene();
	}

}

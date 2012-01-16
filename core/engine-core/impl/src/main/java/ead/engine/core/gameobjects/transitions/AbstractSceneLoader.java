package ead.engine.core.gameobjects.transitions;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.scene.EAdScene;
import ead.common.resources.assets.AssetDescriptor;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.gameobjects.go.transitions.SceneLoaderListener;
import ead.engine.core.platform.AssetHandler;

public abstract class AbstractSceneLoader implements SceneLoader {

	private SceneElementGOFactory sceneElementFactory;

	private AssetHandler assetHandler;

	private List<AssetDescriptor> assetsList;

	private List<AssetDescriptor> currentAssets;

	protected SceneLoaderListener sceneLoaderListener;

	private EAdScene scene;

	protected SceneGO<?> sceneGO;

	protected SceneGO<?> currentSceneGO;

	public AbstractSceneLoader(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory) {
		this.sceneElementFactory = sceneElementFactory;
		this.assetHandler = assetHandler;
		assetsList = new ArrayList<AssetDescriptor>();
		currentAssets = new ArrayList<AssetDescriptor>();
	}

	public void loadScene(EAdScene scene, SceneLoaderListener listener) {
		this.scene = scene;
		this.sceneLoaderListener = listener;
	}

	protected void loadScene() {
		sceneGO = (SceneGO<?>) sceneElementFactory.get(scene);
		assetsList.clear();

		assetsList = sceneGO.getAssets(assetsList, false);

		for (AssetDescriptor asset : assetsList) {
			if (asset != null)
				assetHandler.getRuntimeAsset(asset, true);
		}
	}

	public void freeUnusedAssets(SceneGO<?> currentScene) {
		this.currentSceneGO = currentScene;
	}

	protected void freeScene() {
		currentAssets.clear();
		assetHandler.clean(currentSceneGO.getAssets(currentAssets, true));
	}

}

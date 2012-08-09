package ead.engine.core.gameobjects.transitions.sceneloaders;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.scene.EAdScene;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.gameobjects.go.transitions.SceneLoaderListener;
import ead.engine.core.platform.assets.AssetHandler;
import ead.tools.SceneGraph;

@Singleton
public class GraphSceneLoader implements SceneLoader {

	private AssetHandler assetHandler;

	private SceneLoaderListener listener;

	private SceneElementGOFactory factory;

	private EAdScene scene;

	private SceneGraph sceneGraph;
	
	private List<EAdScene> sceneRemainingToBeLoad;

	@Inject
	public GraphSceneLoader(AssetHandler assetHandler,
			SceneElementGOFactory factory, SceneGraph sceneGraph) {
		this.assetHandler = assetHandler;
		this.factory = factory;
		this.sceneGraph = sceneGraph;
		this.sceneRemainingToBeLoad = new ArrayList<EAdScene>();
	}

	@Override
	public void loadScene(EAdScene scene, SceneLoaderListener listener) {
		this.listener = listener;
		this.scene = scene;
		sceneRemainingToBeLoad.clear();
		// Removes the assets that are loading, since they are not longer needed
		assetHandler.clearAssetQueue();
		assetHandler.queueSceneToLoad(scene);
		// If there is a lot of scenes connected to this scene, we should load
		// some of them, because probably they'll be required soon. We used a random decision
		List<EAdScene> connections = sceneGraph.getGraph().get(scene); 
		for ( int i = 0; i < connections.size(); i++){
			// These scenes will be loaded BEFORE the scene changes
			if ( Math.random() > 0.5f ){
				assetHandler.queueSceneToLoad(connections.get(i));
			}
			// These scenes will be loaded AFTER the scene changes
			else {
				sceneRemainingToBeLoad.add(connections.get(i));
			}
		}
		
	}

	@Override
	public void freeUnusedAssets(SceneGO<?> currentScene, SceneGO<?> oldScene) {
		// TODO Auto-generated method stub

	}

	@Override
	public void step() {
		if (!assetHandler.loadStep()) {
			SceneGO<?> sceneGO = (SceneGO<?>) factory.get(scene);
			sceneGO.update();
			// Add the next scenes, so they load in the background
			for (EAdScene s : sceneRemainingToBeLoad) {
				assetHandler.queueSceneToLoad(s);
			}
			listener.sceneLoaded(sceneGO);
		}
	}

}

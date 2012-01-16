package ead.engine.core.gameobjects.go.transitions;

import ead.common.model.elements.scene.EAdScene;
import ead.engine.core.gameobjects.go.SceneGO;

/**
 * Implemented by those classes loading scenes assets
 * 
 */
public interface SceneLoader {

	/**
	 * Loads the scene assets.
	 * 
	 * @param scene
	 *            The scene to load
	 * @param listener
	 *            the listener will be notices when the load is complete
	 */
	void loadScene(EAdScene scene, SceneLoaderListener listener);

	/**
	 * Frees all the assets contained by the scene, and not used by the
	 * currentScene
	 * @param currentScene
	 *            the current scene
	 */
	void freeUnusedAssets(SceneGO<?> currentScene);

}

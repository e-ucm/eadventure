package ead.engine.core.gameobjects.go.transitions;

import ead.engine.core.gameobjects.go.SceneGO;

/**
 * 
 * Implemented by elements interested in knowing when a scene is loaded by a
 * {@link SceneLoader}
 * 
 */
public interface SceneLoaderListener {

	/**
	 * Triggers when the scene is loaded
	 * 
	 * @param sceneGO
	 *            the game object for the loaded scene
	 */
	void sceneLoaded(SceneGO<?> sceneGO);
}

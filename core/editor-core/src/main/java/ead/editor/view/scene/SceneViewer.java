package ead.editor.view.scene;

import ead.common.model.elements.scene.EAdScene;
import ead.editor.view.scene.listener.SceneListener;

/**
 * General interface for scene viewers
 * 
 */
public interface SceneViewer {

	/**
	 * Sets the scene to be viewed
	 * 
	 * @param scene
	 *            the scene
	 */
	void setScene(EAdScene scene);

	/**
	 * Updates the view. This method is called when changes in the model must
	 * be reflected in the viewer
	 */
	void updateView();

	/**
	 * Adds a field change listener for the view.
	 * 
	 * @param listener
	 */
	void addFieldChangeListener(SceneListener listener);

	/**
	 * Removes the given listener
	 * 
	 * @param listener
	 */
	void removeFieldChangeListener(SceneListener listener);
	

}

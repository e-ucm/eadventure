package ead.editor.view.scene.listener;

import java.util.List;

import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.variables.VarDef;

/**
 * General interface able to listen to changes in some scene viewer
 * 
 */
public interface SceneListener {

	/**
	 * Updates some variable to some value in the given element
	 * 
	 * @param var
	 *            the var definition
	 * @param element
	 *            the scene element
	 * @param value
	 *            the value for the var
	 */
	<T> void updateInitialValue(VarDef<T> var, EAdSceneElement element, T value);

	/**
	 * Notifies that the elements selected in the scene viewer have changed.
	 * 
	 * @param sceneElements
	 *            a list with the selected elements. {@code null} if there is no
	 *            selection
	 */
	void updateSelection(List<EAdSceneElement> sceneElements);

}

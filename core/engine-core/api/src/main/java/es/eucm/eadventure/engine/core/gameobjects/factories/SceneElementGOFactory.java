package es.eucm.eadventure.engine.core.gameobjects.factories;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;

public interface SceneElementGOFactory
		extends
		GameObjectFactory<EAdSceneElement, SceneElementGO<? extends EAdSceneElement>> {

	/**
	 * Removes an element from the cache
	 * 
	 * @param element
	 *            the element to be removed
	 */
	void remove(EAdSceneElement element);

	/**
	 * Remove all elements from the cache
	 */
	void clean();
	
}

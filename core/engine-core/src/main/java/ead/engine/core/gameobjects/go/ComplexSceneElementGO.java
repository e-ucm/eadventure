package ead.engine.core.gameobjects.go;

import java.util.List;

import ead.common.model.elements.scenes.EAdComplexSceneElement;

public interface ComplexSceneElementGO<T extends EAdComplexSceneElement>
		extends SceneElementGO<T> {

	/**
	 * Returns the game object in the given relative coordinates
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	DrawableGO<?> getFirstGOIn(int x, int y);

	/**
	 * Returns a list with all the game objects in the given relative
	 * coordinates. The returned list shouldn't be modified nor expected to be
	 * consistent for more than update
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	List<DrawableGO<?>> getAllGOIn(int x, int y);

}

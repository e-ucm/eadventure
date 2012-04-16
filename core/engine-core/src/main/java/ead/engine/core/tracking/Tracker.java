package ead.engine.core.tracking;

import ead.common.model.elements.scene.EAdSceneElement;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.input.InputAction;

/**
 * General interface for game engine trackers. Methods defined by this interface
 * can be extended if required
 * 
 */
public interface Tracker {

	/**
	 * Tracks an input action executed over the target
	 * 
	 * @param action
	 *            the performed action
	 * @param target
	 *            the game object receiving the action
	 */
	void track(InputAction<?> action, DrawableGO<?> target);

	/**
	 * Tracks a launched effect
	 * 
	 * @param effect
	 *            the launched effect
	 * @param parent
	 *            the effect's owner
	 */
	void track(EffectGO<?> effect, EAdSceneElement parent);

}

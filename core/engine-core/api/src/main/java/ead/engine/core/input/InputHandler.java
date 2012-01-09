package ead.engine.core.input;

import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.util.EAdTransformation;

/**
 * An element registering all the GUI actions performed, and the current state
 * for all the input methods (mouse, keyboards, joysticks...)
 * 
 */
public interface InputHandler {

	/**
	 * Adds an input action to be processed
	 * 
	 * @param action
	 *            the input action (a mouse action, a keyboard action...)
	 */
	void addAction(InputAction<?> action);

	/**
	 * Check if the input is in the given state
	 * 
	 * @param state
	 *            the input state to check
	 * @return
	 */
	boolean checkState(InputState state);

	/**
	 * Process the GUI actions pending in the queue
	 */
	void processActions();

	/**
	 * Returns the x coordinate of the pointer (normally the mouse). This
	 * coordinate is expressed in the game coordinate system
	 * 
	 * @return the pointer's x coordinate
	 */
	int getPointerX();

	/**
	 * Returns the y coordinate of the pointer (normally the mouse). This
	 * coordinate is expressed in the game coordinate system
	 * 
	 * @return the pointer's y coordinate
	 */
	int getPointerY();

	/**
	 * Returns the raw x coordinate of the pointer. This coordinate is the
	 * absolute position in window coordinates system
	 * 
	 * @return the x coordinate of the pointer in the window
	 */
	int getRawPointerX();

	/**
	 * Returns the raw y coordinate of the pointer. This coordinate is the
	 * absolute position in window coordinates system
	 * 
	 * @return the y coordinate of the pointer in the window
	 */
	int getRawPointerY();

	/**
	 * Returns the element under the pointer (normally, the mouse)
	 * 
	 * @return the element under the pointer
	 */
	DrawableGO<?> getGameObjectUnderPointer();

	SceneElementGO<?> getDraggingGameObject();

	EAdSceneElementDef getDraggingElement();

	void setInitialTransformation(EAdTransformation initialTransformation);

}

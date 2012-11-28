package ead.engine.core.gameobjects.go;

import ead.engine.core.input.InputAction;

public interface InputActionProcessor {

	/**
	 * Process the action in the graphic interface (click, etc.)
	 * 
	 * @param action
	 *            the action to process
	 * @return the object that processed the action. {@code null} if no one
	 *         processed it
	 */
	DrawableGO<?> processAction(InputAction<?> action);

}

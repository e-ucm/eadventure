package es.eucm.eadventure.common.model.events.enums;

import com.gwtent.reflection.client.Reflectable;

@Reflectable
public enum SceneElementEventType {

	/**
	 * Triggered when the element is added to the scene
	 */
	ADDED_TO_SCENE,
	
	/**
	 * Triggered in every update
	 */
	ALWAYS
	
}

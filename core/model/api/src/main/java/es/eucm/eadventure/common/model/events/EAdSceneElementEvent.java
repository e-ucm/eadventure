package es.eucm.eadventure.common.model.events;

/**
 * Represents event that could happen to a scene element
 * 
 * 
 */
public interface EAdSceneElementEvent extends EAdEvent {

	public enum SceneElementEvent {
		/**
		 * Triggered when the element is added to the scene
		 */
		ADDED_TO_SCENE
	};
}

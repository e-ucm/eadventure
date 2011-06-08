package es.eucm.eadventure.common.model.events;

/**
 * <p>
 * eAdventure event that is triggered by system events, such as the game being loaded
 * </p>
 * <p>
 * This event is usually used to change the scene to the first one in the game,
 * or to make a button to start the actual game visible.
 * </p>
 */
public interface EAdSystemEvent extends EAdEvent {
	
	enum Event { GAME_LOADED }
	
}

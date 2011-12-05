package es.eucm.eadventure.common.interfaces;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.guievents.EAdGUIEvent;

/**
 * Implemented by all classes with effects to GUI events.
 *
 */
public interface WithBehavior {

	/**
	 * Returns the effects list associated with the given GUI event,
	 * {@code null} if there is no effects associated. This method shouldn't be
	 * used to add new effects to the actor. Returned list could be {@code null}
	 * 
	 * @param event
	 *            the GUI event
	 * @return the effects list associated with the given event
	 */
	EAdList<EAdEffect> getEffects(EAdGUIEvent event);
	
	void addBehavior(EAdGUIEvent event, EAdEffect effect);

	/**
	 * Adds the given effects to the list of the events that will be executed
	 * when the given event is processed by this element
	 * 
	 * @param event the GUI event
	 * @param effects the list of effects
	 */
	void addBehavior(EAdGUIEvent event, EAdList<EAdEffect> effects);
}

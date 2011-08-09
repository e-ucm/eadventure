package es.eucm.eadventure.common.interfaces.features;

import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.extra.EAdList;

/**
 * General interfaces for elements with events
 * 
 *
 */
public interface Evented {
	/**
	 * Returns a list of events associated with this element
	 * 
	 * @return The list of events associated with this element
	 */
	EAdList<EAdEvent> getEvents();

}

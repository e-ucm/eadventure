package es.eucm.eadventure.common.model.weev;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.weev.adaptation.AdaptationStructure;

/**
 * WEEV data model
 */
public interface WEEVModel extends EAdElement {

	/**
	 * @return the list of {@link Actor}s in the WEEV model
	 */
	EAdList<Actor> getActors();
	
	/**
	 * @return the main {@link Actor}s in the game
	 */
	Actor getMainActor();
	
	/**
	 * @return TRUE if the game is in first person
	 */
	boolean isFirstPerson();
	
	/**
	 * @return the {@link AdaptationStructure} of the game
	 */
	AdaptationStructure getAdaptationStructure();
	
}

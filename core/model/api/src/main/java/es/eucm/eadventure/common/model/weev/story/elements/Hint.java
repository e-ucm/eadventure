package es.eucm.eadventure.common.model.weev.story.elements;

import es.eucm.eadventure.common.model.weev.WEEVElement;
import es.eucm.eadventure.common.params.EAdString;

/**
 * A hint, to help the player during the game.<p>
 * Hint have a test (the actual hint) as well as a value (the cost of requesting the hint).
 */
public interface Hint extends WEEVElement {

	/**
	 * @return the string containing the actual hint
	 */
	EAdString getHint();
	
	/**
	 * @return the cost, in points, to the palyer of requesting the hint
	 */
	int getValue();
	
}

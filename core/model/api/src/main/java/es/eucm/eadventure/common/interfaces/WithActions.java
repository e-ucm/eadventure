package es.eucm.eadventure.common.interfaces;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.extra.EAdList;

/**
 * Implemented by elements with actions
 */
public interface WithActions {

	/**
	 * Returns the list of actions associated to this element
	 * 
	 * @return
	 */
	EAdList<EAdAction> getActions();
}

package es.eucm.eadventure.common.interfaces.features;

import es.eucm.eadventure.common.model.variables.EAdElementVars;

/**
 * 
 * Implemented for those elements who hold variables
 *
 */
public interface Variabled {

	/**
	 * Returns the container for all variables of this scene element
	 * 
	 * @return
	 */
	EAdElementVars getVars();
}

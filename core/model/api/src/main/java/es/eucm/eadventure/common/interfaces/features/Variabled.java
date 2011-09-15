package es.eucm.eadventure.common.interfaces.features;

import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

/**
 * 
 * Implemented for those elements who hold variables
 * 
 */
public interface Variabled {

	/**
	 * Returns a map containing
	 * 
	 * @return
	 */
	EAdMap<EAdVarDef<?>, Object> getVars();

	/**
	 * Sets a initial value for the given variable
	 * 
	 * @param var
	 *            variable's definition
	 * @param value
	 *            the initial value
	 */
	<T> void setVarInitialValue(EAdVarDef<T> var, T value);
}

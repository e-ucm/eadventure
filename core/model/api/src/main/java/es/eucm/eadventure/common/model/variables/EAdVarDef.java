package es.eucm.eadventure.common.model.variables;

import es.eucm.eadventure.common.model.EAdElement;

/**
 * General interface for the definition of an eAdventure variable. Definitions
 * of variables are constructed from:
 * <ul>
 * <li><b>Name</b>: A string naming the variable</li>
 * <li><b>Class</b>: The class of the variable's value</li>
 * <li><b>Initial value</b>: The initial value for the variable</li>
 * </ul>
 * 
 * Variables are constructed from an {@link EAdVarDef}.
 * 
 * @param <T>
 *            Class of the variable's value
 */
public interface EAdVarDef<T> extends EAdElement {

	/**
	 * Returns the variable's name
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns the Java class for this variable
	 * 
	 * @return the Java class for this variable
	 */
	Class<T> getType();

	/**
	 * Returns the initial value for this variable
	 * 
	 * @return the initial value for this variable
	 */
	T getInitialValue();

}

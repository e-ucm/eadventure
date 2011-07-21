package es.eucm.eadventure.common.model.variables;

/**
 * General interface for the definition of an eAdventure variable. Definitions
 * of variables are constructed from:
 * <ul>
 * <li><b>Name</b>: A string naming the variable</li>
 * <li><b>Class</b>: The class of the variable's value</li>
 * <li><b>Initial value</b>: The initial value for the variable</li>
 * <li><b>Constant</b>: If the variable value can be modified during runtime</li>
 * <li><b>Global</b>: If the variable is global. Global variables are stored
 * during all the game. Not global variables are deleted in release of
 * unnecessary resources</li>
 * </ul>
 * 
 * Variables are constructed from an {@link EAdVarDef}.
 * 
 * @param <T>
 *            Class of the variable's value
 */
public interface EAdVarDef<T> {

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

	/**
	 * Returns whether this variable must remain constant during the game
	 * 
	 * @return
	 */
	boolean isConstant();
	
	/**
	 * Returns if this 
	 * @return
	 */
	boolean isGlobal();
}

package es.eucm.eadventure.common.model.variables;

import java.util.Collection;

import es.eucm.eadventure.common.model.EAdElement;

/**
 * 
 * General interface for variables containers. Every container is associated
 * with an {@link EAdElement}
 * 
 */
public interface EAdElementVars {

	/**
	 * Returns the {@link EAdElement} associated with this variables container
	 * 
	 * @return
	 */
	EAdElement getElement();

	/**
	 * Returns the {@link EAdVar} associated with the given definition
	 * 
	 * @param var
	 *            the variable's definition
	 * @return the variable
	 */
	<T> EAdVar<T> getVar(EAdVarDef<T> var);

	/**
	 * Returns the variable for the given name
	 * 
	 * @param name
	 *            the name
	 * @param name
	 *            the variable class
	 * @return the variable
	 */
	<T> EAdVar<T> getVar(String name, Class<T> clazz);

	/**
	 * Constructs a variable from the given definition, and it's added to this
	 * container
	 * 
	 * @param var
	 *            the variable definition
	 */
	<T> void add(EAdVarDef<T> varDef);

	/**
	 * Adds a variable to the container
	 * 
	 * @param var
	 */
	<T> void add(EAdVar<T> var);

	/**
	 * Returns all the variables in the container
	 * 
	 * @return
	 */
	Collection<EAdVar<?>> getVars();

}

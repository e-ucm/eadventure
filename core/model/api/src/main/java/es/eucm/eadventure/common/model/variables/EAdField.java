package es.eucm.eadventure.common.model.variables;

import es.eucm.eadventure.common.model.EAdElement;

/**
 * A field is a variable definition associated to an {@link EAdElement}
 * 
 * 
 */
public interface EAdField<T> extends EAdElement {

	/**
	 * Returns the element holding the field
	 * 
	 * @return
	 */
	EAdElement getElement();

	/**
	 * Returns the variable definition for the field
	 * 
	 * @return
	 */
	EAdVarDef<T> getVarDefinition();

}

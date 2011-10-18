package es.eucm.eadventure.common.model.variables;

import es.eucm.eadventure.common.model.EAdElement;

/**
 * A field is a variable definition associated to an {@link EAdElement}
 * 
 * 
 */
public interface EAdField<T> extends EAdElement, EAdOperation {

	/**
	 * Returns the element holding the field. If the element is {@code null}
	 * should be considered as a system variable
	 * 
	 * @return
	 */
	EAdElement getElement();
	
	/**
	 * Returns a field pointing to the element of this field, if exists
	 * @return
	 */
	EAdField<EAdElement> getElementField();

	/**
	 * Returns the variable definition for the field
	 * 
	 * @return
	 */
	EAdVarDef<T> getVarDefinition();

}

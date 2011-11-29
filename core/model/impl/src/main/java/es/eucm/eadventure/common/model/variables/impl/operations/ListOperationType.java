package es.eucm.eadventure.common.model.variables.impl.operations;

import com.gwtent.reflection.client.Reflectable;

@Reflectable
public enum ListOperationType {
	/**
	 * Return the list with a random order
	 */
	RANDOM_LIST,
	
	/**
	 * Returns a random element of the list
	 */
	RANDOM_ELEMENT;
}
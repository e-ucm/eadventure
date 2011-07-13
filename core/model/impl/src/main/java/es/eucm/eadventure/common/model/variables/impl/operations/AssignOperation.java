package es.eucm.eadventure.common.model.variables.impl.operations;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.variables.impl.EAdOperationImpl;

/**
 * 
 * An assign operation. Assigns a given value to the variable
 * 
 */
@Element(runtime = AssignOperation.class, detailed = AssignOperation.class)
public class AssignOperation extends EAdOperationImpl {

	private Object value;

	/**
	 * Creates an assign operation
	 * 
	 * @param id
	 *            the id
	 * @param value
	 *            the value to be assigned
	 */
	public AssignOperation(String id, Object value) {
		super(id);
		this.value = value;
	}

	/**
	 * Returns the value to be assigned
	 * 
	 * @return
	 */
	public Object getValue() {
		return value;
	}

}

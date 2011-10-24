package es.eucm.eadventure.common.model.variables.impl.operations;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdOperationImpl;

@Element(runtime = ListOperation.class, detailed = ListOperation.class)
@SuppressWarnings("rawtypes")
public class ListOperation extends EAdOperationImpl {
	
	public enum Operation {
		/**
		 * Return the list with a random order
		 */
		RANDOM_LIST,
		
		/**
		 * Returns a random element of the list
		 */
		RANDOM_ELEMENT;
	}
	
	@Param("operation")
	private Operation operation;
	
	@Param("listfield")
	private EAdField<EAdList> listField;

	public ListOperation(EAdField<EAdList> listField, Operation operation) {
		super("listOperation");
		this.operation = operation;
		this.listField = listField;
	}

	public Operation getOperation() {
		return operation;
	}

	public EAdField<EAdList> getListField() {
		return listField;
	}

}

package es.eucm.eadventure.common.model.variables.impl.operations;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdOperationImpl;

@Element(runtime = ListOperation.class, detailed = ListOperation.class)
@SuppressWarnings("rawtypes")
public class ListOperation extends EAdOperationImpl {
	
	@Param("operation")
	private ListOperationType operation;
	
	@Param("listfield")
	private EAdField<EAdList> listField;

	public ListOperation(EAdField<EAdList> listField, ListOperationType operation) {
		super();
		setId("listOperation");
		this.operation = operation;
		this.listField = listField;
	}

	public ListOperationType getOperation() {
		return operation;
	}

	public EAdField<EAdList> getListField() {
		return listField;
	}

}

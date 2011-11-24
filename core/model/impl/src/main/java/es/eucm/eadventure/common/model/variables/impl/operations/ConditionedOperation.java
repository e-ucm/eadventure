package es.eucm.eadventure.common.model.variables.impl.operations;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.impl.EAdOperationImpl;

@Element(detailed = ConditionedOperation.class, runtime = ConditionedOperation.class)
public class ConditionedOperation extends EAdOperationImpl {

	@Param("condition")
	private EAdCondition condition;
	
	@Param("opTrue")
	private EAdOperation opTrue;
	
	@Param("opFalse")
	private EAdOperation opFalse;
	
	
	public ConditionedOperation(EAdCondition c, EAdOperation opTrue, EAdOperation opFalse ) {
		super();
		setId("conditionedOperation");
		this.opTrue = opTrue;
		this.opFalse = opFalse;
		this.condition = c;
	}


	public EAdCondition getCondition() {
		return condition;
	}


	public EAdOperation getOpTrue() {
		return opTrue;
	}


	public EAdOperation getOpFalse() {
		return opFalse;
	}

}

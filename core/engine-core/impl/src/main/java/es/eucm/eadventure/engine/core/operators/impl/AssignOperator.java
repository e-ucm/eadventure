package es.eucm.eadventure.engine.core.operators.impl;

import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.operations.AssignOperation;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.operator.Operator;

public class AssignOperator implements Operator<AssignOperation> {

	private ValueMap valueMap;

	public AssignOperator(ValueMap valueMap) {
		this.valueMap = valueMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S operate(EAdVar<S> varResult, AssignOperation operation) {
		if (varResult.getType().equals(operation.getValue().getClass())) {
			valueMap.setValue(varResult, (S) operation.getValue());
			return (S) operation.getValue();
		}
		return null;

	}

}

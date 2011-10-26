package es.eucm.eadventure.engine.core.operators.impl;

import es.eucm.eadventure.common.model.variables.impl.operations.ConditionedOperation;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.operator.Operator;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;

public class ConditionedOperator implements Operator<ConditionedOperation> {

	private EvaluatorFactory evaluatorFactory;

	private OperatorFactory operatorFactory;

	public ConditionedOperator(EvaluatorFactory evaluatorFactory,
			OperatorFactory operatorFactory) {
		this.evaluatorFactory = evaluatorFactory;
		this.operatorFactory = operatorFactory;
	}

	@Override
	public <S> S operate(Class<S> clazz, ConditionedOperation operation) {
		if (evaluatorFactory.evaluate(operation.getCondition())) {
			return operatorFactory.operate(clazz, operation.getOpTrue());
		} else
			return operatorFactory.operate(clazz, operation.getOpFalse());
	}

}

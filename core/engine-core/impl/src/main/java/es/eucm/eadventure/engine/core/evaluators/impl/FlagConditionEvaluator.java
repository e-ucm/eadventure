package es.eucm.eadventure.engine.core.evaluators.impl;

import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.Evaluator;

public class FlagConditionEvaluator implements Evaluator<FlagCondition>{
	
	private ValueMap valueMap;
	
	public FlagConditionEvaluator(ValueMap valueMap){
		this.valueMap = valueMap;
	}

	@Override
	public boolean evaluate(FlagCondition condition) {
		return valueMap.getValue(condition.getFlag());
	}

}

package es.eucm.eadventure.engine.core.operators.impl;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.operator.Operator;

public class FieldOperator implements Operator<EAdField<?>> {
	
	private ValueMap valueMap;
	
	@Inject
	public FieldOperator( ValueMap valueMap ){
		this.valueMap = valueMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S operate(Class<S> clazz, EAdField<?> operation) {
		return (S) valueMap.getValue(operation);
	}

}

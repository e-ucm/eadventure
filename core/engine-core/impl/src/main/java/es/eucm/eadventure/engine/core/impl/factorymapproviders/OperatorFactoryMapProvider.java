package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.variables.impl.operations.AssignOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.engine.core.EvaluatorFactory;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.operator.Operator;
import es.eucm.eadventure.engine.core.operators.impl.AssignOperator;
import es.eucm.eadventure.engine.core.operators.impl.BooleanOperator;
import es.eucm.eadventure.engine.core.operators.impl.LiteralExpressionOperator;

@Singleton
public class OperatorFactoryMapProvider extends AbstractMapProvider<Class<?>, Operator<?>> {

	private static Map<Class<?>, Operator<?>> tempMap = new HashMap<Class<?>, Operator<?>>();

	@Inject
	public OperatorFactoryMapProvider(ValueMap valueMap, EvaluatorFactory evaluatorFactory) {
		super();
		factoryMap.put(LiteralExpressionOperation.class, new LiteralExpressionOperator(valueMap));
		factoryMap.put(BooleanOperation.class, new BooleanOperator(valueMap, evaluatorFactory));
		factoryMap.put(AssignOperation.class, new AssignOperator(valueMap));
		factoryMap.putAll(tempMap);
	}
	
	public static void add(Class<?> operation, Operator<?> operator) {
		tempMap.put(operation, operator);
	}

	
}

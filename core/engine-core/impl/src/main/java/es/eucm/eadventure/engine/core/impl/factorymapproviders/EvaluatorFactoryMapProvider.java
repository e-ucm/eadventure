package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.conditions.impl.ORCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.engine.core.EvaluatorFactory;
import es.eucm.eadventure.engine.core.evaluators.Evaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.EmptyConditionEvaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.FlagConditionEvaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.ListedConditionEvaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.NOTConditionEvaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.VarConditionEvaluator;

@Singleton
public class EvaluatorFactoryMapProvider extends AbstractMapProvider<Class<?>, Evaluator<?>> {

	private static Map<Class<?>, Evaluator<?>> tempMap = new HashMap<Class<?>, Evaluator<?>>();
	
	@Inject
	public EvaluatorFactoryMapProvider(EvaluatorFactory evaluatorFactory, Injector injector) {
		super();
		factoryMap.put(EmptyCondition.class, injector.getInstance(EmptyConditionEvaluator.class));
		factoryMap.put(FlagCondition.class, injector.getInstance(FlagConditionEvaluator.class));
		factoryMap.put(VarVarCondition.class, injector.getInstance(VarConditionEvaluator.class));
		factoryMap.put(VarValCondition.class, injector.getInstance(VarConditionEvaluator.class));
		factoryMap.put(ANDCondition.class, injector.getInstance(ListedConditionEvaluator.class));
		factoryMap.put(ORCondition.class, injector.getInstance(ListedConditionEvaluator.class));
		factoryMap.put(NOTCondition.class, injector.getInstance(NOTConditionEvaluator.class));
		factoryMap.putAll(tempMap);
	}
	
	public static void add(Class<?> condition, Evaluator<?> evaluator) {
		tempMap.put(condition, evaluator);
	}
	
}

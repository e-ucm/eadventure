package es.eucm.eadventure.engine.core.platform.impl.extra;

import com.google.inject.Inject;
import com.google.inject.Provider;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.evaluators.impl.EvaluatorFactoryImpl;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;

public class EvaluatorFactoryProvider implements Provider<EvaluatorFactory> {

	private EvaluatorFactoryImpl evaluatorFactory;
	
	private ReflectionProvider reflectionProvider;
	
	private ValueMap valueMap;
	
	private OperatorFactory operatorFactory;
	
	@Inject
	public EvaluatorFactoryProvider(ReflectionProvider interfaceProvider,
			ValueMap valueMap, OperatorFactory operatorFactory) {
		this.reflectionProvider = interfaceProvider;
		this.valueMap = valueMap;
		this.operatorFactory = operatorFactory;
	}
	
	@Override
	public EvaluatorFactory get() {
		if (evaluatorFactory == null) {
			evaluatorFactory = new EvaluatorFactoryImpl(null, reflectionProvider);
			operatorFactory.setMap(PlayNOperatorMapProviderFactory.create(evaluatorFactory, reflectionProvider));
			evaluatorFactory.setMap(PlayNEvaluatorMapProviderFactory.create(valueMap, evaluatorFactory, operatorFactory));
		}
		return evaluatorFactory;
	}

}

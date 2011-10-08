package es.eucm.eadventure.engine.core.platform.impl.extra;

import com.google.inject.Inject;
import com.google.inject.Provider;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.operators.impl.OperatorFactoryImpl;

public class OperatorFactoryProvider implements Provider<OperatorFactory> {

	private OperatorFactory operatorFactory;

	private ReflectionProvider reflectionProvider;

	private EvaluatorFactory evaluatorFactory;
	
	private ValueMap valueMap;
	
	@Inject
	public OperatorFactoryProvider(
			ReflectionProvider reflectionProvider,
			ValueMap valueMap) {
//		this.evaluatorFactory = evaluatorFactory;
		this.reflectionProvider = reflectionProvider;
		this.valueMap = valueMap;
	}
	
	@Override
	public OperatorFactory get() {
		if (operatorFactory == null) {
			operatorFactory = new OperatorFactoryImpl(null, valueMap, reflectionProvider);
//			operatorFactory.setMap(PlayNOperatorMapProviderFactory.create(evaluatorFactory, reflectionProvider));
		}
		return operatorFactory;
	}

}

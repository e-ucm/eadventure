package es.eucm.eadventure.engine.core.platform.impl.extra;

import com.google.inject.Inject;
import com.google.inject.Provider;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.evaluators.impl.EvaluatorFactoryImpl;

public class EvaluatorFactoryProvider implements Provider<EvaluatorFactory> {

	private EvaluatorFactoryImpl evaluatorFactory;
	
	private ReflectionProvider interfaceProvider;
	
	private ValueMap valueMap;
	
	private EvaluatorMapProviderFactory evaluatorMapProviderFactory;
	
	@Inject
	public EvaluatorFactoryProvider(ReflectionProvider interfaceProvider,
			EvaluatorMapProviderFactory evaluatorMapProviderFactory,
			ValueMap valueMap) {
		this.interfaceProvider = interfaceProvider;
		this.evaluatorMapProviderFactory = evaluatorMapProviderFactory;
		this.valueMap = valueMap;
	}
	
	@Override
	public EvaluatorFactory get() {
		if (evaluatorFactory == null) {
			evaluatorFactory = new EvaluatorFactoryImpl(null, interfaceProvider);
			evaluatorFactory.setMap(evaluatorMapProviderFactory.create(valueMap, evaluatorFactory));
		}
		return evaluatorFactory;
	}

}

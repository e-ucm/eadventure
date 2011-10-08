package es.eucm.eadventure.engine.core.platform.impl.extra;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.OperatorFactoryMapProvider;

public class PlayNOperatorMapProviderFactory {
	
	public static OperatorFactoryMapProvider create(EvaluatorFactory evaluatorFactory,
			ReflectionProvider reflectionProvider) {
		return new OperatorFactoryMapProvider(evaluatorFactory, reflectionProvider);
	}
	
}

package es.eucm.eadventure.engine.core.platform.impl.extra;

import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.EvaluatorFactoryMapProvider;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;

public class PlayNEvaluatorMapProviderFactory {
	
	public static EvaluatorFactoryMapProvider create(ValueMap valueMap,
			EvaluatorFactory evaluatorFactory,
			OperatorFactory operatorFactory) {
		return new EvaluatorFactoryMapProvider(valueMap, evaluatorFactory, operatorFactory);
	}
	
}

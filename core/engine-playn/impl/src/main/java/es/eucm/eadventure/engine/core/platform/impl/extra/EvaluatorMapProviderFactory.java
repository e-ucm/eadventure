package es.eucm.eadventure.engine.core.platform.impl.extra;

import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.EvaluatorFactoryMapProvider;

public class EvaluatorMapProviderFactory {

	
	public EvaluatorMapProviderFactory() {
		
	}
	
	public EvaluatorFactoryMapProvider create(ValueMap valueMap,
			EvaluatorFactory evaluatorFactory) {
		return new EvaluatorFactoryMapProvider(valueMap, evaluatorFactory);
	}
	
}

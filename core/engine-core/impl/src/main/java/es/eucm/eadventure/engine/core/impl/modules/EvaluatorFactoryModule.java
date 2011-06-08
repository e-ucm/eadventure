package es.eucm.eadventure.engine.core.impl.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.MapProvider;
import es.eucm.eadventure.engine.core.EvaluatorFactory;
import es.eucm.eadventure.engine.core.evaluators.Evaluator;
import es.eucm.eadventure.engine.core.impl.EvaluatorFactoryImpl;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.EvaluatorFactoryMapProvider;

@Singleton
public class EvaluatorFactoryModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(EvaluatorFactory.class).to(EvaluatorFactoryImpl.class);
		bind(new TypeLiteral<MapProvider<Class<?>, Evaluator<?>>>() {}).to(EvaluatorFactoryMapProvider.class);
	}

}

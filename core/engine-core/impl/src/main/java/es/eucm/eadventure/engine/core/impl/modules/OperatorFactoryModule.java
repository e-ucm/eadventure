package es.eucm.eadventure.engine.core.impl.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.engine.core.OperatorFactory;
import es.eucm.eadventure.engine.core.impl.OperatorFactoryImpl;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.OperatorFactoryMapProvider;
import es.eucm.eadventure.engine.core.operator.Operator;

@Singleton
public class OperatorFactoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<MapProvider<Class<?>, Operator<?>>>() {}).to(OperatorFactoryMapProvider.class);
		bind( OperatorFactory.class).to(OperatorFactoryImpl.class);
	}

}

package es.eucm.eadventure.engine.core.gameobjects.factories;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.engine.core.gameobjects.EffectGO;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.EffectGameObjectFactoryConfigurator;
import es.eucm.eadventure.engine.core.platform.EAdInjector;

@Singleton
public class EffectGOFactoryImpl extends
		GOFactoryImpl<EAdEffect, EffectGO<? extends EAdEffect>> implements
		EffectGOFactory {

	@Inject
	public EffectGOFactoryImpl(ReflectionProvider reflectionProvider,
			EAdInjector injector) {
		super(false, reflectionProvider, injector);
		EffectGameObjectFactoryConfigurator provider = new EffectGameObjectFactoryConfigurator();
		setClassMap(provider.getMap());
	}

}

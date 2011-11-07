package es.eucm.eadventure.engine.core.gameobjects.factories;

import com.google.inject.Inject;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.engine.core.gameobjects.EventGO;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.EventGameObjectFactoryConfigurator;
import es.eucm.eadventure.engine.core.platform.EAdInjector;

public class EventGOFactoryImpl extends
		GOFactoryImpl<EAdEvent, EventGO<? extends EAdEvent>> implements
		EventGOFactory {

	@Inject
	public EventGOFactoryImpl(ReflectionProvider reflectionProvider,
			EAdInjector injector) {
		super(false, reflectionProvider, injector);
		EventGameObjectFactoryConfigurator provider = new EventGameObjectFactoryConfigurator();
		setClassMap(provider.getMap());
	}

}

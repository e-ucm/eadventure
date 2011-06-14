package es.eucm.eadventure.engine.core.impl.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.EAdElement;

import es.eucm.eadventure.engine.core.gameobjects.*;
import es.eucm.eadventure.engine.core.gameobjects.impl.*;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.GameObjectFactoryMapProvider;

public class GameObjectFactoryModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(SceneGO.class).to(SceneGOImpl.class);
		bind(ActorReferenceGO.class).to(ActorReferenceGOImpl.class);
		bind(ActorGO.class).to(ActorGOImpl.class);
		bind(TimerGO.class).to(TimerGOImpl.class);
		
		bind(new TypeLiteral<MapProvider<Class<? extends EAdElement>, Class<? extends GameObject<?>>>>() {}).to(GameObjectFactoryMapProvider.class);
		bind(GameObjectFactory.class).to(GameObjectFactoryImpl.class);
	}


}

package es.eucm.eadventure.engine.core.gameobjects.impl;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;

@Singleton
public class PlayNGameObjectFactoryImpl extends GameObjectFactoryImpl {

	public PlayNGameObjectFactoryImpl(
			MapProvider<Class<? extends EAdElement>, Class<? extends GameObject<?>>> map) {
		super(map);
	}

	@Override
	public GameObject<?> getInstance(Class<? extends GameObject<?>> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}

package es.eucm.eadventure.engine.core.gameobjects.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;

@Singleton
public abstract class JavaGameObjectFactoryImpl extends GameObjectFactoryImpl {

	/**
	 * The guice injector
	 */
	private Injector injector;

	@Inject
	public JavaGameObjectFactoryImpl(
			MapProvider<Class<? extends EAdElement>, Class<? extends GameObject<?>>> map,
			Injector injector) {
		super(map);
		this.injector = injector;
	}

	@Override
	public GameObject<?> getInstance(Class<? extends GameObject<?>> clazz) {
		return injector.getInstance(clazz);
	}

}

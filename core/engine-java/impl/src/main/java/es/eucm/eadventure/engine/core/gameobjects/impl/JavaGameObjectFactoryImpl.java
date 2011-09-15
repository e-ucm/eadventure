package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;

@Singleton
public class JavaGameObjectFactoryImpl extends GameObjectFactoryImpl {

	/**
	 * The guice injector
	 */
	private Injector injector;

	private static final Logger logger = Logger.getLogger("JavaGameObjectFactoryImpl");
	
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

	@Override
	public void remove(EAdElement element){
		objectMap.remove(element);
	}
	
	@Override
	public void clean(){
		objectMap.clear();
	}

	@Override
	public <T extends EAdElement> Class<?> getRuntimeClass(T element) {
		Class<?> clazz = element.getClass();
		Element annotation = null;
		while (annotation == null && clazz != null ) {
			annotation = clazz.getAnnotation(Element.class);
			clazz = clazz.getSuperclass();
		}
		
		if ( annotation == null ){
			logger.log(Level.SEVERE, "No element annotation for class " + element.getClass());
			return null;
		}
		return annotation.runtime();
	}

}

package es.eucm.eadventure.engine.core.gameobjects.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.platform.EAdInjector;

public class GOFactoryImpl<S extends EAdElement, T extends GameObject<? extends S>> implements
		GameObjectFactory<S, T> {

	private static final Logger logger = Logger
			.getLogger("GameObjectFactoryImpl");
	
	private ReflectionProvider reflectionProvider;
	
	private EAdInjector injector;

	private boolean useCache;

	private Map<S, T> cache;

	private Map<Class<? extends S>, Class<? extends T>> classMap;

	public GOFactoryImpl(boolean useCache,
			ReflectionProvider reflectionProvider, EAdInjector injector) {
		this.useCache = useCache;
		if (useCache) {
			cache = new HashMap<S, T>();
		}
		this.reflectionProvider = reflectionProvider;
		this.injector = injector;
	}
	
	public void setClassMap( Map<Class<? extends S>, Class<? extends T>> classMap ){
		this.classMap = classMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T get(S element) {
		GameObject temp = null;
		if (useCache) {
			temp = cache.get(element);

			if (temp != null)
				return (T) temp;
		}

		Class<?> tempClass = classMap.get(element.getClass());
		if (tempClass == null) {
			Class<?> runtimeClass = reflectionProvider.getRuntimeClass(element);
			tempClass = classMap.get(runtimeClass);
		}
		if (tempClass == null) {
			logger.log(Level.SEVERE, "No game element mapped for class "
					+ element.getClass());
		} else {
			temp = (GameObject) injector.getInstance(tempClass);
			if (temp == null)
				logger.severe("No instace for game object of class " + tempClass);
			temp.setElement(element);
			if ( useCache )
				cache.put(element, (T) temp);
		}
		return (T) temp;
	}
	

	public void remove(EAdElement element) {
		cache.remove(element);
	}

	public void clean() {
		cache.clear();
	}
	
	public void put( Class<? extends S> clazz1, Class<? extends T> clazz2 ){
		classMap.put(clazz1, clazz2);
	}

}

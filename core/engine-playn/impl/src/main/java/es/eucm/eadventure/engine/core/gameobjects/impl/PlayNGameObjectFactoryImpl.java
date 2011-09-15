package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.lang.annotation.Annotation;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.TypeOracle;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;

@Singleton
public class PlayNGameObjectFactoryImpl extends GameObjectFactoryImpl {

	private static final Logger logger = Logger.getLogger("PlayNGameObjectFactoryImpl");
	
	@Inject
	public PlayNGameObjectFactoryImpl(
			MapProvider<Class<? extends EAdElement>, Class<? extends GameObject<?>>> map) {
		super(map);
	}

	@Override
	public GameObject<?> getInstance(Class<? extends GameObject<?>> clazz) {
		// TODO Auto-generated method stub
		
		//return injector.getInstance(clazz);

		return null;
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends EAdElement> Class<?> getRuntimeClass(T element) {
		Class<?> clazz = element.getClass();
		
		Element annotation = null;
		while (annotation == null && clazz != null ) {
			ClassType<?> type = TypeOracle.Instance.getClassType(clazz.getName());
			annotation = type.getAnnotation(Element.class);
			clazz = clazz.getSuperclass();
		}
		
		if ( annotation == null ){
			logger.log(Level.SEVERE, "No element annotation for class " + element.getClass());
			return null;
		}
		return annotation.runtime();
		
	}

}

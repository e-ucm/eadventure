package es.eucm.eadventure.engine.core.platform.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.EAdElement;

@Singleton
public class JavaReflectionProvider implements ReflectionProvider {
	
	private static final Logger logger = Logger.getLogger("ReflectionProvider");

	@Override
	public Class<?>[] getInterfaces(Class<?> object) {
		return object.getInterfaces();
	}

	@Override
	public boolean isAssignableFrom(Class<?> class1, Class<?> class2) {
		return class1.isAssignableFrom(class2);
	}

	@Override
	public Class<?> getSuperclass(Class<?> c) {
		return c.getSuperclass();
	}
	
	@Override
	public Class<?> getRuntimeClass(EAdElement element) {
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

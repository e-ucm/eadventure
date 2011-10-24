package es.eucm.eadventure.engine.core.platform.impl;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;

@Singleton
public class JavaReflectionProvider implements ReflectionProvider {

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

}

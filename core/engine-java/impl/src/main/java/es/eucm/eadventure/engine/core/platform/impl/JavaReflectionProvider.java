package es.eucm.eadventure.engine.core.platform.impl;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

@Singleton
public class JavaReflectionProvider implements ReflectionProvider {

	@Override
	public Class<?>[] getInterfaces(Class<?> object) {
		return object.getInterfaces();
	}

	@Override
	public boolean isAssignableFrom(Class<?> varDef, Object value) {
		return varDef.isAssignableFrom(value.getClass());
	}

}

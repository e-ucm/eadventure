package es.eucm.eadventure.engine.core.platform.impl;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.InterfacesProvider;

@Singleton
public class JavaInterfacesProvider implements InterfacesProvider {

	@Override
	public Class<?>[] getInterfaces(Class<?> object) {
		return object.getInterfaces();
	}

}

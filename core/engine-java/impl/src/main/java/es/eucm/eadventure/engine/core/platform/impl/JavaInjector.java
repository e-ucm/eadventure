package es.eucm.eadventure.engine.core.platform.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.engine.core.platform.EAdInjector;

public class JavaInjector implements EAdInjector {
	
	private Injector injector;

	@Inject
	public JavaInjector(Injector injector){
		this.injector = injector;
	}

	@Override
	public <T> T getInstance(Class<T> clazz) {		
		return injector.getInstance(clazz);
	}

}

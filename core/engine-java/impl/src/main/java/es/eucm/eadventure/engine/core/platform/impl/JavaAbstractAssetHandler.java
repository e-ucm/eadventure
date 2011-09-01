package es.eucm.eadventure.engine.core.platform.impl;

import java.util.Map;

import com.google.inject.Injector;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public abstract class JavaAbstractAssetHandler extends AbstractAssetHandler {

	/**
	 * An instance of the guice injector, used to load the necessary runtime
	 * assets
	 */
	private Injector injector;

	
	public JavaAbstractAssetHandler(
			Injector injector,
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap) {
		super(classMap);
		this.injector = injector;
	}

	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		return injector.getInstance(clazz);
	}

}

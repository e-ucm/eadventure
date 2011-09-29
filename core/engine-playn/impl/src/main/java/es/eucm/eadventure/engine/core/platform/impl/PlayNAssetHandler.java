package es.eucm.eadventure.engine.core.platform.impl;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

@Singleton
public class PlayNAssetHandler extends AbstractAssetHandler {

	@Inject
	public PlayNAssetHandler(
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap) {
		super(classMap);
	}

	@Override
	public void initilize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAbsolutePath(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}

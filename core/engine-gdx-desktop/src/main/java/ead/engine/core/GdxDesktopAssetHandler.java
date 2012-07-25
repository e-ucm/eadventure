package ead.engine.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.assets.GdxAssetHandler;
import ead.engine.core.platform.FontHandler;
import ead.tools.GenericInjector;

@Singleton
public class GdxDesktopAssetHandler extends GdxAssetHandler {

	@Inject
	public GdxDesktopAssetHandler(GenericInjector injector,
			FontHandler fontHandler) {
		super(injector, fontHandler);
	}

	@Override
	public String getAbsolutePath(String uri) {
		return uri.substring(1);
	}

}

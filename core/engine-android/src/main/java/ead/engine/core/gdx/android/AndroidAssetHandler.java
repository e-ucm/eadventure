package ead.engine.core.gdx.android;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.assets.AssetHandlerImpl;
import ead.tools.GenericInjector;
import ead.tools.SceneGraph;

@Singleton
public class AndroidAssetHandler extends AssetHandlerImpl {

	@Inject
	public AndroidAssetHandler(GenericInjector injector, SceneGraph sceneGraph) {
		super(injector, sceneGraph);
	}

	@Override
	public void getTextfileAsync(String path, TextHandler textHandler) {
		// XXX
		textHandler.handle("");
	}
}

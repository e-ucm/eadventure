package es.eucm.eadventure.engine.core.test.launcher;

import com.google.inject.Injector;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elmentfactories.StringFactory;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;

/**
 * Base test launcher
 * 
 * 
 */
public abstract class BaseTestLauncher {

	private PlatformLauncher launcher;

	private LoadingScreen loadingScreen;

	public BaseTestLauncher(Injector injector, Class<? extends EAdScene> scene) {
		launcher = injector.getInstance(PlatformLauncher.class);
		loadingScreen = injector.getInstance(LoadingScreen.class);

		// FIXME Init strings. This probably could be done in a better way
		StringHandler sh = injector.getInstance(StringHandler.class);
		StringFactory stringFactory = new StringFactory();
		stringFactory.addStrings(sh);

		loadingScreen.setInitialScreen(injector.getInstance(scene));
	}

	/**
	 * Launches the test
	 */
	public void start() {
		launcher.launch(null);
	}

}

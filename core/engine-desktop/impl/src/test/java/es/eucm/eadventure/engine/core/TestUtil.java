package es.eucm.eadventure.engine.core;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;

public class TestUtil {

	private static Injector injector;

	public static Injector getInjector() {
		if (injector == null) {
			injector = Guice.createInjector(new DesktopAssetHandlerModule(),
					new DesktopModule(),
					new BasicGameModule());
		}
		return injector;
	}

}

package es.eucm.eadventure.engine.core.platform.impl;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNAssetRendererModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNModule;

@GinModules({PlayNAssetHandlerModule.class, PlayNAssetRendererModule.class, PlayNModule.class, BasicGameModule.class})
public interface PlayNGinInjector extends Ginjector {
	
	public PlatformLauncher getPlatformLauncher();

}

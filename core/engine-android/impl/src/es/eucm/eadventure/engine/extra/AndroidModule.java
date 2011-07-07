package es.eucm.eadventure.engine.extra;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.model.impl.inventory.EAdBasicInventory;
import es.eucm.eadventure.engine.AndroidAssetHandler;
import es.eucm.eadventure.engine.AndroidFontCache;
import es.eucm.eadventure.engine.AndroidGUI;
import es.eucm.eadventure.engine.AndroidPlatformConfiguration;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameProfiler;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.GameObjectManagerImpl;
import es.eucm.eadventure.engine.core.impl.GameLoopImpl;
import es.eucm.eadventure.engine.core.impl.GameProfilerImpl;
import es.eucm.eadventure.engine.core.impl.KeyboardStateImpl;
import es.eucm.eadventure.engine.core.impl.MouseStateImpl;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.GameObjectFactoryMapProvider;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.impl.FontCacheImpl;

public class AndroidModule extends AbstractModule {

	public AndroidModule() {
		GameObjectFactoryMapProvider.add(EAdBasicInventory.class, AndroidBasicInventoryGO.class);
	}
	
	@Override
	protected void configure() {
		install(new AndroidAssetRendererModule(null));
		bind(Boolean.class).annotatedWith(Names.named("threaded")).toInstance(Boolean.TRUE);
		bind(AssetHandler.class).to(AndroidAssetHandler.class);
		bind(GameLoop.class).to(GameLoopImpl.class);
		bind(GameProfiler.class).to(GameProfilerImpl.class);
		bind(GUI.class).to(AndroidGUI.class);
		bind(PlatformConfiguration.class).to(AndroidPlatformConfiguration.class);
		bind(MouseState.class).to(MouseStateImpl.class);
		bind(KeyboardState.class).to(KeyboardStateImpl.class);
		bind(GameObjectManager.class).to(GameObjectManagerImpl.class);
		bind(ActionsHUD.class).to(AndroidActionsHUDImpl.class);
		bind(BasicHUD.class).to(BasicHUDImpl.class);
		bind(FontCacheImpl.class).to(AndroidFontCache.class);
		bind(MenuHUD.class).to(AndroidMenuHUDImpl.class);
	}
}

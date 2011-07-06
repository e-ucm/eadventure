package es.eucm.eadventure.engine.extra;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import es.eucm.eadventure.engine.AndroidAssetHandler;
import es.eucm.eadventure.engine.AndroidGUI;
import es.eucm.eadventure.engine.AndroidPlatformConfiguration;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.GameObjectManagerImpl;
import es.eucm.eadventure.engine.core.impl.MouseStateImpl;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class AndroidModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new AndroidAssetRendererModule(null));
		bind(Boolean.class).annotatedWith(Names.named("threaded")).toInstance(Boolean.TRUE);
		bind(AssetHandler.class).to(AndroidAssetHandler.class);
		bind(GUI.class).to(AndroidGUI.class);
		bind(PlatformConfiguration.class).to(AndroidPlatformConfiguration.class);
		bind(MouseState.class).to(MouseStateImpl.class);
		bind(GameObjectManager.class).to(GameObjectManagerImpl.class);
		bind(ActionsHUD.class).to(AndroidActionsHUDImpl.class);
		bind(BasicHUD.class).to(BasicHUDImpl.class);
	}
}

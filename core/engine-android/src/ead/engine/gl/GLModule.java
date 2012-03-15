package ead.engine.gl;

import android.graphics.Canvas;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;

import ead.engine.core.gameobjects.AndroidActionsHUDImpl;
import ead.engine.core.gameobjects.AndroidBasicHUD;
import ead.engine.core.gameobjects.huds.ActionsHUD;
import ead.engine.core.gameobjects.huds.BottomBasicHUD;
import ead.engine.core.gameobjects.huds.BottomBasicHUDImpl;
import ead.engine.core.gameobjects.huds.InventoryHUD;
import ead.engine.core.gameobjects.huds.InventoryHUDImpl;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.gameobjects.huds.MenuHUDImpl;
import ead.engine.core.gameobjects.huds.TopBasicHUD;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.rendering.AndroidFilterFactory;
import ead.engine.core.platform.rendering.filters.FilterFactory;

public class GLModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(GUI.class).to(GLGUI.class);
		bind(ActionsHUD.class).to(AndroidActionsHUDImpl.class);
		bind(TopBasicHUD.class).to(AndroidBasicHUD.class);
		bind(BottomBasicHUD.class).to(BottomBasicHUDImpl.class);
		bind(InventoryHUD.class).to(InventoryHUDImpl.class);
		bind(MenuHUD.class).to(MenuHUDImpl.class);
		bind(new TypeLiteral<FilterFactory<Canvas>>(){}).to(AndroidFilterFactory.class);
	}
	
	@Provides
	@Named("threaded")
	public boolean provideThreaded() {
		return Boolean.TRUE;
	}
}

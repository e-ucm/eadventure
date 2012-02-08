/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.engine.core.platform.extra;

import java.awt.Graphics2D;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;

import ead.common.Reader;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.reader.EAdAdventureDOMModelReader;
import ead.engine.core.GameLoopImpl;
import ead.engine.core.GameProfilerImpl;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameProfiler;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.GameObjectManagerImpl;
import ead.engine.core.gameobjects.huds.ActionsHUD;
import ead.engine.core.gameobjects.huds.ActionsHUDImpl;
import ead.engine.core.gameobjects.huds.BottomBasicHUD;
import ead.engine.core.gameobjects.huds.BottomBasicHUDImpl;
import ead.engine.core.gameobjects.huds.InventoryHUD;
import ead.engine.core.gameobjects.huds.InventoryHUDImpl;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.gameobjects.huds.MenuHUDImpl;
import ead.engine.core.gameobjects.huds.TopBasicHUD;
import ead.engine.core.gameobjects.huds.TopBasicHUDImpl;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.InputHandlerImpl;
import ead.engine.core.platform.DesktopFontCache;
import ead.engine.core.platform.DesktopGUI;
import ead.engine.core.platform.DesktopPlatformLauncher;
import ead.engine.core.platform.FontHandlerImpl;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.PlatformLauncher;
import ead.engine.core.platform.rendering.DesktopFilterFactory;
import ead.engine.core.platform.rendering.filters.FilterFactory;

public class DesktopModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<Reader<EAdAdventureModel>>() {})
                .to(EAdAdventureDOMModelReader.class);
		bind(GameLoop.class).to(GameLoopImpl.class);
		bind(GameProfiler.class).to(GameProfilerImpl.class);
		configureGUI();
		bind(PlatformLauncher.class).to(DesktopPlatformLauncher.class);
		bind(InputHandler.class).to(InputHandlerImpl.class);
		bind(GameObjectManager.class).to(GameObjectManagerImpl.class);
		bind(TopBasicHUD.class).to(TopBasicHUDImpl.class);
		bind(BottomBasicHUD.class).to(BottomBasicHUDImpl.class);
		bind(InventoryHUD.class).to(InventoryHUDImpl.class);
		bind(ActionsHUD.class).to(ActionsHUDImpl.class);
		bind(MenuHUD.class).to(MenuHUDImpl.class);
		bind(FontHandlerImpl.class).to(DesktopFontCache.class);
		bind(new TypeLiteral<FilterFactory<Graphics2D>>(){}).to(DesktopFilterFactory.class);
	}

	protected void configureGUI() {
		bind(GUI.class).to(DesktopGUI.class);
	}

	@Provides
	@Named("threaded")
	public boolean provideThreaded() {
		return Boolean.FALSE;
	}

}

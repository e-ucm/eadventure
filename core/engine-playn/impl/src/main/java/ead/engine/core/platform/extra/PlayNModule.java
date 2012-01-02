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

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ead.common.ReflectionProvider;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameProfiler;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.GameObjectManagerImpl;
import ead.engine.core.gameobjects.huds.ActionsHUD;
import ead.engine.core.gameobjects.huds.ActionsHUDImpl;
import ead.engine.core.gameobjects.huds.BasicHUD;
import ead.engine.core.gameobjects.huds.BasicHUDImpl;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.gameobjects.huds.MenuHUDImpl;
import ead.engine.core.gameobjects.transitions.PlayNSimpleTransitionGO;
import ead.engine.core.gameobjects.transitions.SimpleTransitionGO;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.InputHandlerImpl;
import ead.engine.core.platform.AbstractEngineConfiguration;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.FontHandlerImpl;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.PlatformLauncher;
import ead.engine.core.platform.PlayNFontCache;
import ead.engine.core.platform.PlayNGUI;
import ead.engine.core.platform.PlayNGameLoop;
import ead.engine.core.platform.PlayNGameProfiler;
import ead.engine.core.platform.PlayNPlatformLauncher;
import ead.engine.core.platform.PlayNReflectionProvider;
import ead.engine.core.platform.PlayNTransitionFactory;
import ead.engine.core.platform.TransitionFactory;

public class PlayNModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(GameLoop.class).to(PlayNGameLoop.class);
		
		bind(ReflectionProvider.class).to(PlayNReflectionProvider.class).in(Singleton.class);
		bind(GameProfiler.class).to(PlayNGameProfiler.class).in(Singleton.class);
		bind(GUI.class).to(PlayNGUI.class).in(Singleton.class);
		bind(EngineConfiguration.class)
				.to(AbstractEngineConfiguration.class).in(Singleton.class);
		bind(PlatformLauncher.class).to(PlayNPlatformLauncher.class).in(Singleton.class);
		bind(InputHandler.class).to(InputHandlerImpl.class).in(Singleton.class);
		bind(GameObjectManager.class).to(GameObjectManagerImpl.class).in(Singleton.class);
		bind(BasicHUD.class).to(BasicHUDImpl.class).in(Singleton.class);
		bind(ActionsHUD.class).to(ActionsHUDImpl.class).in(Singleton.class);
		bind(MenuHUD.class).to(MenuHUDImpl.class).in(Singleton.class);
		bind(FontHandlerImpl.class).to(PlayNFontCache.class).in(Singleton.class);
		bind(TransitionFactory.class).to(PlayNTransitionFactory.class).in(Singleton.class);
		bind(SimpleTransitionGO.class).to(PlayNSimpleTransitionGO.class);
	}

	@Provides
	@Named("threaded")
	public boolean provideThreaded() {
		return Boolean.FALSE;
	}

}

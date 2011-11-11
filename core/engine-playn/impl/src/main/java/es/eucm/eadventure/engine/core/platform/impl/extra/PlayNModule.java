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

package es.eucm.eadventure.engine.core.platform.impl.extra;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameProfiler;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.MenuHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.GameObjectManagerImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.PlayNSimpleTransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.SimpleTransitionGO;
import es.eucm.eadventure.engine.core.impl.KeyboardStateImpl;
import es.eucm.eadventure.engine.core.impl.MouseStateImpl;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.TransitionFactory;
import es.eucm.eadventure.engine.core.platform.impl.FontHandlerImpl;
import es.eucm.eadventure.engine.core.platform.impl.PlayNFontCache;
import es.eucm.eadventure.engine.core.platform.impl.PlayNGUI;
import es.eucm.eadventure.engine.core.platform.impl.PlayNGameLoop;
import es.eucm.eadventure.engine.core.platform.impl.PlayNGameProfiler;
import es.eucm.eadventure.engine.core.platform.impl.PlayNPlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.impl.PlayNPlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.PlayNReflectionProvider;
import es.eucm.eadventure.engine.core.platform.impl.PlayNTransitionFactory;

public class PlayNModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(GameLoop.class).to(PlayNGameLoop.class);
		
		bind(ReflectionProvider.class).to(PlayNReflectionProvider.class).in(Singleton.class);
		bind(GameProfiler.class).to(PlayNGameProfiler.class).in(Singleton.class);
		bind(GUI.class).to(PlayNGUI.class).in(Singleton.class);
		bind(PlatformConfiguration.class)
				.to(PlayNPlatformConfiguration.class).in(Singleton.class);
		bind(PlatformLauncher.class).to(PlayNPlatformLauncher.class).in(Singleton.class);
		bind(MouseState.class).to(MouseStateImpl.class).in(Singleton.class);
		bind(KeyboardState.class).to(KeyboardStateImpl.class).in(Singleton.class);
		bind(GameObjectManager.class).to(GameObjectManagerImpl.class).in(Singleton.class);
		bind(BasicHUD.class).to(BasicHUDImpl.class).in(Singleton.class);
		bind(ActionsHUD.class).to(PlayNActionsHUDImpl.class).in(Singleton.class);
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

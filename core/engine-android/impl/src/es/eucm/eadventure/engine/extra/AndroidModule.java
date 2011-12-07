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

package es.eucm.eadventure.engine.extra;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import es.eucm.eadventure.engine.AndroidFontCache;
import es.eucm.eadventure.engine.AndroidGUI;
import es.eucm.eadventure.engine.AndroidPlatformConfiguration;
import es.eucm.eadventure.engine.AndroidTransitionFactory;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameProfiler;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.InventoryHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.InventoryHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.MenuHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.GameObjectManagerImpl;
import es.eucm.eadventure.engine.core.impl.GameProfilerImpl;
import es.eucm.eadventure.engine.core.impl.KeyboardStateImpl;
import es.eucm.eadventure.engine.core.impl.MouseStateImpl;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.TransitionFactory;
import es.eucm.eadventure.engine.core.platform.impl.FontHandlerImpl;
import es.eucm.eadventure.engine.gameobjects.AndroidBasicHUD;

public class AndroidModule extends AbstractModule {

	public AndroidModule() {
		
	}
	
	@Override
	protected void configure() {
		bind(GameLoop.class).to(AndroidGameLoopImpl.class);
		bind(GameProfiler.class).to(GameProfilerImpl.class);
		bind(GUI.class).to(AndroidGUI.class);
		bind(PlatformConfiguration.class).to(AndroidPlatformConfiguration.class);
		bind(AndroidPlatformConfiguration.class);
		bind(MouseState.class).to(MouseStateImpl.class);
		bind(KeyboardState.class).to(KeyboardStateImpl.class);
		bind(GameObjectManager.class).to(GameObjectManagerImpl.class);
		bind(ActionsHUD.class).to(AndroidActionsHUDImpl.class);
		bind(BasicHUD.class).to(AndroidBasicHUD.class);
		bind(InventoryHUD.class).to(InventoryHUDImpl.class);
		bind(FontHandlerImpl.class).to(AndroidFontCache.class);
		bind(MenuHUD.class).to(MenuHUDImpl.class);
		bind(TransitionFactory.class).to(AndroidTransitionFactory.class);
	}
	
	@Provides
	@Named("threaded")
	public boolean provideThreaded() {
		return Boolean.TRUE;
	}

}

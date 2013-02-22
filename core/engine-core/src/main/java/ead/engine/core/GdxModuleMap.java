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

package ead.engine.core;

import com.badlogic.gdx.ApplicationListener;

import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.AssetHandlerImpl;
import ead.engine.core.assets.fonts.FontHandler;
import ead.engine.core.assets.fonts.FontHandlerImpl;
import ead.engine.core.canvas.filters.FilterFactory;
import ead.engine.core.canvas.filters.GdxFilterFactory;
import ead.engine.core.factories.EffectGOFactory;
import ead.engine.core.factories.EffectGOFactoryImpl;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.EventGOFactoryImpl;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.factories.SceneElementGOFactoryImpl;
import ead.engine.core.factories.TrajectoryFactory;
import ead.engine.core.factories.TrajectoryFactoryImpl;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameStateImpl;
import ead.engine.core.game.PluginHandlerImpl;
import ead.engine.core.game.SoundManagerImpl;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.game.interfaces.PluginHandler;
import ead.engine.core.game.interfaces.SoundManager;
import ead.engine.core.gameobjects.debuggers.DebuggersHandler;
import ead.engine.core.gameobjects.debuggers.DebuggersHandlerImpl;
import ead.engine.core.gameobjects.sceneelements.transitions.sceneloaders.DefaultSceneLoader;
import ead.engine.core.gameobjects.sceneelements.transitions.sceneloaders.SceneLoader;
import ead.engine.core.tracking.DefaultGameTracker;
import ead.engine.core.tracking.GameTracker;
import ead.engine.core.tracking.selection.DefaultTrackerSelector;
import ead.engine.core.tracking.selection.TrackerSelector;
import ead.tools.BasicSceneGraph;
import ead.tools.ModuleMap;
import ead.tools.SceneGraph;

public class GdxModuleMap extends ModuleMap {

	public GdxModuleMap() {

		// Factories
		binds.put(TrajectoryFactory.class, TrajectoryFactoryImpl.class);
		binds.put(SceneElementGOFactory.class, SceneElementGOFactoryImpl.class);
		binds.put(EffectGOFactory.class, EffectGOFactoryImpl.class);
		binds.put(EventGOFactory.class, EventGOFactoryImpl.class);

		binds.put(AssetHandler.class, AssetHandlerImpl.class);
		binds.put(FontHandler.class, FontHandlerImpl.class);
		binds.put(DebuggersHandler.class, DebuggersHandlerImpl.class);

		binds.put(PluginHandler.class, PluginHandlerImpl.class);

		binds.put(SoundManager.class, SoundManagerImpl.class);

		binds.put(FilterFactory.class, GdxFilterFactory.class);

		// Game
		binds.put(GameState.class, GameStateImpl.class);
		binds.put(Game.class, GameImpl.class);

		// Tracking
		binds.put(GameTracker.class, DefaultGameTracker.class);
		binds.put(TrackerSelector.class, DefaultTrackerSelector.class);

		binds.put(SceneLoader.class, DefaultSceneLoader.class);

		binds.put(SceneGraph.class, BasicSceneGraph.class);

		binds.put(ApplicationListener.class, EAdEngine.class);

	}

}

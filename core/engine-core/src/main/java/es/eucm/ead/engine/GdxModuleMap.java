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

package es.eucm.ead.engine;

import com.badlogic.gdx.ApplicationListener;

import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.AssetHandlerImpl;
import es.eucm.ead.engine.assets.fonts.FontHandler;
import es.eucm.ead.engine.assets.fonts.FontHandlerImpl;
import es.eucm.ead.engine.canvas.filters.FilterFactory;
import es.eucm.ead.engine.canvas.filters.GdxFilterFactory;
import es.eucm.ead.engine.factories.EffectGOFactory;
import es.eucm.ead.engine.factories.EffectGOFactoryImpl;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.EventGOFactoryImpl;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactoryImpl;
import es.eucm.ead.engine.factories.TrajectoryFactory;
import es.eucm.ead.engine.factories.TrajectoryFactoryImpl;
import es.eucm.ead.engine.game.GameImpl;
import es.eucm.ead.engine.game.GameStateImpl;
import es.eucm.ead.engine.game.PluginHandlerImpl;
import es.eucm.ead.engine.game.SoundManagerImpl;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.engine.game.interfaces.PluginHandler;
import es.eucm.ead.engine.game.interfaces.SoundManager;
import es.eucm.ead.engine.gameobjects.sceneelements.transitions.sceneloaders.DefaultSceneLoader;
import es.eucm.ead.engine.gameobjects.sceneelements.transitions.sceneloaders.SceneLoader;
import es.eucm.ead.engine.tracking.DefaultGameTracker;
import es.eucm.ead.engine.tracking.GameTracker;
import es.eucm.ead.engine.tracking.selection.DefaultTrackerSelector;
import es.eucm.ead.engine.tracking.selection.TrackerSelector;
import es.eucm.ead.engine.utils.BasicSceneGraph;
import es.eucm.ead.tools.ModuleMap;
import es.eucm.ead.engine.utils.SceneGraph;

public class GdxModuleMap extends ModuleMap {

	public GdxModuleMap() {

		// Factories
		binds.put(TrajectoryFactory.class, TrajectoryFactoryImpl.class);
		binds.put(SceneElementGOFactory.class, SceneElementGOFactoryImpl.class);
		binds.put(EffectGOFactory.class, EffectGOFactoryImpl.class);
		binds.put(EventGOFactory.class, EventGOFactoryImpl.class);

		binds.put(AssetHandler.class, AssetHandlerImpl.class);
		binds.put(FontHandler.class, FontHandlerImpl.class);

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

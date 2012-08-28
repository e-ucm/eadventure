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

package ead.engine.core.gdx.platform;

import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.debuggers.DebuggerHandlerImpl;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.evaluators.EvaluatorFactoryImpl;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameController;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameLoader;
import ead.engine.core.game.GameLoaderImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.game.GameStateImpl;
import ead.engine.core.game.ValueMap;
import ead.engine.core.game.VariableMap;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.GameObjectManagerImpl;
import ead.engine.core.gameobjects.factories.EffectGOFactory;
import ead.engine.core.gameobjects.factories.EffectGOFactoryImpl;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.EventGOFactoryImpl;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactoryImpl;
import ead.engine.core.gameobjects.factories.TransitionFactoryImpl;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.gameobjects.huds.ActionsHUD;
import ead.engine.core.gameobjects.huds.ActionsHUDImpl;
import ead.engine.core.gameobjects.huds.BottomBasicHUD;
import ead.engine.core.gameobjects.huds.BottomBasicHUDImpl;
import ead.engine.core.gameobjects.huds.EffectHUD;
import ead.engine.core.gameobjects.huds.EffectHUDImpl;
import ead.engine.core.gameobjects.huds.InventoryHUD;
import ead.engine.core.gameobjects.huds.InventoryHUDImpl;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.gameobjects.huds.MenuHUDImpl;
import ead.engine.core.gameobjects.huds.TopBasicHUD;
import ead.engine.core.gameobjects.huds.TopBasicHUDImpl;
import ead.engine.core.gameobjects.transitions.sceneloaders.GraphSceneLoader;
import ead.engine.core.gdx.GdxEngine;
import ead.engine.core.gdx.GdxEngineImpl;
import ead.engine.core.gdx.assets.GdxAssetHandler;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.InputHandlerImpl;
import ead.engine.core.inventory.InventoryHandler;
import ead.engine.core.inventory.InventoryHandlerImpl;
import ead.engine.core.operators.OperatorFactory;
import ead.engine.core.operators.OperatorFactoryImpl;
import ead.engine.core.platform.AbstractEngineConfiguration;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.FontHandlerImpl;
import ead.engine.core.platform.TransitionFactory;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.plugins.PluginHandler;
import ead.engine.core.tracking.DefaultGameTracker;
import ead.engine.core.tracking.GameTracker;
import ead.engine.core.tracking.selection.DefaultTrackerSelector;
import ead.engine.core.tracking.selection.TrackerSelector;
import ead.engine.core.trajectories.TrajectoryFactory;
import ead.engine.core.trajectories.TrajectoryFactoryImpl;
import ead.tools.BasicSceneGraph;
import ead.tools.ModuleMap;
import ead.tools.SceneGraph;

public class GdxModuleMap extends ModuleMap {

	public GdxModuleMap() {

		// Factories
		binds.put(EvaluatorFactory.class, EvaluatorFactoryImpl.class);
		binds.put(OperatorFactory.class, OperatorFactoryImpl.class);
		binds.put(TrajectoryFactory.class, TrajectoryFactoryImpl.class);
		binds.put(SceneElementGOFactory.class, SceneElementGOFactoryImpl.class);
		binds.put(EffectGOFactory.class, EffectGOFactoryImpl.class);
		binds.put(EventGOFactory.class, EventGOFactoryImpl.class);

		binds.put(AssetHandler.class, GdxAssetHandler.class);
		binds.put(FontHandler.class, FontHandlerImpl.class);

		binds.put(GenericCanvas.class, GdxCanvas.class);

		binds.put(ValueMap.class, VariableMap.class);

		binds.put(GameObjectManager.class, GameObjectManagerImpl.class);

		binds.put(EngineConfiguration.class, AbstractEngineConfiguration.class);

		binds.put(DebuggerHandler.class, DebuggerHandlerImpl.class);

		binds.put(InventoryHandler.class, InventoryHandlerImpl.class);
		binds.put(TransitionFactory.class, TransitionFactoryImpl.class);

		binds.put(GameObjectManager.class, GameObjectManagerImpl.class);

		binds.put(InputHandler.class, InputHandlerImpl.class);

		binds.put(PluginHandler.class, GdxPluginHandler.class);

		// Game
		binds.put(GameState.class, GameStateImpl.class);
		binds.put(GameController.class, GdxGameController.class);
		binds.put(Game.class, GameImpl.class);

		// Tracking
		binds.put(GameTracker.class, DefaultGameTracker.class);
		binds.put(TrackerSelector.class, DefaultTrackerSelector.class);

		// HUDs
		binds.put(EffectHUD.class, EffectHUDImpl.class);
		binds.put(TopBasicHUD.class, TopBasicHUDImpl.class);
		binds.put(BottomBasicHUD.class, BottomBasicHUDImpl.class);
		binds.put(InventoryHUD.class, InventoryHUDImpl.class);
		binds.put(ActionsHUD.class, ActionsHUDImpl.class);
		binds.put(MenuHUD.class, MenuHUDImpl.class);

		binds.put(SceneLoader.class, GraphSceneLoader.class);

		binds.put(SceneGraph.class, BasicSceneGraph.class);

		binds.put(GameLoader.class, GameLoaderImpl.class);
		binds.put(GdxEngine.class, GdxEngineImpl.class);

	}

}

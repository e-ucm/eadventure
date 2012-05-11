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

package ead.engine.core.platform.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.scene.EAdScene;
import ead.common.util.ReflectionProvider;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.debuggers.DebuggerHandlerImpl;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.evaluators.EvaluatorFactoryImpl;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameController;
import ead.engine.core.game.GameControllerImpl;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameLoopImpl;
import ead.engine.core.game.GameProfiler;
import ead.engine.core.game.GameProfilerImpl;
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
import ead.engine.core.gameobjects.huds.EffectHUD;
import ead.engine.core.gameobjects.huds.EffectHUDImpl;
import ead.engine.core.gameobjects.transitions.sceneloaders.DefaultSceneLoader;
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
import ead.engine.core.platform.GenericInjector;
import ead.engine.core.platform.JavaInjector;
import ead.engine.core.platform.JavaPluginHandler;
import ead.engine.core.platform.JavaReflectionProvider;
import ead.engine.core.platform.LoadingScreen;
import ead.engine.core.platform.TransitionFactory;
import ead.engine.core.plugins.PluginHandler;
import ead.engine.core.tracking.GLASGameTracker;
import ead.engine.core.tracking.GameTracker;
import ead.engine.core.tracking.selection.DefaultTrackerSelector;
import ead.engine.core.tracking.selection.TrackerSelector;
import ead.engine.core.trajectories.TrajectoryFactory;
import ead.engine.core.trajectories.TrajectoryFactoryImpl;
import es.eucm.glas.tracker.GLASTracker;
import es.eucm.glas.tracker.JerseyTracker;

public class BasicGameModule extends AbstractModule {

	@Override
	protected void configure() {
		installFactories();
		bind(ValueMap.class).to(VariableMap.class);
		bind(EngineConfiguration.class).to(AbstractEngineConfiguration.class);
		
		bind(GameState.class).to(GameStateImpl.class);
		bind(GameController.class).to(GameControllerImpl.class);
		bind(Game.class).to(GameImpl.class);
		bind(GameLoop.class).to(GameLoopImpl.class);
		bind(GameProfiler.class).to(GameProfilerImpl.class);
		
		bind(InputHandler.class).to(InputHandlerImpl.class);
		bind(GameObjectManager.class).to(GameObjectManagerImpl.class);
		
		bind(EffectHUD.class).to(EffectHUDImpl.class);
		bind(FontHandler.class).to(FontHandlerImpl.class);
		bind(DebuggerHandler.class).to(DebuggerHandlerImpl.class);
		bind(PluginHandler.class).to(JavaPluginHandler.class);
		bind(GenericInjector.class).to(JavaInjector.class);
		bind(InventoryHandler.class).to(InventoryHandlerImpl.class);
		bind(TransitionFactory.class).to(TransitionFactoryImpl.class);

		bind(ReflectionProvider.class).to(JavaReflectionProvider.class);
		bind(SceneLoader.class).to(DefaultSceneLoader.class);

		bind(EAdAdventureModel.class).to(BasicAdventureModel.class);
		bind(EAdScene.class).annotatedWith(Names.named("LoadingScreen"))
				.to(LoadingScreen.class).asEagerSingleton();
		
		// Tracking
//		bind(GameTracker.class).to(DefaultGameTracker.class);
		bind(TrackerSelector.class).to(DefaultTrackerSelector.class);
		bind(GameTracker.class).to(GLASGameTracker.class);
		bind(GLASTracker.class).to(JerseyTracker.class);

	}

	private void installFactories() {
		bind(EvaluatorFactory.class).to(EvaluatorFactoryImpl.class);
		bind(OperatorFactory.class).to(OperatorFactoryImpl.class);
		bind(TrajectoryFactory.class).to(TrajectoryFactoryImpl.class);
		bind(SceneElementGOFactory.class).to(SceneElementGOFactoryImpl.class);
		bind(EffectGOFactory.class).to(EffectGOFactoryImpl.class);
		bind(EventGOFactory.class).to(EventGOFactoryImpl.class);
	}

	@Provides
	@Named("classParam")
	public String provideThreaded() {
		return "class";
	}

}

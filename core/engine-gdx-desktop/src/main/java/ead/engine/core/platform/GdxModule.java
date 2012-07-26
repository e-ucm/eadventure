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

package ead.engine.core.platform;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ead.common.model.elements.scene.EAdScene;
import ead.common.resources.assets.multimedia.EAdVideo;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.debuggers.DebuggerHandlerImpl;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.evaluators.EvaluatorFactoryImpl;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameController;
import ead.engine.core.game.GameImpl;
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
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.GdxCanvas;
import ead.engine.core.platform.GdxGameController;
import ead.engine.core.platform.LoadingScreen;
import ead.engine.core.platform.TransitionFactory;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.SpecialAssetRenderer;
import ead.engine.core.platform.assets.VLCDesktopVideoRenderer;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.plugins.PluginHandler;
import ead.engine.core.tracking.DefaultGameTracker;
import ead.engine.core.tracking.GameTracker;
import ead.engine.core.tracking.selection.DefaultTrackerSelector;
import ead.engine.core.tracking.selection.TrackerSelector;
import ead.engine.core.trajectories.TrajectoryFactory;
import ead.engine.core.trajectories.TrajectoryFactoryImpl;

public class GdxModule extends AbstractModule {

	@Override
	protected void configure() {

		installFactories();

		bind(AssetHandler.class).to(GdxDesktopAssetHandler.class);
		bind(FontHandler.class).to(FontHandlerImpl.class);

		bind(GenericCanvas.class).to(GdxCanvas.class);

		bind(ValueMap.class).to(VariableMap.class);

		bind(EngineConfiguration.class).to(AbstractEngineConfiguration.class);

		bind(DebuggerHandler.class).to(DebuggerHandlerImpl.class);

		bind(InventoryHandler.class).to(InventoryHandlerImpl.class);
		bind(TransitionFactory.class).to(TransitionFactoryImpl.class);

		bind(GameObjectManager.class).to(GameObjectManagerImpl.class);

		bind(GUI.class).to(GdxDesktopGUI.class);
		bind(InputHandler.class).to(InputHandlerImpl.class);

		bind(PluginHandler.class).to(GdxPluginHandler.class);

		// Game
		bind(GameState.class).to(GameStateImpl.class);
		bind(GameController.class).to(GdxGameController.class);
		bind(Game.class).to(GameImpl.class);

		// Tracking
		bind(GameTracker.class).to(DefaultGameTracker.class);
		bind(TrackerSelector.class).to(DefaultTrackerSelector.class);

		// HUDs
		bind(EffectHUD.class).to(EffectHUDImpl.class);
		bind(TopBasicHUD.class).to(TopBasicHUDImpl.class);
		bind(BottomBasicHUD.class).to(BottomBasicHUDImpl.class);
		bind(InventoryHUD.class).to(InventoryHUDImpl.class);
		bind(ActionsHUD.class).to(ActionsHUDImpl.class);
		bind(MenuHUD.class).to(MenuHUDImpl.class);

		bind(SceneLoader.class).to(DefaultSceneLoader.class);

		bind(EAdScene.class).annotatedWith(Names.named("LoadingScreen"))
				.to(LoadingScreen.class).asEagerSingleton();

		bind(new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
		}).to(VLCDesktopVideoRenderer.class);
	}

	private void installFactories() {
		bind(EvaluatorFactory.class).to(EvaluatorFactoryImpl.class);
		bind(OperatorFactory.class).to(OperatorFactoryImpl.class);
		bind(TrajectoryFactory.class).to(TrajectoryFactoryImpl.class);
		bind(SceneElementGOFactory.class).to(SceneElementGOFactoryImpl.class);
		bind(EffectGOFactory.class).to(EffectGOFactoryImpl.class);
		bind(EventGOFactory.class).to(EventGOFactoryImpl.class);
	}

}

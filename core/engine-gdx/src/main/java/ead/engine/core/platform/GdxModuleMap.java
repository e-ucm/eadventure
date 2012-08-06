package ead.engine.core.platform;

import java.util.HashMap;
import java.util.Map;

import ead.engine.core.assets.GdxAssetHandler;
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
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.plugins.PluginHandler;
import ead.engine.core.tracking.DefaultGameTracker;
import ead.engine.core.tracking.GameTracker;
import ead.engine.core.tracking.selection.DefaultTrackerSelector;
import ead.engine.core.tracking.selection.TrackerSelector;
import ead.engine.core.trajectories.TrajectoryFactory;
import ead.engine.core.trajectories.TrajectoryFactoryImpl;

public class GdxModuleMap {
	
	private Map<Class<?>, Class<?>> binds;
	
	public GdxModuleMap( ){
		binds = new HashMap<Class<?>, Class<?>>();
		
		// Factories
		binds.put(EvaluatorFactory.class,EvaluatorFactoryImpl.class);
		binds.put(OperatorFactory.class,OperatorFactoryImpl.class);
		binds.put(TrajectoryFactory.class,TrajectoryFactoryImpl.class);
		binds.put(SceneElementGOFactory.class,SceneElementGOFactoryImpl.class);
		binds.put(EffectGOFactory.class,EffectGOFactoryImpl.class);
		binds.put(EventGOFactory.class,EventGOFactoryImpl.class);
		
		binds.put(AssetHandler.class,GdxAssetHandler.class);
		binds.put(FontHandler.class,FontHandlerImpl.class);
		
		binds.put(GenericCanvas.class,GdxCanvas.class);
		
		binds.put(ValueMap.class,VariableMap.class);
		
		binds.put(GameObjectManager.class,GameObjectManagerImpl.class);
		
		
		binds.put(EngineConfiguration.class,AbstractEngineConfiguration.class);
		
		binds.put(DebuggerHandler.class,DebuggerHandlerImpl.class);
		
		binds.put(InventoryHandler.class,InventoryHandlerImpl.class);
		binds.put(TransitionFactory.class,TransitionFactoryImpl.class);
		
		binds.put(GameObjectManager.class,GameObjectManagerImpl.class);
		
		binds.put(InputHandler.class,InputHandlerImpl.class);
		
		
		binds.put(PluginHandler.class,GdxPluginHandler.class);
		
		//Game
		binds.put(GameState.class,GameStateImpl.class);
		binds.put(GameController.class,GdxGameController.class);
		binds.put(Game.class,GameImpl.class);
		
		// Tracking
		binds.put(GameTracker.class,DefaultGameTracker.class);
		binds.put(TrackerSelector.class,DefaultTrackerSelector.class);
		
		// HUDs
		binds.put(EffectHUD.class,EffectHUDImpl.class);
		binds.put(TopBasicHUD.class,TopBasicHUDImpl.class);
		binds.put(BottomBasicHUD.class,BottomBasicHUDImpl.class);
		binds.put(InventoryHUD.class,InventoryHUDImpl.class);
		binds.put(ActionsHUD.class,ActionsHUDImpl.class);
		binds.put(MenuHUD.class,MenuHUDImpl.class);
		
		binds.put(SceneLoader.class,DefaultSceneLoader.class);
		
	}
	
	public Map<Class<?>, Class<?>> getBinds( ){
		return binds;
	}

}

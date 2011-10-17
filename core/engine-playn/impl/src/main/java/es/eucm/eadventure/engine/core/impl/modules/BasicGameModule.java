package es.eucm.eadventure.engine.core.impl.modules;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameController;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.debuggers.impl.EAdMainDebugger;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.evaluators.impl.EvaluatorFactoryImpl;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.EffectHUDImpl;
import es.eucm.eadventure.engine.core.impl.GameControllerImpl;
import es.eucm.eadventure.engine.core.impl.GameImpl;
import es.eucm.eadventure.engine.core.impl.GameStateImpl;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.VariableMap;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.operators.impl.OperatorFactoryImpl;
import es.eucm.eadventure.engine.core.platform.FontHandler;
import es.eucm.eadventure.engine.core.platform.impl.FontHandlerImpl;

public class BasicGameModule extends AbstractGinModule {

	@Override
	protected void configure() {
		install(new GameObjectFactoryModule());
		bind(EvaluatorFactory.class).to(EvaluatorFactoryImpl.class).in(Singleton.class);
		bind(OperatorFactory.class).to(OperatorFactoryImpl.class).in(Singleton.class);
		install(new TrajectoryFactoryModule());
		
		bind(ValueMap.class).to(VariableMap.class).in(Singleton.class);
		bind(GameState.class).to(GameStateImpl.class).in(Singleton.class);
		bind(GameController.class).to(GameControllerImpl.class).in(Singleton.class);
		bind(Game.class).to(GameImpl.class).in(Singleton.class);
		bind(EffectHUD.class).to(EffectHUDImpl.class).in(Singleton.class);
		bind(FontHandler.class).to(FontHandlerImpl.class).in(Singleton.class);
		bind(EAdDebugger.class).to(EAdMainDebugger.class).in(Singleton.class);

		bind(EAdAdventureModel.class).to(EAdAdventureModelImpl.class);
		bind(EAdScene.class).annotatedWith(Names.named("LoadingScreen")).to(
				LoadingScreen.class).in(Singleton.class);

	}
	
	@Provides
	@Named("classParam")
	public String provideThreaded() {
		return "class";
	}

}

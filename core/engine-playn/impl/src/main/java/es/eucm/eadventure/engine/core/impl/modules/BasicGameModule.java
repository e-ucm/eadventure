package es.eucm.eadventure.engine.core.impl.modules;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameController;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.debuggers.impl.EAdMainDebugger;
import es.eucm.eadventure.engine.core.evaluators.Evaluator;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.EffectHUDImpl;
import es.eucm.eadventure.engine.core.impl.GameControllerImpl;
import es.eucm.eadventure.engine.core.impl.GameImpl;
import es.eucm.eadventure.engine.core.impl.GameStateImpl;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.VariableMap;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.EvaluatorFactoryMapProvider;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.OperatorFactoryMapProvider;
import es.eucm.eadventure.engine.core.operator.Operator;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.platform.FontCache;
import es.eucm.eadventure.engine.core.platform.impl.FontCacheImpl;
import es.eucm.eadventure.engine.core.platform.impl.extra.EvaluatorFactoryProvider;
import es.eucm.eadventure.engine.core.platform.impl.extra.OperatorFactoryProvider;

public class BasicGameModule extends AbstractGinModule {

	@Override
	protected void configure() {
		install(new GameObjectFactoryModule());

		bind(new TypeLiteral<MapProvider<Class<?>, Evaluator<?>>>() {}).to(EvaluatorFactoryMapProvider.class);
		bind(EvaluatorFactory.class).toProvider(EvaluatorFactoryProvider.class).in(Singleton.class);

		bind(new TypeLiteral<MapProvider<Class<?>, Operator<?>>>() {}).to(OperatorFactoryMapProvider.class);
		bind(OperatorFactory.class).toProvider(OperatorFactoryProvider.class).in(Singleton.class);
		install(new TrajectoryFactoryModule());
		
		bind(ValueMap.class).to(VariableMap.class).in(Singleton.class);
		bind(GameState.class).to(GameStateImpl.class).in(Singleton.class);
		bind(GameController.class).to(GameControllerImpl.class).in(Singleton.class);
		bind(Game.class).to(GameImpl.class).in(Singleton.class);
		bind(EffectHUD.class).to(EffectHUDImpl.class).in(Singleton.class);
		bind(FontCache.class).to(FontCacheImpl.class).in(Singleton.class);
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

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

package ead.engine.core.gdx.html.platform;

import javax.inject.Singleton;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.name.Names;

import ead.common.model.elements.scenes.EAdScene;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.debuggers.DebuggerHandlerImpl;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.evaluators.EvaluatorFactoryImpl;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.game.GameStateImpl;
import ead.engine.core.game.ValueMap;
import ead.engine.core.game.VariableMap;
import ead.engine.core.gameobjects.factories.EffectGOFactory;
import ead.engine.core.gameobjects.factories.EffectGOFactoryImpl;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.EventGOFactoryImpl;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactoryImpl;
import ead.engine.core.gameobjects.factories.TransitionFactoryImpl;
import ead.engine.core.gdx.html.tools.GwtInjector;
import ead.engine.core.gdx.platform.GdxPluginHandler;
import ead.engine.core.inventory.InventoryHandler;
import ead.engine.core.inventory.InventoryHandlerImpl;
import ead.engine.core.operators.OperatorFactory;
import ead.engine.core.operators.OperatorFactoryImpl;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.FontHandlerImpl;
import ead.engine.core.platform.LoadingScreen;
import ead.engine.core.platform.TransitionFactory;
import ead.engine.core.plugins.PluginHandler;
import ead.engine.core.tracking.DefaultGameTracker;
import ead.engine.core.tracking.GameTracker;
import ead.engine.core.tracking.selection.DefaultTrackerSelector;
import ead.engine.core.tracking.selection.TrackerSelector;
import ead.engine.core.trajectories.TrajectoryFactory;
import ead.engine.core.trajectories.TrajectoryFactoryImpl;
import ead.tools.GenericInjector;

public class GWTBasicGameModule extends AbstractGinModule {

	@Override
	protected void configure() {

		installFactories();

		bind(ValueMap.class).to(VariableMap.class).in(Singleton.class);
		bind(GameState.class).to(GameStateImpl.class).in(Singleton.class);

		bind(Game.class).to(GameImpl.class).in(Singleton.class);
		bind(FontHandler.class).to(FontHandlerImpl.class).in(Singleton.class);
		bind(DebuggerHandler.class).to(DebuggerHandlerImpl.class).in(
				Singleton.class);
		bind(PluginHandler.class).to(GdxPluginHandler.class)
				.in(Singleton.class);
		bind(GenericInjector.class).to(GwtInjector.class).in(Singleton.class);
		bind(InventoryHandler.class).to(InventoryHandlerImpl.class).in(
				Singleton.class);

		bind(EAdScene.class).annotatedWith(Names.named("LoadingScreen")).to(
				LoadingScreen.class).in(Singleton.class);
		bind(TransitionFactory.class).to(TransitionFactoryImpl.class).in(
				Singleton.class);

		// bind(new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
		// }).to(PlayNVideoRenderer.class);
		// bind(new TypeLiteral<FilterFactory<Canvas>>() {
		// }).to(PlayNFilterFactory.class);

		// Tracking
		bind(GameTracker.class).to(DefaultGameTracker.class)
				.in(Singleton.class);
		bind(TrackerSelector.class).to(DefaultTrackerSelector.class).in(
				Singleton.class);
		// bind(GLASTracker.class).to(GLASGwtTracker.class).in(Singleton.class);
		// bind(GameTracker.class).to(GLASGameTracker.class).in(Singleton.class);

	}

	private void installFactories() {
		bind(EvaluatorFactory.class).to(EvaluatorFactoryImpl.class).in(
				Singleton.class);
		;
		bind(OperatorFactory.class).to(OperatorFactoryImpl.class).in(
				Singleton.class);
		;
		bind(TrajectoryFactory.class).to(TrajectoryFactoryImpl.class).in(
				Singleton.class);
		;
		bind(SceneElementGOFactory.class).to(SceneElementGOFactoryImpl.class)
				.in(Singleton.class);
		;
		bind(EffectGOFactory.class).to(EffectGOFactoryImpl.class).in(
				Singleton.class);
		;
		bind(EventGOFactory.class).to(EventGOFactoryImpl.class).in(
				Singleton.class);
		;
	}

}

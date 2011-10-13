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

package es.eucm.eadventure.engine.core.impl.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameController;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.debuggers.impl.EAdMainDebugger;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.EffectHUDImpl;
import es.eucm.eadventure.engine.core.impl.GameControllerImpl;
import es.eucm.eadventure.engine.core.impl.GameImpl;
import es.eucm.eadventure.engine.core.impl.GameStateImpl;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.VariableMap;
import es.eucm.eadventure.engine.core.platform.FontHandler;
import es.eucm.eadventure.engine.core.platform.impl.FontHandlerImpl;
import es.eucm.eadventure.engine.core.platform.impl.JavaReflectionProvider;

public class BasicGameModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new GameObjectFactoryModule());
		install(new EvaluatorFactoryModule());
		install(new OperatorFactoryModule());
		install(new TrajectoryFactoryModule());
		
		bind(ValueMap.class).to(VariableMap.class);
		bind(GameState.class).to(GameStateImpl.class);
		bind(GameController.class).to(GameControllerImpl.class);
		bind(Game.class).to(GameImpl.class);
		bind(EffectHUD.class).to(EffectHUDImpl.class);
		bind(FontHandler.class).to(FontHandlerImpl.class);
		bind(EAdDebugger.class).to(EAdMainDebugger.class);
		
		bind(ReflectionProvider.class).to(JavaReflectionProvider.class);

		bind(EAdAdventureModel.class).to(EAdAdventureModelImpl.class);
		bind(EAdScene.class).annotatedWith(Names.named("LoadingScreen")).to(
				LoadingScreen.class).asEagerSingleton();

	}
	
	@Provides
	@Named("classParam")
	public String provideThreaded() {
		return "class";
	}


}

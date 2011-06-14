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
import com.google.inject.name.Names;

import es.eucm.eadventure.common.model.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameController;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.EffectHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.impl.GameControllerImpl;
import es.eucm.eadventure.engine.core.impl.GameImpl;
import es.eucm.eadventure.engine.core.impl.GameStateImpl;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.VariableMap;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.GameObjectFactoryMapProvider;
import es.eucm.eadventure.engine.core.platform.FontCache;
import es.eucm.eadventure.engine.core.platform.impl.FontCacheImpl;

public class BasicGameModule extends AbstractModule {

	@Override
	protected void configure() {
		setLoadingScreen();

		install(new GameObjectFactoryModule());
		install(new EvaluatorFactoryModule());
		install(new OperatorFactoryModule());
		
		bind(ValueMap.class).to(VariableMap.class);
		bind(GameState.class).to(GameStateImpl.class);
		bind(GameController.class).to(GameControllerImpl.class);
		bind(Game.class).to(GameImpl.class);
		bind(EffectHUD.class).to(EffectHUDImpl.class);
		bind(FontCache.class).to(FontCacheImpl.class);

		/** TODO remove from here
		bind(new TypeLiteral<Reader<EAdAdventureModel>>() {
		}).to(EAdAdventureModelReader.class);
		bind(AdventureHandler.class);
		*/
		bind(EAdAdventureModel.class).to(EAdAdventureModelImpl.class);
		bind(String.class).annotatedWith(Names.named("classParam")).toInstance(
				"class");
		
	}
	
	protected void setLoadingScreen()
	{
		bind(EAdScene.class).annotatedWith(Names.named("LoadingScreen")).to(
				LoadingScreen.class);
		GameObjectFactoryMapProvider.add(LoadingScreen.class, SceneGOImpl.class);
	}

	

}

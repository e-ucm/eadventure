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

package es.eucm.eadventure.engine.core.platform;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.model.EAdAdventureModel;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdCancelEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.EAdWaitEffect;
import es.eucm.eadventure.common.model.effects.impl.actorreference.EAdHighlightActorReference;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameController;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.ActorGO;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.EffectHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.ActorGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.ActorReferenceGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.GameObjectFactoryImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.CancelEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeScreenGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeVariableGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.HighlightEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ShowTextEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.WaitEffectGO;
import es.eucm.eadventure.engine.core.impl.GameControllerImpl;
import es.eucm.eadventure.engine.core.impl.GameImpl;
import es.eucm.eadventure.engine.core.impl.GameStateImpl;
import es.eucm.eadventure.engine.core.impl.ValueMapImpl;
import es.eucm.eadventure.engine.core.impl.modules.EvaluatorFactoryModule;
import es.eucm.eadventure.engine.core.impl.modules.OperatorFactoryModule;
import es.eucm.eadventure.engine.core.platform.impl.FontCacheImpl;

public class TestBasicGameModule extends AbstractModule {
	private static HashMap<Class<? extends EAdElement>, Class<? extends GameObject<?>>> tempMap = new HashMap<Class<? extends EAdElement>, Class<? extends GameObject<?>>>();

	@Override
	protected void configure() {
		configureGameObjectFactory();
		install(new EvaluatorFactoryModule());
		install(new OperatorFactoryModule());
		bind(ValueMap.class).to(ValueMapImpl.class);
		bind(GameState.class).to(GameStateImpl.class);
		bind(GameController.class).to(GameControllerImpl.class);
		bind(Game.class).to(GameImpl.class);
		bind(EffectHUD.class).to(EffectHUDImpl.class);
		bind(FontCache.class).to(FontCacheImpl.class);
		bind(EAdScene.class).annotatedWith(Names.named("LoadingScreen")).to(
				EffectOpaqueBlockTestScreen.class);

		bind(EAdAdventureModel.class).to(EAdAdventureModelImpl.class);
		bind(String.class).annotatedWith(Names.named("classParam")).toInstance(
				"class");
		
	}

	protected void configureGameObjectFactory() {
		bind(SceneGO.class).to(SceneGOImpl.class);
		bind(ActorReferenceGO.class).to(ActorReferenceGOImpl.class);
		bind(ActorGO.class).to(ActorGOImpl.class);

		Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> map = new HashMap<Class<? extends EAdElement>, Class<? extends GameObject<?>>>();

		bind(
				new TypeLiteral<Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>>>() {
				}).toInstance(map);

		map.put(EffectOpaqueBlockTestScreen.class, SceneGOImpl.class);
		map.put(EAdScene.class, SceneGOImpl.class);
		map.put(EAdSceneImpl.class, SceneGOImpl.class);
		map.put(EAdActorReference.class, ActorReferenceGO.class);
		map.put(EAdActorReferenceImpl.class, ActorReferenceGO.class);
		map.put(EAdActor.class, ActorGO.class);
		map.put(EAdBasicActor.class, ActorGO.class);

		map.put(EAdShowText.class, ShowTextEffectGO.class);
		map.put(EAdChangeScene.class, ChangeScreenGO.class);
		map.put(EAdMoveSceneElement.class, MoveSceneElementGO.class);
		map.put(EAdHighlightActorReference.class, HighlightEffectGO.class);
		map.put(EAdWaitEffect.class, WaitEffectGO.class);
		map.put(EAdCancelEffect.class, CancelEffectGO.class);
		map.put(EAdChangeVarValueEffect.class, ChangeVariableGO.class);

		for (Class<? extends EAdElement> key_value : tempMap.keySet())
			map.put(key_value, tempMap.get(key_value));

		bind(GameObjectFactory.class).to(GameObjectFactoryImpl.class);

	}

	public static void addToGameObjectFactory(
			Class<? extends EAdElement> key_value,
			Class<? extends GameObject<?>> value_value) {
		tempMap.put(key_value, value_value);
	}

}

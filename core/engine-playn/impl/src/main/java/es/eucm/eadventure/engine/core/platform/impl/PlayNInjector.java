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

package es.eucm.eadventure.engine.core.platform.impl;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.core.gameobjects.go.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.ComposedSceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.VideoSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ActorActionsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.CancelEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeFieldGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.HighlightEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.InterpolationGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.InventoryEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.PlaySoundEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.QuitGameEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.RandomEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ShowSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.SpeakEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.TriggerMacroEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.WaitEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics.PhApplyForceGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics.PhysicsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.ConditionEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SceneElementEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SceneElementTimedEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SystemEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ComplexSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.SimpleTransitionGO;
import es.eucm.eadventure.engine.core.platform.GenericInjector;

public class PlayNInjector implements GenericInjector {

	private PlayNGinInjector ginjector;

	@Inject
	public PlayNInjector(PlayNGinInjector injector) {
		this.ginjector = injector;
	}

	public void setInjector(PlayNGinInjector ginjector) {
		this.ginjector = ginjector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(Class<T> clazz) {
		Object go = null;
		if (clazz == SimpleTransitionGO.class)
			go = ginjector.getSimpleTransitionGO();
		else if (clazz == ActorActionsEffectGO.class)
			go = ginjector.getActorActionEffectGO();
		else if (clazz == CancelEffectGO.class)
			go = ginjector.getCancelEffectGO();
		else if (clazz == ChangeSceneGO.class)
			go = ginjector.getChangeSceneGO();
		else if (clazz == ChangeFieldGO.class)
			go = ginjector.getChangeFieldGO();
		else if (clazz == ComplexBlockingEffectGO.class)
			go = ginjector.getComplexBlockingEffectGO();
		else if (clazz == HighlightEffectGO.class)
			go = ginjector.getHighlightEffectGO();
		else if (clazz == InventoryEffectGO.class)
			go = ginjector.getModifyActorStateGO();
		else if (clazz == MoveSceneElementGO.class)
			go = ginjector.getMoveSceneElementGO();
		else if (clazz == PlaySoundEffectGO.class)
			go = ginjector.getPlaySoundEffectGO();
		else if (clazz == QuitGameEffectGO.class)
			go = ginjector.getQuitGameEffectGO();
		else if (clazz == RandomEffectGO.class)
			go = ginjector.getRandomEffectGO();
		else if (clazz == ShowSceneElementGO.class)
			go = ginjector.getShowSceneElementGO();
		else if (clazz == SpeakEffectGO.class)
			go = ginjector.getSpeakEffectGO();
		else if (clazz == TriggerMacroEffectGO.class)
			go = ginjector.getTriggerMacroEffectGO();
		else if (clazz == InterpolationGO.class)
			go = ginjector.getVarInterpolationGO();
		else if (clazz == WaitEffectGO.class)
			go = ginjector.getWaitEffectGO();
		else if (clazz == PhysicsEffectGO.class)
			go = ginjector.getPhysicsEffectGO();
		else if (clazz == PhApplyForceGO.class)
			go = ginjector.getPhApplyForceGO();
		else if (clazz == ConditionEventGO.class)
			go = ginjector.getConditionEventGO();
		else if (clazz == SceneElementEventGO.class)
			go = ginjector.getSceneElementEventGO();
		else if (clazz == SceneElementTimedEventGO.class)
			go = ginjector.getSceneElementTimedEventGO();
		else if (clazz == SystemEventGO.class)
			go = ginjector.getSystemEventGO();
		else if (clazz == BasicSceneElementGO.class)
			go = ginjector.getBasicSceneElementGO();
		else if (clazz == ComplexSceneElementGO.class)
			go = ginjector.getComplexSceneElementGO();
		else if (clazz == ComposedSceneGOImpl.class)
			go = ginjector.getComposedSceneGO();
		else if (clazz == SceneGO.class || clazz == SceneGOImpl.class)
			go = ginjector.getSceneGO();
		else if (clazz == VideoSceneGO.class)
			go = ginjector.getVideoSceneGO();
		return (T) go;
	}

}

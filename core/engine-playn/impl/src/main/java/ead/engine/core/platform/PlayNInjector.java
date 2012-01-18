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

import com.google.inject.Inject;

import ead.engine.core.gameobjects.ComposedSceneGOImpl;
import ead.engine.core.gameobjects.SceneGOImpl;
import ead.engine.core.gameobjects.VideoSceneGO;
import ead.engine.core.gameobjects.effects.ActorActionsGO;
import ead.engine.core.gameobjects.effects.CancelEffectGO;
import ead.engine.core.gameobjects.effects.ChangeFieldGO;
import ead.engine.core.gameobjects.effects.ChangeSceneGO;
import ead.engine.core.gameobjects.effects.ComplexBlockingEffectGO;
import ead.engine.core.gameobjects.effects.HighlightSceneElementGO;
import ead.engine.core.gameobjects.effects.InterpolationGO;
import ead.engine.core.gameobjects.effects.ModifyInventoryGO;
import ead.engine.core.gameobjects.effects.MoveSceneElementGO;
import ead.engine.core.gameobjects.effects.PlaySoundGO;
import ead.engine.core.gameobjects.effects.QuitGameGO;
import ead.engine.core.gameobjects.effects.RandomEffectGO;
import ead.engine.core.gameobjects.effects.ShowSceneElementGO;
import ead.engine.core.gameobjects.effects.SpeakGO;
import ead.engine.core.gameobjects.effects.TriggerMacroGO;
import ead.engine.core.gameobjects.effects.WaitGO;
import ead.engine.core.gameobjects.effects.physics.PhApplyForceGO;
import ead.engine.core.gameobjects.effects.physics.PhysicsEffectGO;
import ead.engine.core.gameobjects.events.ConditionEvGO;
import ead.engine.core.gameobjects.events.SceneElementEvGO;
import ead.engine.core.gameobjects.events.SystemEvGO;
import ead.engine.core.gameobjects.events.TimedEvGO;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.sceneelements.BasicSceneElementGO;
import ead.engine.core.gameobjects.sceneelements.ComplexSceneElementGO;
import ead.engine.core.gameobjects.transitions.BasicTransitionGO;
import ead.engine.core.gameobjects.transitions.DisplaceTransitionGO;
import ead.engine.core.gameobjects.transitions.FadeInTransitionGO;

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
		if (clazz == BasicTransitionGO.class)
			go = ginjector.getSimpleTransitionGO();
		else if ( clazz == DisplaceTransitionGO.class )
			go = ginjector.getDisplaceTransitionGO();
		else if ( clazz == FadeInTransitionGO.class )
			go = ginjector.getDisplaceTransitionGO();
		else if (clazz == ActorActionsGO.class)
			go = ginjector.getActorActionEffectGO();
		else if (clazz == CancelEffectGO.class)
			go = ginjector.getCancelEffectGO();
		else if (clazz == ChangeSceneGO.class)
			go = ginjector.getChangeSceneGO();
		else if (clazz == ChangeFieldGO.class)
			go = ginjector.getChangeFieldGO();
		else if (clazz == ComplexBlockingEffectGO.class)
			go = ginjector.getComplexBlockingEffectGO();
		else if (clazz == HighlightSceneElementGO.class)
			go = ginjector.getHighlightEffectGO();
		else if (clazz == ModifyInventoryGO.class)
			go = ginjector.getModifyActorStateGO();
		else if (clazz == MoveSceneElementGO.class)
			go = ginjector.getMoveSceneElementGO();
		else if (clazz == PlaySoundGO.class)
			go = ginjector.getPlaySoundEffectGO();
		else if (clazz == QuitGameGO.class)
			go = ginjector.getQuitGameEffectGO();
		else if (clazz == RandomEffectGO.class)
			go = ginjector.getRandomEffectGO();
		else if (clazz == ShowSceneElementGO.class)
			go = ginjector.getShowSceneElementGO();
		else if (clazz == SpeakGO.class)
			go = ginjector.getSpeakEffectGO();
		else if (clazz == TriggerMacroGO.class)
			go = ginjector.getTriggerMacroEffectGO();
		else if (clazz == InterpolationGO.class)
			go = ginjector.getVarInterpolationGO();
		else if (clazz == WaitGO.class)
			go = ginjector.getWaitEffectGO();
		else if (clazz == PhysicsEffectGO.class)
			go = ginjector.getPhysicsEffectGO();
		else if (clazz == PhApplyForceGO.class)
			go = ginjector.getPhApplyForceGO();
		else if (clazz == ConditionEvGO.class)
			go = ginjector.getConditionEventGO();
		else if (clazz == SceneElementEvGO.class)
			go = ginjector.getSceneElementEventGO();
		else if (clazz == TimedEvGO.class)
			go = ginjector.getSceneElementTimedEventGO();
		else if (clazz == SystemEvGO.class)
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

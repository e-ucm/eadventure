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

package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.Map;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdAddActorReferenceEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdCancelEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeCursorEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveActiveElement;
import es.eucm.eadventure.common.model.effects.impl.EAdPlaySoundEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdQuitGame;
import es.eucm.eadventure.common.model.effects.impl.EAdRandomEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdHighlightSceneElement;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdWaitEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ActorActionsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AddActorReferenceEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.CancelEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeCursorEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeFieldGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.HighlightEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.InterpolationGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ModifyActorStateGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveActiveElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.PlaySoundEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.QuitGameEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.RandomEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ShowSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.SpeakEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.TriggerMacroEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.WaitEffectGO;

public class EffectGameObjectFactoryConfigurator {
	
	public void configure(
			Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> factoryMap) {
		factoryMap.put(EAdShowSceneElement.class, ShowSceneElementGO.class);
		factoryMap.put(EAdComplexBlockingEffect.class,
				ComplexBlockingEffectGO.class);
		factoryMap.put(EAdChangeScene.class, ChangeSceneGO.class);
		factoryMap.put(EAdMoveSceneElement.class, MoveSceneElementGO.class);
		factoryMap.put(EAdHighlightSceneElement.class,
				HighlightEffectGO.class);
		factoryMap.put(EAdWaitEffect.class, WaitEffectGO.class);
		factoryMap.put(EAdCancelEffect.class, CancelEffectGO.class);
		factoryMap.put(EAdChangeFieldValueEffect.class, ChangeFieldGO.class);
		factoryMap.put(EAdActorActionsEffect.class, ActorActionsEffectGO.class);
		factoryMap.put(EAdQuitGame.class, QuitGameEffectGO.class);
		factoryMap.put(EAdTriggerMacro.class, TriggerMacroEffectGO.class);
		factoryMap.put(EAdModifyActorState.class, ModifyActorStateGO.class);
		factoryMap.put(EAdRandomEffect.class, RandomEffectGO.class);
		factoryMap.put(EAdInterpolationEffect.class, InterpolationGO.class);
		factoryMap.put(EAdMoveActiveElement.class, MoveActiveElementGO.class);
		factoryMap.put(EAdSpeakEffect.class, SpeakEffectGO.class);
		factoryMap.put(EAdPlaySoundEffect.class, PlaySoundEffectGO.class);
		factoryMap.put(EAdAddActorReferenceEffect.class, AddActorReferenceEffectGO.class);
		factoryMap.put(EAdChangeCursorEffect.class, ChangeCursorEffectGO.class);
		
	}

}

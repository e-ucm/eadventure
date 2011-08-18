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
import es.eucm.eadventure.common.model.effects.impl.EAdCancelEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveActiveElement;
import es.eucm.eadventure.common.model.effects.impl.EAdPlaySoundEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdQuitGame;
import es.eucm.eadventure.common.model.effects.impl.EAdRandomEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdWaitEffect;
import es.eucm.eadventure.common.model.effects.impl.actorreference.EAdHighlightSceneElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ActorActionsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.CancelEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeAppearanceGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeScreenGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeVariableGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.HighlightEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MakeActiveElementEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ModifyActorStateGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveActiveElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.PlaySoundEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.QuitGameEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.RandomEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ShowTextEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.SpeakEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.TriggerMacroEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.VarInterpolationGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.WaitEffectGO;

public class EffectGameObjectFactoryConfigurator {
	
	public void configure(
			Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> factoryMap) {
		factoryMap.put(EAdShowText.class, ShowTextEffectGO.class);
		factoryMap.put(EAdComplexBlockingEffect.class,
				ComplexBlockingEffectGO.class);
		factoryMap.put(EAdChangeScene.class, ChangeScreenGO.class);
		factoryMap.put(EAdMoveSceneElement.class, MoveSceneElementGO.class);
		factoryMap.put(EAdHighlightSceneElement.class,
				HighlightEffectGO.class);
		factoryMap.put(EAdWaitEffect.class, WaitEffectGO.class);
		factoryMap.put(EAdCancelEffect.class, CancelEffectGO.class);
		factoryMap.put(EAdChangeVarValueEffect.class, ChangeVariableGO.class);
		factoryMap.put(EAdActorActionsEffect.class, ActorActionsEffectGO.class);
		factoryMap.put(EAdQuitGame.class, QuitGameEffectGO.class);
		factoryMap.put(EAdChangeAppearance.class, ChangeAppearanceGO.class);
		factoryMap.put(EAdTriggerMacro.class, TriggerMacroEffectGO.class);
		factoryMap.put(EAdModifyActorState.class, ModifyActorStateGO.class);
		factoryMap.put(EAdRandomEffect.class, RandomEffectGO.class);
		factoryMap.put(EAdVarInterpolationEffect.class, VarInterpolationGO.class);
		factoryMap.put(EAdMakeActiveElementEffect.class, MakeActiveElementEffectGO.class);
		factoryMap.put(EAdMoveActiveElement.class, MoveActiveElementGO.class);
		factoryMap.put(EAdSpeakEffect.class, SpeakEffectGO.class);
		factoryMap.put(EAdPlaySoundEffect.class, PlaySoundEffectGO.class);
	}

}

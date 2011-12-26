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

package es.eucm.eadventure.engine.core.factorymapproviders;

import es.eucm.eadventure.common.model.elements.EAdEffect;
import es.eucm.eadventure.common.model.elements.effects.ActorActionsEf;
import es.eucm.eadventure.common.model.elements.effects.AddActorReferenceEf;
import es.eucm.eadventure.common.model.elements.effects.CancelEffectEf;
import es.eucm.eadventure.common.model.elements.effects.ChangeSceneEf;
import es.eucm.eadventure.common.model.elements.effects.ComplexBlockingEffect;
import es.eucm.eadventure.common.model.elements.effects.InterpolationEf;
import es.eucm.eadventure.common.model.elements.effects.ModifyInventoryEf;
import es.eucm.eadventure.common.model.elements.effects.PlaySoundEf;
import es.eucm.eadventure.common.model.elements.effects.QuitGameEf;
import es.eucm.eadventure.common.model.elements.effects.RandomEf;
import es.eucm.eadventure.common.model.elements.effects.TriggerMacroEf;
import es.eucm.eadventure.common.model.elements.effects.hud.ModifyHUDEf;
import es.eucm.eadventure.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import es.eucm.eadventure.common.model.elements.effects.text.ShowQuestionEf;
import es.eucm.eadventure.common.model.elements.effects.text.SpeakEf;
import es.eucm.eadventure.common.model.elements.effects.timedevents.HighlightSceneElementEf;
import es.eucm.eadventure.common.model.elements.effects.timedevents.ShowSceneElementEf;
import es.eucm.eadventure.common.model.elements.effects.timedevents.WaitEf;
import es.eucm.eadventure.common.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.eadventure.engine.core.gameobjects.effects.ActorActionsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.AddActorReferenceEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.CancelEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.ChangeFieldGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.ChangeSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.HighlightEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.InterpolationGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.InventoryEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.ModifyHudGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.MoveSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.PlaySoundEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.QuitGameEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.RandomEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.ShowSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.SpeakEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.TriggerMacroEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.effects.WaitEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.go.EffectGO;

public class EffectGameObjectFactoryConfigurator
		extends
		AbstractMapProvider<Class<? extends EAdEffect>, Class<? extends EffectGO<? extends EAdEffect>>> {

	public EffectGameObjectFactoryConfigurator() {
		factoryMap.put(ShowSceneElementEf.class, ShowSceneElementGO.class);
		factoryMap.put(ComplexBlockingEffect.class,
				ComplexBlockingEffectGO.class);
		factoryMap.put(ChangeSceneEf.class, ChangeSceneGO.class);
		factoryMap.put(MoveSceneElementEf.class, MoveSceneElementGO.class);
		factoryMap.put(HighlightSceneElementEf.class, HighlightEffectGO.class);
		factoryMap.put(WaitEf.class, WaitEffectGO.class);
		factoryMap.put(CancelEffectEf.class, CancelEffectGO.class);
		factoryMap.put(ChangeFieldEf.class, ChangeFieldGO.class);
		factoryMap.put(ActorActionsEf.class, ActorActionsEffectGO.class);
		factoryMap.put(QuitGameEf.class, QuitGameEffectGO.class);
		factoryMap.put(TriggerMacroEf.class, TriggerMacroEffectGO.class);
		factoryMap.put(ModifyInventoryEf.class, InventoryEffectGO.class);
		factoryMap.put(RandomEf.class, RandomEffectGO.class);
		factoryMap.put(InterpolationEf.class, InterpolationGO.class);
		factoryMap.put(SpeakEf.class, SpeakEffectGO.class);
		factoryMap.put(PlaySoundEf.class, PlaySoundEffectGO.class);
		factoryMap.put(AddActorReferenceEf.class,
				AddActorReferenceEffectGO.class);
		factoryMap.put(ShowQuestionEf.class, ComplexBlockingEffectGO.class);
		factoryMap.put(ModifyHUDEf.class, ModifyHudGO.class);
	}
	
	

}

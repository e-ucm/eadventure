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

package ead.engine.core.factories.mapproviders;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.*;
import ead.common.model.elements.effects.hud.ModifyHUDEf;
import ead.common.model.elements.effects.physics.PhApplyImpulseEf;
import ead.common.model.elements.effects.physics.PhysicsEf;
import ead.common.model.elements.effects.sceneelements.ChangeColorEf;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.text.QuestionEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.timedevents.HighlightSceneElementEf;
import ead.common.model.elements.effects.timedevents.ShowSceneElementEf;
import ead.common.model.elements.effects.timedevents.WaitEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.engine.core.gameobjects.effects.*;
import ead.engine.core.gameobjects.effects.sceneelement.ChangeColorGO;

public class EffectsMapProvider
		extends
		AbstractMapProvider<Class<? extends EAdEffect>, Class<? extends EffectGO<? extends EAdEffect>>> {

	public EffectsMapProvider() {
		factoryMap.put(ShowSceneElementEf.class, ShowSceneElementGO.class);
		factoryMap.put(ChangeSceneEf.class, ChangeSceneGO.class);
		factoryMap.put(MoveSceneElementEf.class, MoveSceneElementGO.class);
		factoryMap.put(HighlightSceneElementEf.class,
				HighlightSceneElementGO.class);
		factoryMap.put(WaitEf.class, WaitGO.class);
		factoryMap.put(ChangeFieldEf.class, ChangeFieldGO.class);
		factoryMap.put(ActorActionsEf.class, ActorActionsGO.class);
		factoryMap.put(QuitGameEf.class, QuitGameGO.class);
		factoryMap.put(TriggerMacroEf.class, TriggerMacroGO.class);
		factoryMap.put(ModifyInventoryEf.class, ModifyInventoryGO.class);
		factoryMap.put(InterpolationEf.class, InterpolationGO.class);
		factoryMap.put(SpeakEf.class, SpeakGO.class);
		factoryMap.put(PlaySoundEf.class, PlaySoundGO.class);
        factoryMap.put(PlayMusicEf.class, PlayMusicGO.class);
		factoryMap.put(AddActorReferenceEf.class, AddActorReferenceGO.class);
		factoryMap.put(ModifyHUDEf.class, ModifyHudGO.class);
		factoryMap.put(RandomEf.class, RandomGO.class);
		factoryMap.put(RemoveEf.class, RemoveGO.class);
		factoryMap.put(QuestionEf.class, QuestionGO.class);
		factoryMap.put(AddChildEf.class, AddChildGO.class);
		factoryMap.put(WaitUntilEf.class, WaitUntilGO.class);
		factoryMap.put(ChangeColorEf.class, ChangeColorGO.class);
		factoryMap.put(ToggleSoundEf.class, ToggleSoundGO.class);
		factoryMap.put(EmptyEffect.class, EmptyEffectGO.class);
		factoryMap.put(TogglePauseEf.class, TogglePauseGO.class);
		factoryMap.put(DragEf.class, DragGO.class);
		factoryMap.put(LoadGameEf.class, LoadGameGO.class);

		// Physics
		factoryMap.put(PhysicsEf.class, PhysicsEffectGO.class);
		factoryMap.put(PhApplyImpulseEf.class, ApplyForceGO.class);
	}

}

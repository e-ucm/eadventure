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

package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.WaitUntilEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.BasicField;
import ead.converter.subconverters.CutsceneConverter;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerCutsceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerLastSceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerSceneEffect;

@SuppressWarnings("rawtypes")
public class ChangeSceneConverter implements EffectConverter {

	public ChangeSceneConverter() {

	}

	@Override
	public List<EAdEffect> convert(Effect e) {
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();
		// Normal scenes
		if (e instanceof TriggerSceneEffect) {
			TriggerSceneEffect ef = (TriggerSceneEffect) e;
			list.add(new ChangeSceneEf(new BasicElement(ef.getTargetId())));
			// Cutscenes
		} else if (e instanceof TriggerCutsceneEffect) {

			TriggerCutsceneEffect ef = (TriggerCutsceneEffect) e;
			BasicElement nextScene = new BasicElement(ef.getTargetId());
			ChangeSceneEf changeScene = new ChangeSceneEf(nextScene);

			// When a cutscene is triggered, all the effects after it must wait
			// to be launched until the cutscene ends. We make sure that
			// IN_CUTSCENE is set to true before launching any other effect
			BasicField<Boolean> field = new BasicField<Boolean>(nextScene,
					CutsceneConverter.IN_CUTSCENE);
			changeScene.getSimultaneousEffects().add(
					new ChangeFieldEf(field, EmptyCond.TRUE));
			EAdCondition cond = new NOTCond(new OperationCond(field));
			WaitUntilEf waitUntil = new WaitUntilEf(cond);
			waitUntil.setPersistent(true);
			changeScene.getNextEffects().add(waitUntil);
			changeScene.getNextEffects().add(EffectsConverter.hideGhostEffects);
			list.add(changeScene);
			list.add(waitUntil);
			waitUntil.getNextEffects().add(EffectsConverter.showGhostEffects);
		} else if (e instanceof TriggerLastSceneEffect) {
			list.add(new ChangeSceneEf());
		}
		return list;
	}
}

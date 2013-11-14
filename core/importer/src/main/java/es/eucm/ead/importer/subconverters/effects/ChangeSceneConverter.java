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

package es.eucm.ead.importer.subconverters.effects;

import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.subconverters.CutsceneConverter;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.WaitUntilEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.eadventure.common.data.chapter.effects.TriggerCutsceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerLastSceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerSceneEffect;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ChangeSceneConverter implements EffectConverter {

	private final ModelQuerier modelQuerier;
	private EffectsConverter effectsConverter;

	public ChangeSceneConverter(EffectsConverter effectsConverter,
			ModelQuerier modelQuerier) {
		this.effectsConverter = effectsConverter;
		this.modelQuerier = modelQuerier;
	}

	@Override
	public List<Effect> convert(
			es.eucm.eadventure.common.data.chapter.effects.Effect e) {
		ArrayList<Effect> list = new ArrayList<Effect>();
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
			ElementField field = new ElementField(modelQuerier
					.getCurrentChapter(), CutsceneConverter.IN_CUTSCENE
					+ ef.getTargetId(), false);
			changeScene.addSimultaneousEffect(new ChangeFieldEf(field,
					EmptyCond.TRUE));

			WaitUntilEf waitUntil = new WaitUntilEf(new NOTCond(
					new OperationCond(field)));
			// To avoid be deleted after changing the scene
			waitUntil.setPersistent(true);

			changeScene.addNextEffect(waitUntil);

			// To allow clicks in cutscenes
			changeScene.addNextEffect(effectsConverter.hideGhostEffects);
			waitUntil.addNextEffect(effectsConverter.showGhostEffects);

			list.add(changeScene);
			list.add(waitUntil);
		} else if (e instanceof TriggerLastSceneEffect) {
			list.add(new ChangeSceneEf());
		}
		return list;
	}
}

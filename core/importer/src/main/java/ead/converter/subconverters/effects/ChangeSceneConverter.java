package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.WaitUntilEf;
import ead.common.model.elements.operations.BasicField;
import ead.converter.subconverters.CutsceneConverter;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerCutsceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerSceneEffect;

@SuppressWarnings("rawtypes")
public class ChangeSceneConverter implements EffectConverter {

	public ChangeSceneConverter() {

	}

	@Override
	public List<EAdEffect> convert(Effect e) {
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();
		if (e instanceof TriggerSceneEffect) {
			TriggerSceneEffect ef = (TriggerSceneEffect) e;
			list.add(new ChangeSceneEf(new BasicElement(ef.getTargetId())));
		} else if (e instanceof TriggerCutsceneEffect) {
			TriggerCutsceneEffect ef = (TriggerCutsceneEffect) e;
			BasicElement nextScene = new BasicElement(ef.getTargetId());
			ChangeSceneEf changeScene = new ChangeSceneEf(nextScene);

			EAdCondition cond = new NOTCond(new OperationCond(
					new BasicField<Boolean>(nextScene,
							CutsceneConverter.IN_CUTSCENE)));

			WaitUntilEf waitUntil = new WaitUntilEf(cond);
			changeScene.getNextEffects().add(waitUntil);
			changeScene.getNextEffects().add(EffectsConverter.hideGhostEffects);
			list.add(changeScene);
			list.add(waitUntil);
			waitUntil.getNextEffects().add(EffectsConverter.showGhostEffects);
		}
		return list;
	}
}

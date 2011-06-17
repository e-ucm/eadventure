package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.TriggerLastSceneEffect;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class TriggerPreviousSceneImporter extends EffectImporter<TriggerLastSceneEffect, EAdEffect>{
	
	@Inject
	public TriggerPreviousSceneImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter) {
		super(conditionImporter);
	}

	@Override
	public EAdEffect init(TriggerLastSceneEffect oldObject) {
		return new EAdChangeScene("gotToPreviousScene");
	}

	@Override
	public EAdEffect convert(TriggerLastSceneEffect oldObject, Object object) {
		return (EAdChangeScene) object;
	}

}

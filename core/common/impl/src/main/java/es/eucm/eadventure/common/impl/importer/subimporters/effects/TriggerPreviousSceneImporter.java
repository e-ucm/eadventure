package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.TriggerLastSceneEffect;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class TriggerPreviousSceneImporter extends EffectImporter<TriggerLastSceneEffect, EAdEffect>{
	
	@Inject
	public TriggerPreviousSceneImporter(
			Importer<Conditions, EAdCondition> conditionImporter) {
		super(conditionImporter);
	}

	@Override
	public EAdEffect convert(TriggerLastSceneEffect oldObject) {
		return new EAdChangeScene("gotToPreviousScene");
	}

	@Override
	public boolean equals(TriggerLastSceneEffect oldObject,
			EAdEffect newObject) {
		// TODO Auto-generated method stub
		return false;
	}

}

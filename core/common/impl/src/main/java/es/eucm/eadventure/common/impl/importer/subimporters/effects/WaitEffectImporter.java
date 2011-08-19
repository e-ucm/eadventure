package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.WaitTimeEffect;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdWaitEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class WaitEffectImporter extends EffectImporter<WaitTimeEffect, EAdWaitEffect>{

	@Inject
	public WaitEffectImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter) {
		super(conditionImporter);
	}

	@Override
	public EAdWaitEffect init(WaitTimeEffect oldObject) {
		return new EAdWaitEffect("waitEffect" + ID_GENERATOR);
	}

	@Override
	public EAdWaitEffect convert(WaitTimeEffect oldObject, Object newElement) {
		EAdWaitEffect effect = super.convert(oldObject, newElement);
		effect.setTime(oldObject.getTime());
		return effect;
	}
	
	

}

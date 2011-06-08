package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.RandomEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdRandomEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class RandomEffectImporter extends
		EffectImporter<RandomEffect, EAdRandomEffect> {

	private EffectsImporterFactory effectsImporterFactory;

	private static int ID_GENERATOR = 0;

	@Inject
	public RandomEffectImporter(
			Importer<Conditions, EAdCondition> conditionImporter,
			EffectsImporterFactory effectsImporterFactory) {
		super(conditionImporter);
		this.effectsImporterFactory = effectsImporterFactory;
	}

	@Override
	public EAdRandomEffect convert(RandomEffect oldObject) {
		EAdRandomEffect effect = new EAdRandomEffect("randomEffect"
				+ ID_GENERATOR++);

		super.importConditions(oldObject, effect);

		EAdEffect positiveEffect = (EAdEffect) effectsImporterFactory
				.getEffect(oldObject.getPositiveEffect());
		effect.addEffect(positiveEffect, oldObject.getProbability());

		EAdEffect negativeEffect = (EAdEffect) effectsImporterFactory
				.getEffect(oldObject.getNegativeEffect());
		effect.addEffect(negativeEffect, 100.0f - oldObject.getProbability());

		return effect;
	}
}

package ead.converter.subconverters.effects.variables;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.RandomEf;
import ead.converter.subconverters.effects.EffectsConverter;
import es.eucm.eadventure.common.data.chapter.effects.RandomEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anserran
 *         Date: 17/05/13
 *         Time: 13:26
 */
public class RandomEffectConverter implements
		EffectsConverter.EffectConverter<RandomEffect> {

	private final EffectsConverter effectsConverter;

	public RandomEffectConverter(EffectsConverter effectsConverter) {
		this.effectsConverter = effectsConverter;
	}

	@Override
	public List<EAdEffect> convert(RandomEffect e) {
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();
		RandomEf effect = new RandomEf();
		List<EAdEffect> positiveEffect = effectsConverter.convert(e
				.getPositiveEffect());
		effect.addEffect(positiveEffect.get(0), e.getProbability());

		List<EAdEffect> negativeEffect = effectsConverter.convert(e
				.getNegativeEffect());
		effect.addEffect(negativeEffect.get(0), 100.0f - e.getProbability());
		list.add(effect);
		return list;
	}
}

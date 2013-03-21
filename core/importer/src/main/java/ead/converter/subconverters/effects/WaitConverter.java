package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.timedevents.WaitEf;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.WaitTimeEffect;

public class WaitConverter implements EffectConverter<WaitTimeEffect> {

	@Override
	public List<EAdEffect> convert(WaitTimeEffect e) {
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();
		list.add(new WaitEf(e.getTime() * 1000));
		return list;
	}

}

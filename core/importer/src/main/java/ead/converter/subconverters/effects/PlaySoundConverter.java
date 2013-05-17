package ead.converter.subconverters.effects;

import ead.common.model.assets.multimedia.Sound;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.PlaySoundEf;
import ead.converter.resources.ResourcesConverter;
import es.eucm.eadventure.common.data.chapter.effects.PlaySoundEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anserran
 *         Date: 17/05/13
 *         Time: 13:36
 */
public class PlaySoundConverter implements
		EffectsConverter.EffectConverter<PlaySoundEffect> {

	private ResourcesConverter resourcesConverter;

	public PlaySoundConverter(ResourcesConverter resourcesConverter) {
		this.resourcesConverter = resourcesConverter;
	}

	@Override
	public List<EAdEffect> convert(PlaySoundEffect e) {
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();
		PlaySoundEf effect = new PlaySoundEf();
		String newString = resourcesConverter.getPath(e.getPath());

		effect.setSound(new Sound(newString));
		list.add(effect);
		return list;
	}
}

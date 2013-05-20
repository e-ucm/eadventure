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

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

package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.params.fills.Paint;
import ead.converter.ModelQuerier;
import ead.converter.StringsConverter;
import ead.converter.UtilsConverter;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.data.chapter.effects.ShowTextEffect;
import es.eucm.eadventure.common.data.chapter.effects.SpeakCharEffect;
import es.eucm.eadventure.common.data.chapter.effects.SpeakPlayerEffect;
import es.eucm.eadventure.common.data.chapter.elements.Player;

@SuppressWarnings("rawtypes")
public class SpeakEffectConverter implements EffectConverter {

	private ModelQuerier modelQuerier;

	private StringsConverter stringsConverter;

	private UtilsConverter utilsConverter;

	public SpeakEffectConverter(ModelQuerier modelQuerier,
			StringsConverter stringsConverter, UtilsConverter utilsConverter) {
		this.modelQuerier = modelQuerier;
		this.stringsConverter = stringsConverter;
		this.utilsConverter = utilsConverter;
	}

	public List<EAdEffect> convert(Effect e) {
		// [ST - Speak]
		ArrayList<EAdEffect> effects = new ArrayList<EAdEffect>();
		SpeakEf effect = null;
		// XXX e.getAudioPath()
		String text = null;
		if (e instanceof SpeakCharEffect) {
			SpeakCharEffect sc = (SpeakCharEffect) e;
			text = sc.getLine();
			effect = modelQuerier.getSpeakFor(sc.getTargetId(),
					stringsConverter.convert(text, true));

		} else if (e instanceof SpeakPlayerEffect) {
			SpeakPlayerEffect sp = (SpeakPlayerEffect) e;
			text = sp.getLine();
			effect = modelQuerier.getSpeakFor(Player.IDENTIFIER,
					stringsConverter.convert(text, true));

		} else if (e instanceof ShowTextEffect) {
			ShowTextEffect st = (ShowTextEffect) e;
			text = st.getText();
			effect = new SpeakEf(stringsConverter.convert(text, true));
			effect.setColor(
					utilsConverter.getPaint(Integer.toHexString(st
							.getRgbFrontColor())
							+ "ff", Integer.toHexString(st.getRgbBorderColor())
							+ "ff"), Paint.TRANSPARENT);

		}

		// Add possible operations in the text
		List<EAdOperation> ops = stringsConverter.getOperations(text);
		if (ops.size() > 0) {
			effect.getCaption().getOperations().addAll(ops);
		}
		effects.add(effect);
		return effects;
	}

}
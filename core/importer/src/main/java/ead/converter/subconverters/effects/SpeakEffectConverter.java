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
		ArrayList<EAdEffect> effects = new ArrayList<EAdEffect>();
		SpeakEf effect = null;
		// XXX e.getAudioPath()
		String text = null;
		if (e instanceof SpeakCharEffect) {
			SpeakCharEffect sc = (SpeakCharEffect) e;
			text = sc.getLine();
			effect = modelQuerier.getSpeakFor(sc.getTargetId(),
					stringsConverter.convert(text));

		} else if (e instanceof SpeakPlayerEffect) {
			SpeakPlayerEffect sp = (SpeakPlayerEffect) e;
			text = sp.getLine();
			effect = modelQuerier.getSpeakFor(Player.IDENTIFIER,
					stringsConverter.convert(text));

		} else if (e instanceof ShowTextEffect) {
			ShowTextEffect st = (ShowTextEffect) e;
			text = st.getText();
			effect = new SpeakEf(stringsConverter.convert(text));
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

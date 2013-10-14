package es.eucm.ead.importer.subconverters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.StringsConverter;
import es.eucm.ead.importer.subconverters.conditions.ConditionsConverter;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter;
import es.eucm.ead.legacyplugins.model.TimerEv;
import es.eucm.ead.model.elements.EAdEvent;
import es.eucm.eadventure.common.data.chapter.Timer;

@Singleton
public class TimerConverter {

	private StringsConverter stringsConverter;

	private ConditionsConverter conditionsConverter;

	private EffectsConverter effectsConverter;

	@Inject
	public TimerConverter(StringsConverter stringsConverter,
			ConditionsConverter conditionsConverter,
			EffectsConverter effectsConverter) {
		this.stringsConverter = stringsConverter;
		this.conditionsConverter = conditionsConverter;
		this.effectsConverter = effectsConverter;
	}

	public EAdEvent convert(Timer t) {
		TimerEv timer = new TimerEv();
		// [TI - Time]
		timer.setTime((int) (t.getTime() * 1000));
		// [TI - Display]
		timer.setDisplay(t.isShowTime());
		// [TI - DisplayName]
		if (t.isShowTime() && t.getDisplayName() != null) {
			timer.setDisplayName(stringsConverter.convert(t.getDisplayName(),
					false));
		}
		// [TI - Countdown]
		timer.setCountdown(t.isCountDown());
		// [TI - ShowStopped]
		timer.setShowWhenStopped(t.isShowWhenStopped());
		// [TI - Multiple]
		timer.setMultipleStarts(t.isMultipleStarts());
		// [TI - Loops]
		timer.setRunsInLoops(t.isRunsInLoop());
		// [TI - Condition]
		timer.setInitCondition(conditionsConverter.convert(t.getInitCond()));
		// [TI - StopConditionCheck]
		if (t.isUsesEndCondition()) {
			// [TI - StopCondition]
			timer.setStopCondition(conditionsConverter.convert(t.getEndCond()));
		}
		// [TI - Effects]
		timer.setExpiredEffects(effectsConverter.convert(t.getEffects()));
		// [TI - StopEffects]
		if (t.isUsesEndCondition()) {
			timer.setStoppedEffects(effectsConverter
					.convert(t.getPostEffects()));
		}
		return timer;
	}
}

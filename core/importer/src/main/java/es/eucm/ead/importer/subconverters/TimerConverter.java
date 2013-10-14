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

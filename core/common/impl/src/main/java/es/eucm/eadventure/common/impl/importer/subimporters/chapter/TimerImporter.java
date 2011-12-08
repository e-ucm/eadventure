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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.elements.impl.EAdTimerImpl;
import es.eucm.eadventure.common.model.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;

public class TimerImporter implements EAdElementImporter<Timer, EAdTimer> {

	private static int ID = 0;

	/**
	 * Conditions importer
	 */
	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	private EffectsImporterFactory effectsImporter;

	@Inject
	public TimerImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EffectsImporterFactory effectsImporter) {
		this.conditionsImporter = conditionsImporter;
		this.effectsImporter = effectsImporter;
	}

	@Override
	public EAdTimer init(Timer oldTimer) {
		EAdTimer timer = new EAdTimerImpl();
		timer.setId("timer" + ID++);
		return timer;
	}

	@Override
	public EAdTimer convert(Timer oldTimer, Object object) {
		EAdTimerImpl newTimer = (EAdTimerImpl) object;

		newTimer.setTime(oldTimer.getTime().intValue() * 1000);

		EAdCondition condition = conditionsImporter
				.init(oldTimer.getInitCond());
		condition = conditionsImporter.convert(oldTimer.getInitCond(),
				condition);

		EAdConditionEventImpl startEvent = new EAdConditionEventImpl( condition);
		startEvent.setId("timerStart");
		EAdChangeFieldValueEffect startEffect = new EAdChangeFieldValueEffect(
				 new EAdFieldImpl<Boolean>(newTimer,
						EAdTimerImpl.VAR_STARTED), new BooleanOperation(
						EmptyCondition.TRUE_EMPTY_CONDITION));
		startEffect.setId("timerStartEffect");
		startEvent.addEffect(ConditionedEventType.CONDITIONS_MET,
				startEffect);
		// TODO timer with events? indeed, has timers sense anymore?
//		newTimer.getEvents().add(startEvent);

		EAdMacroImpl endedMacro = new EAdMacroImpl();
		endedMacro.setId("timerEndMacro");
		EAdTriggerMacro triggerEndedMacro = new EAdTriggerMacro();
		triggerEndedMacro.setId("triggerMacro_" + endedMacro.getId());
//		triggerEndedMacro.setMacro(endedMacro);

		for (Effect e : oldTimer.getEffects().getEffects()) {
			EAdEffect effect = effectsImporter.getEffect(e);
			if (effect != null)
				endedMacro.getEffects().add(effect);
		}

		EAdMacroImpl stoppedMacro = new EAdMacroImpl();
		stoppedMacro.setId("timerStoppedMacro");
		EAdTriggerMacro triggerStoppedMacro = new EAdTriggerMacro();
		triggerStoppedMacro.setId("triggerMacro_" + stoppedMacro.getId());
//		triggerStoppedMacro.setMacro(stoppedMacro);

		for (Effect e : oldTimer.getPostEffects().getEffects()) {
			EAdEffect effect = effectsImporter.getEffect(e);
			if (effect != null)
				stoppedMacro.getEffects().add(effect);
		}
//
//		EAdTimerEventImpl stopTimerEvent = new EAdTimerEventImpl( newTimer);
//		stopTimerEvent.addEffect(TimerEventType.TIMER_ENDED,
//				triggerEndedMacro);
//		stopTimerEvent.addEffect(TimerEventType.TIMER_STOPPED,
//				triggerStoppedMacro);

//		newTimer.getEvents().add(stopTimerEvent);

		return newTimer;
	}
}

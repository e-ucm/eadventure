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
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdTimedEventImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;

public class TimerImporter implements EAdElementImporter<Timer, EAdEvent> {

	private static int ID = 0;

	/**
	 * Conditions importer
	 */
	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	private EffectsImporterFactory effectsImporter;

	private StringHandler stringHandler;

	private static final EAdVarDef<Long> TIMER_VAR = new EAdVarDefImpl<Long>("time_timer",
			Long.class, 0L);

	@Inject
	public TimerImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EffectsImporterFactory effectsImporter, StringHandler stringHandler) {
		this.conditionsImporter = conditionsImporter;
		this.effectsImporter = effectsImporter;
		this.stringHandler = stringHandler;
	}

	@Override
	public EAdEvent init(Timer oldTimer) {
		return new EAdTimedEventImpl();
	}

	@Override
	public EAdEvent convert(Timer oldTimer, Object object) {
		EAdTimedEventImpl event = (EAdTimedEventImpl) object;
		event.setTime(oldTimer.getTime());

		EAdSceneElementDefImpl timerDef = new EAdSceneElementDefImpl();
		EAdBasicSceneElement timer = new EAdBasicSceneElement(timerDef);
		timer.setVarInitialValue(TIMER_VAR, oldTimer.getTime());
		if (oldTimer.isShowTime()) {
			Caption c = new CaptionImpl();
			String timerName = oldTimer.getDisplayName();
			timerDef.setInitialAppearance(c);
		}
		// EAdTimerImpl newTimer = (EAdTimerImpl) object;
		//
		// newTimer.setTime(oldTimer.getTime().intValue() * 1000);
		//
		// EAdCondition condition = conditionsImporter
		// .init(oldTimer.getInitCond());
		// condition = conditionsImporter.convert(oldTimer.getInitCond(),
		// condition);
		//
		// EAdConditionEventImpl startEvent = new EAdConditionEventImpl(
		// condition);
		// startEvent.setId("timerStart");
		// EAdChangeFieldValueEffect startEffect = new
		// EAdChangeFieldValueEffect(
		// new EAdFieldImpl<Boolean>(newTimer,
		// EAdTimerImpl.VAR_STARTED), new BooleanOperation(
		// EmptyCondition.TRUE_EMPTY_CONDITION));
		// startEffect.setId("timerStartEffect");
		// startEvent.addEffect(ConditionedEventType.CONDITIONS_MET,
		// startEffect);
		// TODO timer with events? indeed, has timers sense anymore?
		// newTimer.getEvents().add(startEvent);

		EAdMacroImpl endedMacro = new EAdMacroImpl();
		endedMacro.setId("timerEndMacro");
		EAdTriggerMacro triggerEndedMacro = new EAdTriggerMacro();
		triggerEndedMacro.setId("triggerMacro_" + endedMacro.getId());
		// triggerEndedMacro.setMacro(endedMacro);

		for (Effect e : oldTimer.getEffects().getEffects()) {
			EAdEffect effect = effectsImporter.getEffect(e);
			if (effect != null)
				endedMacro.getEffects().add(effect);
		}

		EAdMacroImpl stoppedMacro = new EAdMacroImpl();
		stoppedMacro.setId("timerStoppedMacro");
		EAdTriggerMacro triggerStoppedMacro = new EAdTriggerMacro();
		triggerStoppedMacro.setId("triggerMacro_" + stoppedMacro.getId());
		// triggerStoppedMacro.setMacro(stoppedMacro);

		// for (Effect e : oldTimer.getPostEffects().getEffects()) {
		// EAdEffect effect = effectsImporter.getEffect(e);
		// if (effect != null)
		// stoppedMacro.getEffects().add(effect);
		// }
		//
		// EAdTimerEventImpl stopTimerEvent = new EAdTimerEventImpl( newTimer);
		// stopTimerEvent.addEffect(TimerEventType.TIMER_ENDED,
		// triggerEndedMacro);
		// stopTimerEvent.addEffect(TimerEventType.TIMER_STOPPED,
		// triggerStoppedMacro);

		// newTimer.getEvents().add(stopTimerEvent);

		return new EAdSceneElementEventImpl();
	}
}

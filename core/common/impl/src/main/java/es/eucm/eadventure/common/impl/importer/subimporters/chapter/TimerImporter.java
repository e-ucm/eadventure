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
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.Comparator;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.hud.ModifyHUDEf;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;

public class TimerImporter implements EAdElementImporter<Timer, EAdEvent> {

	private static int ID = 0;

	/**
	 * Conditions importer
	 */
	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	private EffectsImporterFactory effectsImporter;

	private StringHandler stringHandler;

	private static final EAdVarDef<Integer> CURRENT_TIME_VAR = new EAdVarDefImpl<Integer>(
			"current_time_timer", Integer.class, 0);

	private static final EAdVarDef<Boolean> RUNNING_VAR = new EAdVarDefImpl<Boolean>(
			"stopped_timer", Boolean.class, false);

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
		return new EAdConditionEventImpl();
	}

	@Override
	public EAdEvent convert(Timer oldTimer, Object object) {
		EAdConditionEventImpl event = (EAdConditionEventImpl) object;
		EAdSceneElement timer = getSceneElementForTimer(oldTimer);
		EAdField<Boolean> runningField = new EAdFieldImpl<Boolean>(timer,
				RUNNING_VAR);
		ModifyHUDEf modifyHUD = new ModifyHUDEf(timer, true);

		EAdChangeFieldValueEffect changeRunning = new EAdChangeFieldValueEffect(
				runningField, new BooleanOperation(
						EmptyCondition.TRUE_EMPTY_CONDITION));
		
		modifyHUD.getNextEffects().add(changeRunning);
		
		EAdCondition initCondition = conditionsImporter.init(oldTimer
				.getInitCond());
		initCondition = conditionsImporter.convert(oldTimer.getInitCond(),
				initCondition);
		event.addEffect(ConditionedEventType.CONDITIONS_MET, modifyHUD);
		event.setCondition(initCondition);

		return event;
	}

	private EAdSceneElement getSceneElementForTimer(Timer oldTimer) {
		EAdBasicSceneElement timer = new EAdBasicSceneElement();
		timer.setId("timer" + ID++);

		EAdField<Integer> currentTimeField = new EAdFieldImpl<Integer>(timer,
				CURRENT_TIME_VAR);
		EAdField<Boolean> runningField = new EAdFieldImpl<Boolean>(timer,
				RUNNING_VAR);

		// Appearance
		if (oldTimer.isShowTime())
			addAppearance(timer, oldTimer, currentTimeField);

		// Event to update the timer
		EAdSceneElementEvent updater = new EAdSceneElementEventImpl();
		timer.getEvents().add(updater);

		// Update current time
		addUpdateCurrentTime(updater, currentTimeField, runningField);

		// Check if time expired
		addEffectsTimeExpired(timer, updater, currentTimeField, runningField, oldTimer);

		return timer;
	}

	private void addEffectsTimeExpired(EAdSceneElement timer, EAdSceneElementEvent updater,
			EAdField<Integer> currentTimeField, EAdField<Boolean> runningField,
			Timer oldTimer) {
		int time = oldTimer.getTime().intValue() * 1000;
		
		EAdMacro expiredEffects = effectsImporter.getMacroEffects(oldTimer
				.getEffects());
		EAdTriggerMacro triggerExpiredEffects = new EAdTriggerMacro();
		triggerExpiredEffects.putMacro(expiredEffects,
				EmptyCondition.TRUE_EMPTY_CONDITION);
		EAdCondition expireCondition = null;
		if (oldTimer.isCountDown()) {
			expireCondition = new OperationCondition(currentTimeField, 0,
					Comparator.LESS_EQUAL);
		} else {
			expireCondition = new OperationCondition(currentTimeField, time, Comparator.GREATER_EQUAL);
		}
		
		// Stop the timer if expires and set current time to its final value
		expiredEffects.getEffects().add(
				new EAdChangeFieldValueEffect(runningField,
						new BooleanOperation(
								EmptyCondition.FALSE_EMPTY_CONDITION)));
		if (oldTimer.isCountDown()) {
			expiredEffects.getEffects().add(
					new EAdChangeFieldValueEffect(currentTimeField,
							new ValueOperation(0)));
		} else {
			expiredEffects.getEffects().add(
					new EAdChangeFieldValueEffect(currentTimeField,
							new ValueOperation(time)));
		}
		EAdConditionEvent expireEffectsEvent = new EAdConditionEventImpl( );
		expireEffectsEvent.addEffect(ConditionedEventType.CONDITIONS_MET, triggerExpiredEffects);
		expireEffectsEvent.setCondition(expireCondition);
		timer.getEvents().add(expireEffectsEvent);
	}

	private void addUpdateCurrentTime(EAdSceneElementEvent updater,
			EAdField<Integer> currentTimeField, EAdField<Boolean> runningField) {
		EAdChangeFieldValueEffect updateCurrentTime = new EAdChangeFieldValueEffect();
		updateCurrentTime.addField(currentTimeField);
		updateCurrentTime.setOperation(new MathOperation("[0] + [1]",
				currentTimeField, SystemFields.ELAPSED_TIME_PER_UPDATE));
		updateCurrentTime.setCondition(new OperationCondition(runningField));
		updater.addEffect(SceneElementEventType.ALWAYS, updateCurrentTime);
	}

	private void addAppearance(EAdBasicSceneElement timer, Timer oldTimer,
			EAdField<Integer> currentTimeField) {
		CaptionImpl text = new CaptionImpl();
		text.setFont(new EAdFontImpl(18));

		// FIXME if oldTimer.isCountDown(), use operations in captions
		stringHandler.setString(text.getLabel(), oldTimer.getDisplayName()
				+ " #0");
		text.getFields().add(currentTimeField);
		timer.getDefinition()
				.getResources()
				.addAsset(timer.getDefinition().getInitialBundle(),
						EAdSceneElementDefImpl.appearance, text);

	}
}

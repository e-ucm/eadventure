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

package ead.common.importer.subimporters.chapter;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EffectsImporterFactory;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.hud.ModifyHUDEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.VarDef;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.text.BasicFont;
import ead.common.util.StringHandler;
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;

public class TimerImporter implements EAdElementImporter<Timer, EAdEvent> {

	private static int ID = 0;

	/**
	 * Conditions importer
	 */
	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	private EffectsImporterFactory effectsImporter;

	private StringHandler stringHandler;

	private static final EAdVarDef<Integer> CURRENT_TIME_VAR = new VarDef<Integer>(
			"current_time_timer", Integer.class, 0);

	private static final EAdVarDef<Boolean> RUNNING_VAR = new VarDef<Boolean>(
			"stopped_timer", Boolean.class, false);

	private static final EAdVarDef<Boolean> NOT_TRIGGERED_VAR = new VarDef<Boolean>(
			"stopped_timer", Boolean.class, true);

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
		return new ConditionedEv();
	}

	@Override
	public EAdEvent convert(Timer oldTimer, Object object) {
		ConditionedEv event = (ConditionedEv) object;
		EAdCondition initCondition = conditionsImporter.init(oldTimer
				.getInitCond());
		initCondition = conditionsImporter.convert(oldTimer.getInitCond(),
				initCondition);
		EAdSceneElement timer = getSceneElementForTimer(oldTimer, initCondition);
		EAdField<Boolean> runningField = new BasicField<Boolean>(timer,
				RUNNING_VAR);
		EAdField<Boolean> notTriggeredField = new BasicField<Boolean>(timer,
				NOT_TRIGGERED_VAR);

		EAdField<Boolean> visibleField = new BasicField<Boolean>(timer,
				SceneElement.VAR_VISIBLE);
		ChangeFieldEf changeVisible = new ChangeFieldEf(
				visibleField, BooleanOp.TRUE_OP);
		EAdField<Integer> currentTimeField = new BasicField<Integer>(timer,
				CURRENT_TIME_VAR);

		ModifyHUDEf modifyHUD = new ModifyHUDEf(timer, true);

		ChangeFieldEf changeRunning = new ChangeFieldEf(
				runningField, new BooleanOp(
						EmptyCond.TRUE_EMPTY_CONDITION));

		event.addEffect(ConditionedEvType.CONDITIONS_MET, modifyHUD);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, changeVisible);
		event.addEffect(ConditionedEvType.CONDITIONS_MET,
				new ChangeFieldEf(currentTimeField,
						new ValueOp(0)));
		event.addEffect(ConditionedEvType.CONDITIONS_MET, changeRunning);
		event.addEffect(ConditionedEvType.CONDITIONS_MET,
				new ChangeFieldEf(notTriggeredField,
						BooleanOp.FALSE_OP));
		event.setRunNotMetConditions(false);
		event.setCondition(new ANDCond(initCondition,
				new OperationCond(notTriggeredField)));

		if (!oldTimer.isShowWhenStopped()) {
			ConditionedEv visibleEvent = new ConditionedEv();
			ChangeFieldEf changeVisibleFalse = new ChangeFieldEf(
					visibleField, BooleanOp.FALSE_OP);
			visibleEvent.setCondition(new ANDCond(new OperationCond(
					visibleField), new NOTCond(new OperationCond(
					runningField))));
			visibleEvent.addEffect(ConditionedEvType.CONDITIONS_MET,
					changeVisibleFalse);
			timer.getEvents().add(visibleEvent);
		}

		if (oldTimer.isMultipleStarts()) {
			ConditionedEv multipleStarts = new ConditionedEv();
			multipleStarts.setCondition(initCondition);
			multipleStarts.addEffect(ConditionedEvType.CONDITIONS_MET,
					changeRunning);
			multipleStarts.addEffect(ConditionedEvType.CONDITIONS_MET,
					changeVisible);
			multipleStarts.addEffect(ConditionedEvType.CONDITIONS_MET,
					new ChangeFieldEf(currentTimeField,
							new ValueOp(0)));
			timer.getEvents().add(multipleStarts);
		}

		return event;
	}

	private EAdSceneElement getSceneElementForTimer(Timer oldTimer,
			EAdCondition initCondition) {
		SceneElement timer = new SceneElement();
		timer.setVarInitialValue(SceneElement.VAR_ENABLE, false);
		timer.setId("timer" + ID++);

		EAdField<Integer> currentTimeField = new BasicField<Integer>(timer,
				CURRENT_TIME_VAR);
		EAdField<Boolean> runningField = new BasicField<Boolean>(timer,
				RUNNING_VAR);

		EAdField<Boolean> visibleField = new BasicField<Boolean>(timer,
				SceneElement.VAR_VISIBLE);

		timer.setVarInitialValue(SceneElement.VAR_VISIBLE, false);
		timer.setPosition(10, 20 * ID - 1 );

		// Appearance
		if (oldTimer.isShowTime())
			addAppearance(timer, oldTimer, currentTimeField);

		// Event to update the timer
		SceneElementEv updater = new SceneElementEv();
		timer.getEvents().add(updater);

		// Update current time
		addUpdateCurrentTime(updater, currentTimeField, runningField);

		// Check if time expired
		addEffectsTimeExpired(timer, updater, currentTimeField, visibleField,
				runningField, oldTimer);

		// Add effects when stopped
		addEffectsWhenStopped(timer, oldTimer, runningField, initCondition);

		return timer;
	}

	private void addEffectsWhenStopped(SceneElement timer,
			Timer oldTimer, EAdField<Boolean> runningField,
			EAdCondition initCondition) {
		EffectsMacro stopEffects = effectsImporter.getMacroEffects(oldTimer
				.getPostEffects());
		if (stopEffects != null) {
			EAdCondition stopCondition = null;
			if (oldTimer.isUsesEndCondition()) {
				stopCondition = conditionsImporter.init(oldTimer.getEndCond());
				stopCondition = conditionsImporter.convert(
						oldTimer.getEndCond(), stopCondition);
			} else {
				stopCondition = new NOTCond(initCondition);
			}
			stopCondition = new ANDCond(new OperationCond(
					runningField), stopCondition);

			TriggerMacroEf triggerStopEffects = new TriggerMacroEf();
			triggerStopEffects.putMacro(stopEffects,
					EmptyCond.TRUE_EMPTY_CONDITION);

			ConditionedEv stopEvent = new ConditionedEv();
			stopEvent.setCondition(stopCondition);
			stopEvent.addEffect(ConditionedEvType.CONDITIONS_MET,
					triggerStopEffects);
			stopEffects.getEffects().add(
					new ChangeFieldEf(runningField,
							BooleanOp.FALSE_OP));
			timer.getEvents().add(stopEvent);
		}
	}

	private void addEffectsTimeExpired(EAdSceneElement timer,
			SceneElementEv updater, EAdField<Integer> currentTimeField,
			EAdField<Boolean> visibleField, EAdField<Boolean> runningField,
			Timer oldTimer) {
		int time = oldTimer.getTime().intValue() * 1000;

		EffectsMacro expiredEffects = effectsImporter.getMacroEffects(oldTimer
				.getEffects());
		if (expiredEffects != null) {
			TriggerMacroEf triggerExpiredEffects = new TriggerMacroEf();
			triggerExpiredEffects.putMacro(expiredEffects,
					EmptyCond.TRUE_EMPTY_CONDITION);

			EAdCondition expireCondition = new OperationCond(currentTimeField, time,
					Comparator.GREATER_EQUAL);

			// Stop the timer if expires and set current time to its final value
			
			time = oldTimer.isRunsInLoop() ? 0 : time;
			expiredEffects.getEffects().add(
					new ChangeFieldEf(currentTimeField,
							new ValueOp(time)));
			
			if (!oldTimer.isRunsInLoop()) {
				expiredEffects.getEffects().add(
						new ChangeFieldEf(runningField,
								BooleanOp.FALSE_OP));
			}


			ConditionedEv expireEffectsEvent = new ConditionedEv();
			expireEffectsEvent.addEffect(ConditionedEvType.CONDITIONS_MET,
					triggerExpiredEffects);
			expireEffectsEvent.setCondition(expireCondition);

			timer.getEvents().add(expireEffectsEvent);
		}
	}

	private void addUpdateCurrentTime(SceneElementEv updater,
			EAdField<Integer> currentTimeField, EAdField<Boolean> runningField) {
		ChangeFieldEf updateCurrentTime = new ChangeFieldEf();
		updateCurrentTime.addField(currentTimeField);
		updateCurrentTime.setOperation(new MathOp("[0] + [1]",
				currentTimeField, SystemFields.ELAPSED_TIME_PER_UPDATE));
		updateCurrentTime.setCondition(new OperationCond(runningField));
		updater.addEffect(SceneElementEvType.ALWAYS, updateCurrentTime);
	}

	private void addAppearance(SceneElement timer, Timer oldTimer,
			EAdField<Integer> currentTimeField) {
		Caption text = new Caption();
		text.setFont(new BasicFont(18));

		MathOp timeOperation = null;
		if (oldTimer.isCountDown())
			timeOperation = new MathOp(oldTimer.getTime()
					+ " - ([0] / 1000)", currentTimeField);
		else
			timeOperation = new MathOp("[0] / 1000", currentTimeField);
		
		stringHandler.setString(text.getLabel(), oldTimer.getDisplayName()
				+ " [0]");
		text.getFields().add(timeOperation);
		timer.getDefinition()
				.getResources()
				.addAsset(timer.getDefinition().getInitialBundle(),
						SceneElementDef.appearance, text);

	}
}

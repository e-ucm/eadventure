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

package ead.importer.subimporters.chapter;

import com.google.inject.Inject;

import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.conditions.enums.Comparator;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.effects.hud.ModifyHUDEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.ConditionedEv;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.ConditionedEvType;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.model.params.variables.VarDef;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EffectsImporterFactory;
import es.eucm.ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;

public class TimerImporter implements EAdElementImporter<Timer, Event> {

	private static int count = 0;

	/**
	 * Conditions importer
	 */
	private EAdElementImporter<Conditions, Condition> conditionsImporter;

	private EffectsImporterFactory effectsImporter;

	private StringHandler stringHandler;

	protected ImportAnnotator annotator;

	private static final EAdVarDef<Integer> CURRENT_TIME_VAR = new VarDef<Integer>(
			"current_time_timer", Integer.class, 0);

	private static final EAdVarDef<Boolean> RUNNING_VAR = new VarDef<Boolean>(
			"stopped_timer", Boolean.class, false);

	private static final EAdVarDef<Boolean> NOT_TRIGGERED_VAR = new VarDef<Boolean>(
			"stopped_timer", Boolean.class, true);

	@Inject
	public TimerImporter(
			EAdElementImporter<Conditions, Condition> conditionsImporter,
			EffectsImporterFactory effectsImporter,
			StringHandler stringHandler, ImportAnnotator annotator) {
		this.conditionsImporter = conditionsImporter;
		this.effectsImporter = effectsImporter;
		this.stringHandler = stringHandler;
		this.annotator = annotator;
	}

	@Override
	public Event init(Timer oldTimer) {
		return new ConditionedEv();
	}

	@Override
	public Event convert(Timer oldTimer, Object object) {
		ConditionedEv event = (ConditionedEv) object;
		Condition initCondition = conditionsImporter.init(oldTimer
				.getInitCond());
		initCondition = conditionsImporter.convert(oldTimer.getInitCond(),
				initCondition);
		SceneElement timer = getSceneElementForTimer(oldTimer, initCondition);
		ElementField<Boolean> runningField = new ElementField<Boolean>(timer,
				RUNNING_VAR);
		ElementField<Boolean> notTriggeredField = new ElementField<Boolean>(timer,
				NOT_TRIGGERED_VAR);

		ElementField<Boolean> visibleField = new ElementField<Boolean>(timer,
				SceneElement.VAR_VISIBLE);
		ChangeFieldEf changeVisible = new ChangeFieldEf(visibleField,
				EmptyCond.TRUE);
		ElementField<Integer> currentTimeField = new ElementField<Integer>(timer,
				CURRENT_TIME_VAR);

		ModifyHUDEf modifyHUD = new ModifyHUDEf(timer, true);

		ChangeFieldEf changeRunning = new ChangeFieldEf(runningField,
				EmptyCond.TRUE);

		event.addEffect(ConditionedEvType.CONDITIONS_MET, modifyHUD);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, changeVisible);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, new ChangeFieldEf(
				currentTimeField, new ValueOp(0)));
		event.addEffect(ConditionedEvType.CONDITIONS_MET, changeRunning);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, new ChangeFieldEf(
				notTriggeredField, EmptyCond.FALSE));
		event.setRunNotMetConditions(false);
		event.setCondition(new ANDCond(initCondition, new OperationCond(
				notTriggeredField)));

		if (!oldTimer.isShowWhenStopped()) {
			ConditionedEv visibleEvent = new ConditionedEv();
			ChangeFieldEf changeVisibleFalse = new ChangeFieldEf(visibleField,
					EmptyCond.FALSE);
			visibleEvent
					.setCondition(new ANDCond(new OperationCond(visibleField),
							new NOTCond(new OperationCond(runningField))));
			visibleEvent.addEffect(ConditionedEvType.CONDITIONS_MET,
					changeVisibleFalse);
			timer.addEvent(visibleEvent);
		}

		if (oldTimer.isMultipleStarts()) {
			ConditionedEv multipleStarts = new ConditionedEv();
			multipleStarts.setCondition(initCondition);
			multipleStarts.addEffect(ConditionedEvType.CONDITIONS_MET,
					changeRunning);
			multipleStarts.addEffect(ConditionedEvType.CONDITIONS_MET,
					changeVisible);
			multipleStarts.addEffect(ConditionedEvType.CONDITIONS_MET,
					new ChangeFieldEf(currentTimeField, new ValueOp(0)));
			timer.addEvent(multipleStarts);
		}

		return event;
	}

	private SceneElement getSceneElementForTimer(Timer oldTimer,
			Condition initCondition) {
		SceneElement timer = new SceneElement();
		timer.setVarInitialValue(SceneElement.VAR_ENABLE, false);

		ElementField<Integer> currentTimeField = new ElementField<Integer>(timer,
				CURRENT_TIME_VAR);
		ElementField<Boolean> runningField = new ElementField<Boolean>(timer,
				RUNNING_VAR);

		ElementField<Boolean> visibleField = new ElementField<Boolean>(timer,
				SceneElement.VAR_VISIBLE);

		timer.setVarInitialValue(SceneElement.VAR_VISIBLE, false);
		timer.setPosition(10, 20 * count - 1);

		// Appearance
		if (oldTimer.isShowTime()) {
			addAppearance(timer, oldTimer, currentTimeField);
		}

		// Event to update the timer
		SceneElementEv updater = new SceneElementEv();
		timer.addEvent(updater);

		// Update current time
		addUpdateCurrentTime(updater, currentTimeField, runningField);

		// Check if time expired
		addEffectsTimeExpired(timer, updater, currentTimeField, visibleField,
				runningField, oldTimer);

		// Add effects when stopped
		addEffectsWhenStopped(timer, oldTimer, runningField, initCondition);

		return timer;
	}

	private void addEffectsWhenStopped(SceneElement timer, Timer oldTimer,
			ElementField<Boolean> runningField, Condition initCondition) {
		EAdList<Effect> stopEffects = effectsImporter.getMacroEffects(oldTimer
				.getPostEffects());
		if (stopEffects != null) {
			Condition stopCondition = null;
			if (oldTimer.isUsesEndCondition()) {
				stopCondition = conditionsImporter.init(oldTimer.getEndCond());
				stopCondition = conditionsImporter.convert(oldTimer
						.getEndCond(), stopCondition);
			} else {
				stopCondition = new NOTCond(initCondition);
			}
			stopCondition = new ANDCond(new OperationCond(runningField),
					stopCondition);

			TriggerMacroEf triggerStopEffects = new TriggerMacroEf();
			triggerStopEffects.putEffects(EmptyCond.TRUE, stopEffects);

			ConditionedEv stopEvent = new ConditionedEv();
			stopEvent.setCondition(stopCondition);
			stopEvent.addEffect(ConditionedEvType.CONDITIONS_MET,
					triggerStopEffects);
			stopEffects.add(new ChangeFieldEf(runningField, EmptyCond.FALSE));
			timer.addEvent(stopEvent);
		}
	}

	private void addEffectsTimeExpired(SceneElement timer,
			SceneElementEv updater, ElementField<Integer> currentTimeField,
			ElementField<Boolean> visibleField, ElementField<Boolean> runningField,
			Timer oldTimer) {
		int time = oldTimer.getTime().intValue() * 1000;

		EAdList<Effect> expiredEffects = effectsImporter
				.getMacroEffects(oldTimer.getEffects());
		if (expiredEffects != null) {
			TriggerMacroEf triggerExpiredEffects = new TriggerMacroEf();
			triggerExpiredEffects.putEffects(EmptyCond.TRUE, expiredEffects);

			Condition expireCondition = new OperationCond(currentTimeField,
					time, Comparator.GREATER_EQUAL);

			// Stop the timer if expires and set current time to its final value

			time = oldTimer.isRunsInLoop() ? 0 : time;
			expiredEffects.add(new ChangeFieldEf(currentTimeField, new ValueOp(
					time)));

			if (!oldTimer.isRunsInLoop()) {
				expiredEffects.add(new ChangeFieldEf(runningField,
						EmptyCond.FALSE));
			}

			ConditionedEv expireEffectsEvent = new ConditionedEv();
			expireEffectsEvent.addEffect(ConditionedEvType.CONDITIONS_MET,
					triggerExpiredEffects);
			expireEffectsEvent.setCondition(expireCondition);

			timer.addEvent(expireEffectsEvent);
		}
	}

	private void addUpdateCurrentTime(SceneElementEv updater,
			ElementField<Integer> currentTimeField, ElementField<Boolean> runningField) {
		ChangeFieldEf updateCurrentTime = new ChangeFieldEf();
		updateCurrentTime.addField(currentTimeField);
		updateCurrentTime.setOperation(new MathOp("[0] + [1]",
				currentTimeField, SystemFields.ELAPSED_TIME_PER_UPDATE));
		updateCurrentTime.setCondition(new OperationCond(runningField));
		updater.addEffect(SceneElementEvType.ALWAYS, updateCurrentTime);
	}

	private void addAppearance(SceneElement timer, Timer oldTimer,
			ElementField<Integer> currentTimeField) {
		Caption text = new Caption(stringHandler.generateNewString());
		text.setFont(BasicFont.BIG);

		MathOp timeOperation = null;
		if (oldTimer.isCountDown())
			timeOperation = new MathOp(oldTimer.getTime() + " - ([0] / 1000)",
					currentTimeField);
		else
			timeOperation = new MathOp("[0] / 1000", currentTimeField);

		stringHandler.setString(text.getLabel(), oldTimer.getDisplayName()
				+ " [0]");
		text.getOperations().add(timeOperation);
		timer.getDefinition().addAsset(SceneElementDef.appearance, text);

	}
}

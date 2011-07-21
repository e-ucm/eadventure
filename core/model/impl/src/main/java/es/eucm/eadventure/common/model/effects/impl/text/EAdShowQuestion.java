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

package es.eucm.eadventure.common.model.effects.impl.text;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.impl.EAdListImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.EAdPosition.Corner;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;

/**
 * <p>
 * Effect ShowQuestion
 * </p>
 * 
 */
@Element(runtime = EAdComplexBlockingEffect.class, detailed = EAdShowQuestion.class)
public class EAdShowQuestion extends EAdComplexBlockingEffect {

	/**
	 * Used to generate unique variables
	 */
	private static int ID_GENERATOR = 0;

	/**
	 * List of {@link Answer}s to the question
	 */
	private EAdList<Answer> answers;

	@Param("questionElement")
	private EAdSceneElement questionElement;

	@Param("marginLeft")
	private int marginLeft = 10;

	private EAdVar<Boolean> answered;

	private IntegerVar selectedAnswer;

	public EAdShowQuestion(String id) {
		super(id);
		answers = new EAdListImpl<Answer>(Answer.class);
	}

	public EAdShowQuestion() {
		this("showQuestionEffect");
	}

	/**
	 * Returns the {@link Answer}s to the question
	 * 
	 * @return the {@link Answer}s to the question
	 */
	public EAdList<Answer> getAnswers() {
		return answers;
	}

	public void setUpNewInstance() {
		components.clear();

		// Vars
		selectedAnswer = new IntegerVar(id + "_selectedAnswer" + ID_GENERATOR++);
		answered = new BooleanVar(id + "_answerd" + ID_GENERATOR++);
		answered.setInitialValue(false);
		selectedAnswer.setInitialValue(-1);

		// Effects
		EAdChangeVarValueEffect invisibleEffect = new EAdChangeVarValueEffect(
				"questionInvisibleEffect");
		invisibleEffect.setOperation(BooleanOperation.FALSE_OP);

		EAdChangeVarValueEffect visibleEffect = new EAdChangeVarValueEffect(
				"questionVisibleEffect");
		visibleEffect.setOperation(BooleanOperation.TRUE_OP);

		if (questionElement != null) {
			((EAdBasicSceneElement) questionElement).setClone(true);
			((EAdBasicSceneElement) questionElement).setPosition(new EAdPosition( Corner.TOP_LEFT, marginLeft, 10 ));
			components.add(questionElement);

			EAdVar<Boolean> qEvisibleVar = questionElement.getVars().getVar(
					EAdSceneElementVars.VAR_VISIBLE);

			qEvisibleVar.setInitialValue(false);
			invisibleEffect.addVar(qEvisibleVar);
			visibleEffect.addVar(qEvisibleVar);
			questionElement.getVars().add(answered);
			questionElement.getVars().add(selectedAnswer);
		}

		for (Answer a : answers) {
			invisibleEffect.addVar(a.getVars().getVar(
					EAdSceneElementVars.VAR_VISIBLE));
			visibleEffect.addVar(a.getVars().getVar(
					EAdSceneElementVars.VAR_VISIBLE));
		}

		// Start macro
		EAdMacro startMacro = new EAdMacroImpl("startQuestionMacro");
		startMacro.getEffects().add(invisibleEffect);
		addPositioningEvent(startMacro);
		startMacro.getEffects().add(visibleEffect);

		// Reset answered
		EAdEffect resetAnswered = new EAdChangeVarValueEffect(
				"questionInitAnswered", answered, BooleanOperation.FALSE_OP);
		this.getFinalEffects().add(resetAnswered);
		this.getFinalEffects().add(invisibleEffect);

		// Events
		EAdSceneElementEvent addedEvent = new EAdSceneElementEventImpl(
				"questionAddedEvent");

		EAdChangeVarValueEffect endEffect = new EAdChangeVarValueEffect(id
				+ "_endEffect", answered, BooleanOperation.TRUE_OP);

		int i = 0;
		for (Answer a : answers) {
			a.getVars().getVar(EAdSceneElementVars.VAR_VISIBLE)
					.setInitialValue(Boolean.FALSE);
			a.setClone(true);
			a.setUpNewInstance(selectedAnswer, endEffect, i++);
			a.setPosition(new EAdPosition(Corner.TOP_LEFT, 0, 0));
			components.add(a);
		}

		// TODO randomize answer order
		// TODO behavior to change selection with key presses?

		addedEvent.addEffect(SceneElementEvent.ADDED_TO_SCENE,
				new EAdTriggerMacro(startMacro));

		getEvents().add(addedEvent);
		this.setBlockingCondition(new NOTCondition(new FlagCondition(answered)));

	}

	private void addPositioningEvent(EAdMacro macro) {

		EAdChangeVarValueEffect initQuestion = new EAdChangeVarValueEffect(id
				+ "initializaingAnswred");
		initQuestion.addVar(answered);
		initQuestion.setOperation(new BooleanOperation("visibleAnswer",
				EmptyCondition.FALSE_EMPTY_CONDITION));

		macro.getEffects().add(initQuestion);

		for (int i = 0; i < answers.size(); i++) {
			EAdSceneElement previousElement = i == 0 ? questionElement
					: answers.get(i - 1);
			Answer a = answers.get(i);

			EAdChangeVarValueEffect effect = new EAdChangeVarValueEffect(id
					+ "positoningAnswer" + i);
			effect.addVar(a.getVars().getVar(
					EAdSceneElementVars.VAR_Y));
			if (previousElement != null) {
				effect.setOperation(new LiteralExpressionOperation(effect
						.getId() + "_op", "[0] + [1] + " + marginLeft,
						previousElement.getVars().getVar(
								EAdSceneElementVars.VAR_Y), previousElement
								.getVars().getVar(
										EAdSceneElementVars.VAR_HEIGHT)));
			} else
				effect.setOperation(new LiteralExpressionOperation(effect
						.getId() + "_op", "0 + " + marginLeft));

			macro.getEffects().add(effect);

			EAdVarInterpolationEffect interpolation = new EAdVarInterpolationEffect(
					"answer_interpolation",
					a.getVars().getVar(
							EAdSceneElementVars.VAR_X),
					new LiteralExpressionOperation("id", "-800"),
					new LiteralExpressionOperation("id", "" + (marginLeft * 2)),
					500, EAdVarInterpolationEffect.LoopType.NO_LOOP);
			macro.getEffects().add(interpolation);

		}

	}

	public void setQuestion(EAdSceneElement eAdSceneElement) {
		this.questionElement = eAdSceneElement;
	}

}

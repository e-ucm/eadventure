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
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;

/**
 * <p>
 * Effect ShowQuestion
 * </p>
 * 
 */
@Element(runtime = EAdShowQuestion.class, detailed = EAdShowQuestion.class)
public class EAdShowQuestion extends EAdComplexBlockingEffect {

	public static final EAdVarDef<Boolean> VAR_ANSWERED = new EAdVarDefImpl<Boolean>(
			"answered", Boolean.class, Boolean.FALSE);

	public static final EAdVarDef<Integer> VAR_SELECTED_ANSWER = new EAdVarDefImpl<Integer>(
			"selected_answer", Integer.class, -1);

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

	private EAdFieldImpl<Integer> selectedAnswer;

	private EAdFieldImpl<Boolean> answered;

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
		selectedAnswer = new EAdFieldImpl<Integer>(this, VAR_SELECTED_ANSWER);
		answered = new EAdFieldImpl<Boolean>(this, VAR_ANSWERED);

		// Effects
		EAdChangeFieldValueEffect invisibleEffect = new EAdChangeFieldValueEffect(
				"questionInvisibleEffect");
		invisibleEffect.setOperation(BooleanOperation.FALSE_OP);

		EAdChangeFieldValueEffect visibleEffect = new EAdChangeFieldValueEffect(
				"questionVisibleEffect");
		visibleEffect.setOperation(BooleanOperation.TRUE_OP);

		if (questionElement != null) {
			((EAdBasicSceneElement) questionElement).setClone(true);
			((EAdBasicSceneElement) questionElement)
					.setPosition(new EAdPositionImpl(Corner.TOP_LEFT,
							marginLeft, 10));
			components.add(questionElement);

			EAdField<Boolean> qEvisibleVar = new EAdFieldImpl<Boolean>(this,
					EAdBasicSceneElement.VAR_VISIBLE);

			//qEvisibleVar.setInitialValue(false);
			invisibleEffect.addVar(qEvisibleVar);
			visibleEffect.addVar(qEvisibleVar);
		}



		// Start macro
		EAdMacro startMacro = new EAdMacroImpl("startQuestionMacro");
		startMacro.getEffects().add(invisibleEffect);
		addPositioningEvent(startMacro);
		startMacro.getEffects().add(visibleEffect);

		// Reset answered
		EAdEffect resetAnswered = new EAdChangeFieldValueEffect(
				"questionInitAnswered", answered, BooleanOperation.FALSE_OP);
		this.getFinalEffects().add(resetAnswered);
		this.getFinalEffects().add(invisibleEffect);

		// Events
		EAdSceneElementEvent addedEvent = new EAdSceneElementEventImpl(
				"questionAddedEvent");

		EAdChangeFieldValueEffect endEffect = new EAdChangeFieldValueEffect(id
				+ "_endEffect", answered, BooleanOperation.TRUE_OP);

		int i = 0;
		for (Answer a : answers) {
//			a.getVars().getVar(EAdSceneElementVars.VAR_VISIBLE)
//					.setInitialValue(Boolean.FALSE);
			a.setClone(true);
			a.setUpNewInstance(selectedAnswer, endEffect, i++);
			a.setPosition(new EAdPositionImpl(Corner.TOP_LEFT, 0, 0));
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

		EAdChangeFieldValueEffect initQuestion = new EAdChangeFieldValueEffect(
				id + "initializaingAnswred");
		initQuestion.addVar(answered);
		initQuestion.setOperation(new BooleanOperation("visibleAnswer",
				EmptyCondition.FALSE_EMPTY_CONDITION));

		macro.getEffects().add(initQuestion);

		for (int i = 0; i < answers.size(); i++) {
			EAdSceneElement previousElement = i == 0 ? questionElement
					: answers.get(i - 1);
			Answer a = answers.get(i);

			EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect(id
					+ "positoningAnswer" + i);
//			effect.addVar(a.getVars().getVar(EAdSceneElementVars.VAR_Y));
			if (previousElement != null) {
//				effect.setOperation(new LiteralExpressionOperation(effect
//						.getId() + "_op", "[0] + [1] + " + marginLeft,
//						previousElement.getVars().getVar(
//								EAdSceneElementVars.VAR_Y), previousElement
//								.getVars().getVar(
//										EAdSceneElementVars.VAR_HEIGHT)));
			} else
				effect.setOperation(new LiteralExpressionOperation(effect
						.getId() + "_op", "0 + " + marginLeft));

			macro.getEffects().add(effect);

//			EAdVarInterpolationEffect interpolation = new EAdVarInterpolationEffect(
//					"answer_interpolation",
//					a.getVars().getVar(EAdSceneElementVars.VAR_X),
//					new LiteralExpressionOperation("id", "-800"),
//					new LiteralExpressionOperation("id", "" + (marginLeft * 2)),
//					500, EAdVarInterpolationEffect.LoopType.NO_LOOP);
//			macro.getEffects().add(interpolation);

		}

	}

	public void setQuestion(EAdSceneElement eAdSceneElement) {
		this.questionElement = eAdSceneElement;
	}

}

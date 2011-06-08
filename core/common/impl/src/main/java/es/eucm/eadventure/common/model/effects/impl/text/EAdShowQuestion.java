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

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.EAdPosition.Corner;
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
	private EAdElementList<Answer> answers;

	@Param("questionElement")
	private EAdSceneElement questionElement;

	public EAdShowQuestion(String id) {
		super(id);
		answers = new EAdElementListImpl<Answer>();
	}

	public EAdShowQuestion() {
		this("showQuestionEffect");
	}

	/**
	 * Returns the {@link Answer}s to the question
	 * 
	 * @return the {@link Answer}s to the question
	 */
	public EAdElementList<Answer> getAnswers() {
		return answers;
	}

	public void setUpNewInstance() {
		int marginLeft = 10;
		components.clear();
		
		if (questionElement != null){
			questionElement.getPosition().set(marginLeft, 10, Corner.TOP_LEFT);
			components.add(questionElement);
		}

		IntegerVar selectedAnswer = new IntegerVar(id + "_selectedAnswer" + ID_GENERATOR++ );
		BooleanVar answered = new BooleanVar(id + "_answerd" + ID_GENERATOR++ );

		EAdChangeVarValueEffect endEffect = new EAdChangeVarValueEffect(id
				+ "_endEffect");
		endEffect.setVar(answered);
		BooleanOperation booleanOperation = new BooleanOperation(id + "_endOp",
				EmptyCondition.TRUE_EMPTY_CONDITION);
		endEffect.setOperation(booleanOperation);

		for (int i = 0; i < answers.size(); i++) {
			answers.get(i).setUpNewInstance(selectedAnswer, endEffect, i);

			// TODO place answers in reasonable places, probably needs property
			// variables for height
			// TODO randomize answer order
			answers.get(i).setPosition(
					new EAdPosition(Corner.TOP_LEFT, marginLeft * 2, 0 ));

			components.add(answers.get(i));
		}

		// TODO behavior to change selection with key presses?

		this.setBlockingCondition(new NOTCondition(new FlagCondition(answered)));
		
		addPositioningEvent( );
	}
	
	private void addPositioningEvent( ){
		EAdSceneElementEvent event = new EAdSceneElementEventImpl( id + "AddToSceneEvent");
		
		for ( int i = 0; i < answers.size(); i++ ){
			EAdSceneElement previousElement = i == 0 ? questionElement : answers.get(i - 1 );
			Answer a = answers.get(i);
			EAdChangeVarValueEffect effect = new EAdChangeVarValueEffect( id + "positoningAnswer" + i);
			effect.setVar(a.positionYVar());
			effect.setOperation(new LiteralExpressionOperation(effect.getId() + "_op", "[0] + [1] + 10", previousElement.positionYVar(), previousElement.heightVar() ));
			event.addEffect(SceneElementEvent.ADDED_TO_SCENE, effect);
		}
		
		getEvents().add(event);
	}

	public void setQuestion(EAdBasicSceneElement questionElement) {
		this.questionElement = questionElement;
	}

}

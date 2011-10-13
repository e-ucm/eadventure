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

import java.util.Map.Entry;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;

/**
 * <p>
 * Effect ShowQuestion
 * </p>
 * 
 */
@Element(runtime = EAdShowQuestion.class, detailed = EAdShowQuestion.class)
public class EAdShowQuestion extends EAdComplexBlockingEffect {

	/**
	 * List of {@link Answer}s to the question
	 */
	@Param("answers")
	private EAdMap<EAdString, EAdEffect> answers;

	@Param("question")
	private final EAdString question;

	public EAdShowQuestion(String id) {
		super(id);
		answers = new EAdMapImpl<EAdString, EAdEffect>(EAdString.class,
				EAdEffect.class);
		question = EAdString.newEAdString("question");
	}

	public EAdShowQuestion() {
		this("showQuestionEffect");
	}

	public void addAnswer(EAdString string, EAdEffect effect) {
		answers.put(string, effect);
	}
	
	public EAdString getQuestion() {
		return question;
	}

	public void setUpNewInstance() {
		components.clear();

		EAdField<Boolean> effectFinished = new EAdFieldImpl<Boolean>(this,
				VAR_EFFECT_FINISHED);
		EAdChangeFieldValueEffect changeFinished = new EAdChangeFieldValueEffect(
				"endEffect");
		changeFinished.addField(effectFinished);
		changeFinished.setOperation(BooleanOperation.TRUE_OP);

		setUpQuestion();
		int i = 0;
		for (Entry<EAdString, EAdEffect> a : answers.entrySet()) {
			EAdBasicSceneElement se = new EAdBasicSceneElement("answer" + i);
			CaptionImpl c = new CaptionImpl(a.getKey());
			c.setTextPaint(EAdColor.RED);
			c.setBubblePaint(EAdColor.WHITE);
			se.getResources().addAsset(se.getInitialBundle(),
					EAdBasicSceneElement.appearance, c);
			int initialX = -200 * i - 820;
			se.setPosition(initialX, (i + 1) * 120);

			EAdVarInterpolationEffect effect = new EAdVarInterpolationEffect(
					"interpolationAnswer");
			effect.setInterpolation(new EAdFieldImpl<Integer>(se,
					EAdBasicSceneElement.VAR_X), initialX, 10, 500,
					LoopType.NO_LOOP, InterpolationType.LINEAR);
			
			effect.setDelay(200*i);

			EAdSceneElementEvent event = new EAdSceneElementEventImpl("event");
			event.addEffect(SceneElementEvent.ADDED_TO_SCENE, effect);

			se.getEvents().add(event);

			se.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, a.getValue());
			se.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, changeFinished);

			components.add(se);
			i++;
		}

	}

	public void setUpQuestion() {
		CaptionImpl c = new CaptionImpl(question);
		
		c.setTextPaint(EAdColor.BLUE);
		c.setBubblePaint(EAdColor.WHITE);

		EAdBasicSceneElement q = new EAdBasicSceneElement("question");
		q.getResources().addAsset(q.getInitialBundle(),
				EAdBasicSceneElement.appearance, c);
		q.setPosition(10, 10);
		q.setVarInitialValue(EAdBasicSceneElement.VAR_ALPHA, 0.0f);

		EAdVarInterpolationEffect effect = new EAdVarInterpolationEffect(
				"quesionInterpolation");
		effect.setInterpolation(new EAdFieldImpl<Float>(q,
				EAdBasicSceneElement.VAR_ALPHA), 0.0f, 1.0f, 500,
				LoopType.NO_LOOP, InterpolationType.LINEAR);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl(
				"questionEvent");
		event.addEffect(SceneElementEvent.ADDED_TO_SCENE, effect);

		q.getEvents().add(event);

		components.add(q);
	}

}

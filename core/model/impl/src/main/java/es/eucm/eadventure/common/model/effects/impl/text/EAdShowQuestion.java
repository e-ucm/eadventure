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
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.text.EAdFont;
import es.eucm.eadventure.common.params.text.EAdString;
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
	 * System field to control questions
	 */
	private static final EAdField<Boolean> ANSWER_SELECTED = new EAdFieldImpl<Boolean>(
			null, new EAdVarDefImpl<Boolean>("EAdShowQuestion_answer_selected",
					Boolean.class, false));

	@Param("answers")
	private EAdMap<EAdString, EAdEffect> answers;

	@Param("question")
	private EAdString question;

	private int y;

	public EAdShowQuestion() {
		super();
		setId("showQuestionEffect");
		answers = new EAdMapImpl<EAdString, EAdEffect>(EAdString.class,
				EAdEffect.class);
		setOpaque(true);
	}

	public void addAnswer(EAdString string, EAdEffect effect) {
		answers.put(string, effect);
	}

	public EAdString getQuestion() {
		if (question == null) {
			question = EAdString.newEAdString("question");
		}
		return question;
	}

	public void setUpNewInstance() {
		EAdChangeFieldValueEffect initEffect = new EAdChangeFieldValueEffect();
		initEffect.addField(ANSWER_SELECTED);
		initEffect.setOperation(BooleanOperation.FALSE_OP);
		getInitEffects().add(initEffect);

		endCondition = new OperationCondition(ANSWER_SELECTED);

		EAdChangeFieldValueEffect selectEffect = new EAdChangeFieldValueEffect();
		selectEffect.addField(ANSWER_SELECTED);
		selectEffect.setOperation(BooleanOperation.TRUE_OP);

		components.clear();

		y = 10;

		EAdChangeFieldValueEffect inEffect = new EAdChangeFieldValueEffect();
		inEffect.setVarDef(EAdBasicSceneElement.VAR_ALPHA);
		inEffect.setOperation(new ValueOperation(0.5f));

		EAdChangeFieldValueEffect outEffect = new EAdChangeFieldValueEffect();
		outEffect.setVarDef(EAdBasicSceneElement.VAR_ALPHA);
		outEffect.setOperation(new ValueOperation(1.0f));

		if (question != null) {
			setUpQuestion();
		}

		int i = 0;
		for (Entry<EAdString, EAdEffect> entry : answers.entrySet()) {
			setUpAnswer(i++, entry, selectEffect, inEffect, outEffect);
		}

	}

	private void setUpQuestion() {
		int fontSize = 20;
		int padding = 8;
		EAdFont font = new EAdFontImpl(fontSize);
		CaptionImpl caption = new CaptionImpl(question);
		caption.setFont(font);
		caption.setPadding(padding);
		caption.setBubblePaint(EAdPaintImpl.BLACK_ON_WHITE);
		EAdBasicSceneElement questionElement = new EAdBasicSceneElement(caption);
		questionElement.setPosition(10, y);

		questionElement
				.setVarInitialValue(EAdBasicSceneElement.VAR_ALPHA, 0.0f);
		EAdChangeFieldValueEffect setAlpha = new EAdChangeFieldValueEffect();
		setAlpha.setOperation(new ValueOperation(0.0f));
		setAlpha.addField(new EAdFieldImpl<Float>(questionElement,
				EAdBasicSceneElement.VAR_ALPHA));
		getInitEffects().add(setAlpha);

		components.add(questionElement);
		EAdSceneElementEvent event = new EAdSceneElementEventImpl();

		EAdInterpolationEffect interpolation = new EAdInterpolationEffect(
				questionElement, EAdBasicSceneElement.VAR_ALPHA, 0, 1.0f, 500);
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, interpolation);

		questionElement.getEvents().add(event);

		y += fontSize * 2 + padding * 2;
	}

	private void setUpAnswer(int pos, Entry<EAdString, EAdEffect> entry,
			EAdEffect selectEffect, EAdEffect inEffect, EAdEffect outEffect) {
		int fontSize = 18;
		int padding = 5;
		EAdFont font = new EAdFontImpl(fontSize);
		CaptionImpl caption = new CaptionImpl(entry.getKey());
		caption.setFont(font);
		caption.setPadding(padding);
		caption.setBubblePaint(EAdColor.WHITE);
		EAdBasicSceneElement answerElement = new EAdBasicSceneElement(caption);
		answerElement.setPosition(-800, y);
		answerElement.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				selectEffect);
		answerElement.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				entry.getValue());

		answerElement.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, inEffect);
		answerElement.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, outEffect);
		y += fontSize * 2 + padding * 2;

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();

		EAdInterpolationEffect interpolation = new EAdInterpolationEffect(
				answerElement, EAdBasicSceneElement.VAR_X, 0, 820, 400, 500 + pos * 100,
				InterpolationLoopType.NO_LOOP, 1,
				InterpolationType.LINEAR);
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, interpolation);
		
		answerElement.getEvents().add(event);

		components.add(answerElement);
	}

	public EAdMap<EAdString, EAdEffect> getAnswers() {
		return answers;
	}

	public void setQuestion(EAdString question) {
		this.question = question;
	}
	
	

}

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

package ead.common.model.elements.effects.text;

import java.util.Map.Entry;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.ComplexBlockingEffect;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.extra.EAdMapImpl;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.VarDef;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;

/**
 * <p>
 * Effect ShowQuestion
 * </p>
 * 
 */
@Element(runtime = ShowQuestionEf.class, detailed = ShowQuestionEf.class)
public class ShowQuestionEf extends ComplexBlockingEffect {

	/**
	 * System field to control questions
	 */
	private static final EAdField<Boolean> ANSWER_SELECTED = new BasicField<Boolean>(
			null, new VarDef<Boolean>("EAdShowQuestion_answer_selected",
					Boolean.class, false));

	@Param("answers")
	private EAdMap<EAdString, EAdEffect> answers;

	@Param("question")
	private EAdString question;

	private int y;

	public ShowQuestionEf() {
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
			question = EAdString.newRandomEAdString("question");
		}
		return question;
	}

	public void setUpNewInstance() {
		ChangeFieldEf initEffect = new ChangeFieldEf();
		initEffect.addField(ANSWER_SELECTED);
		initEffect.setOperation(BooleanOp.FALSE_OP);
		getInitEffects().add(initEffect);

		endCondition = new OperationCond(ANSWER_SELECTED);

		ChangeFieldEf selectEffect = new ChangeFieldEf();
		selectEffect.addField(ANSWER_SELECTED);
		selectEffect.setOperation(BooleanOp.TRUE_OP);

		components.clear();

		y = 10;

		ChangeFieldEf inEffect = new ChangeFieldEf();
		inEffect.setVarDef(SceneElement.VAR_ALPHA);
		inEffect.setOperation(new ValueOp(0.5f));

		ChangeFieldEf outEffect = new ChangeFieldEf();
		outEffect.setVarDef(SceneElement.VAR_ALPHA);
		outEffect.setOperation(new ValueOp(1.0f));

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
		EAdFont font = new BasicFont(fontSize);
		Caption caption = new Caption(question);
		caption.setFont(font);
		caption.setPadding(padding);
		caption.setBubblePaint(Paint.BLACK_ON_WHITE);
		SceneElement questionElement = new SceneElement(caption);
		questionElement.setPosition(10, y);

		questionElement
				.setVarInitialValue(SceneElement.VAR_ALPHA, 0.0f);
		ChangeFieldEf setAlpha = new ChangeFieldEf();
		setAlpha.setOperation(new ValueOp(0.0f));
		setAlpha.addField(new BasicField<Float>(questionElement,
				SceneElement.VAR_ALPHA));
		getInitEffects().add(setAlpha);

		components.add(questionElement);
		SceneElementEv event = new SceneElementEv();

		InterpolationEf interpolation = new InterpolationEf(
				questionElement, SceneElement.VAR_ALPHA, 0, 1.0f, 500);
		event.addEffect(SceneElementEvType.FIRST_UPDATE, interpolation);

		questionElement.getEvents().add(event);

		y += fontSize * 2 + padding * 2;
	}

	private void setUpAnswer(int pos, Entry<EAdString, EAdEffect> entry,
			EAdEffect selectEffect, EAdEffect inEffect, EAdEffect outEffect) {
		int fontSize = 18;
		int padding = 5;
		EAdFont font = new BasicFont(fontSize);
		Caption caption = new Caption(entry.getKey());
		caption.setFont(font);
		caption.setPadding(padding);
		caption.setBubblePaint(ColorFill.WHITE);
		SceneElement answerElement = new SceneElement(caption);
		answerElement.setPosition(-800, y);
		answerElement.addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
				selectEffect);
		answerElement.addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
				entry.getValue());

		answerElement.addBehavior(MouseGEv.MOUSE_ENTERED, inEffect);
		answerElement.addBehavior(MouseGEv.MOUSE_EXITED, outEffect);
		y += fontSize * 2 + padding * 2;

		SceneElementEv event = new SceneElementEv();

		InterpolationEf interpolation = new InterpolationEf(
				answerElement, SceneElement.VAR_X, 0, 820, 400, 500 + pos * 100,
				InterpolationLoopType.NO_LOOP, 1,
				InterpolationType.LINEAR);
		event.addEffect(SceneElementEvType.FIRST_UPDATE, interpolation);
		
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

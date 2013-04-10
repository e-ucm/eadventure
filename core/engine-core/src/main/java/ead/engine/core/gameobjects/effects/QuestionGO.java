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

package ead.engine.core.gameobjects.effects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import com.google.inject.Inject;

import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.enums.Alignment;
import ead.common.model.assets.text.BasicFont;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.RemoveEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.text.QuestionEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.text.EAdString;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;

public class QuestionGO extends AbstractEffectGO<QuestionEf> implements
		Comparator<Object> {

	private SceneElementGOFactory sceneElementFactory;

	private GUI gui;

	private int y;

	private static final Random r = new Random(System.currentTimeMillis());

	private ArrayList<EAdString> answersToAdd;

	private ArrayList<EAdString> answersOrdered;

	@Inject
	public QuestionGO(GameState gameState, GUI gui,
			SceneElementGOFactory sceneElementFactory) {
		super(gameState);
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;
		answersToAdd = new ArrayList<EAdString>();
		answersOrdered = new ArrayList<EAdString>();
	}

	public void initialize() {
		super.initialize();
		y = 0;
		SceneElement question = setUpNewInstance();
		gui.getHUD(GUI.EFFECTS_HUD_ID).addSceneElement(
				sceneElementFactory.get(question));
	}

	public SceneElement setUpNewInstance() {
		GroupElement question = new GroupElement();
		GhostElement bg = new GhostElement();
		bg.setCatchAll(true);
		question.getSceneElements().add(bg);

		y = 10;

		ChangeFieldEf inEffect = new ChangeFieldEf();
		inEffect.setVarDef(SceneElement.VAR_ALPHA);
		inEffect.setOperation(new ValueOp(0.5f));

		ChangeFieldEf outEffect = new ChangeFieldEf();
		outEffect.setVarDef(SceneElement.VAR_ALPHA);
		outEffect.setOperation(new ValueOp(1.0f));

		setUpQuestion(question);

		RemoveEf selectEffect = new RemoveEf();
		selectEffect.setElement(question);

		// Order answers
		answersOrdered.clear();
		answersToAdd.clear();

		answersToAdd.addAll(effect.getAnswers().keySet());

		if (effect.isRandomAnswers()) {
			while (answersToAdd.size() > 0) {
				answersOrdered.add(answersToAdd.remove(r.nextInt(answersToAdd
						.size())));
			}
		} else {
			answersOrdered.addAll(answersToAdd);
		}

		int i = 0;
		for (EAdString s : answersOrdered) {
			EAdEffect e = effect.getAnswers().get(s);
			setUpAnswer(question, i++, s, e, selectEffect, inEffect, outEffect);
		}
		return question;

	}

	private void setUpQuestion(GroupElement root) {
		int fontSize = 20;
		int padding = 8;
		EAdFont font = new BasicFont(fontSize);
		Caption caption = new Caption(effect.getQuestion());
		caption.setFont(font);
		caption.setPadding(padding);
		caption.setBubblePaint(Paint.BLACK_ON_WHITE);
		caption.setPreferredWidth(750);
		SceneElement questionElement = new SceneElement(caption);
		questionElement.setPosition(10, y);

		questionElement.setVarInitialValue(SceneElement.VAR_ALPHA, 0.0f);
		root.getSceneElements().add(questionElement);
		SceneElementEv event = new SceneElementEv();

		InterpolationEf interpolation = new InterpolationEf(questionElement,
				SceneElement.VAR_ALPHA, 0, 1.0f, 500);
		event.addEffect(SceneElementEvType.INIT, interpolation);

		questionElement.getEvents().add(event);

		y += fontSize * 2 + padding * 2;
	}

	private void setUpAnswer(GroupElement question, int pos, EAdString key,
			EAdEffect value, EAdEffect selectEffect, EAdEffect inEffect,
			EAdEffect outEffect) {
		int fontSize = 18;
		int padding = 5;
		EAdFont font = new BasicFont(fontSize);
		Caption caption = new Caption(key);
		caption.setFont(font);
		caption.setPadding(padding);
		caption.setBubblePaint(Paint.BLACK_ON_WHITE);
		caption.setPreferredWidth(700);
		caption.setAlignment(Alignment.LEFT);
		SceneElement answerElement = new SceneElement(caption);
		answerElement.setPosition(-800, y);
		answerElement.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, selectEffect);
		answerElement.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, value);

		answerElement.addBehavior(MouseGEv.MOUSE_ENTERED, inEffect);
		answerElement.addBehavior(MouseGEv.MOUSE_EXITED, outEffect);
		y += fontSize * 2.5f + padding * 2.5f;

		SceneElementEv event = new SceneElementEv();

		InterpolationEf interpolation = new InterpolationEf(answerElement,
				SceneElement.VAR_X, 0, 820, 400, 500 + pos * 100,
				InterpolationLoopType.NO_LOOP, 1, InterpolationType.LINEAR);
		event.addEffect(SceneElementEvType.INIT, interpolation);

		answerElement.getEvents().add(event);

		question.getSceneElements().add(answerElement);
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		return Math.random() > 0.5f ? 1 : -1;
	}

}

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

import com.google.inject.Inject;
import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.enums.Alignment;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.RemoveEf;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.effects.text.QuestionEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.scenes.GhostElement;
import es.eucm.ead.model.elements.scenes.GroupElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.text.EAdString;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class QuestionGO extends AbstractEffectGO<QuestionEf> implements
		Comparator<Object> {

	private SceneElementGOFactory sceneElementFactory;

	private GUI gui;

	private int y;

	private static final Random r = new Random(System.currentTimeMillis());

	private ArrayList<EAdString> answers;

	private ArrayList<EAdList<EAdEffect>> effects;

	@Inject
	public QuestionGO(GameState gameState, GUI gui,
			SceneElementGOFactory sceneElementFactory) {
		super(gameState);
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;
		answers = new ArrayList<EAdString>();
		effects = new ArrayList<EAdList<EAdEffect>>();
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
		effects.clear();
		answers.clear();

		answers.addAll(effect.getAnswers());
		effects.addAll(effect.getEffects());

		int i = 0;
		while (answers.size() > 0) {
			int index = (effect.isRandomAnswers() ? r.nextInt(answers.size())
					: 0);
			EAdString s = answers.remove(index);
			EAdList<EAdEffect> e = effects.remove(index);
			setUpAnswer(question, i++, s, e, selectEffect, inEffect, outEffect);
		}
		return question;

	}

	private void setUpQuestion(GroupElement root) {
		if (effect.getQuestion() != null) {
			int fontSize = 20;
			int padding = 8;
			EAdFont font = BasicFont.REGULAR;
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

			InterpolationEf interpolation = new InterpolationEf(
					questionElement, SceneElement.VAR_ALPHA, 0, 1.0f, 500);
			event.addEffect(SceneElementEvType.INIT, interpolation);

			questionElement.getEvents().add(event);

			y += fontSize * 2 + padding * 2;
		}
	}

	private void setUpAnswer(GroupElement question, int pos, EAdString key,
			EAdList<EAdEffect> value, EAdEffect selectEffect,
			EAdEffect inEffect, EAdEffect outEffect) {
		int delay = effect.getQuestion() != null ? 500 : 0;
		int fontSize = 18;
		int padding = 5;
		EAdFont font = BasicFont.REGULAR;
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
				SceneElement.VAR_X, 0, 820, 400, delay + pos * 100,
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

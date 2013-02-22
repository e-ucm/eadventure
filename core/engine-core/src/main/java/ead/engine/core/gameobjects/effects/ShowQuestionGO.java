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

import java.util.Map.Entry;

import com.google.inject.Inject;

import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.text.BasicFont;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.RemoveEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.text.ShowQuestionEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.text.EAdString;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;

public class ShowQuestionGO extends AbstractEffectGO<ShowQuestionEf> {

	private SceneElementGOFactory sceneElementFactory;

	private GUI gui;

	private int y;

	@Inject
	public ShowQuestionGO(GameState gameState, GUI gui,
			SceneElementGOFactory sceneElementFactory) {
		super(gameState);
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;
	}

	public void initialize() {
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

		int i = 0;
		for (Entry<EAdString, EAdEffect> entry : effect.getAnswers().entrySet()) {
			setUpAnswer(question, i++, entry, selectEffect, inEffect, outEffect);
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
		SceneElement questionElement = new SceneElement(caption);
		questionElement.setPosition(10, y);

		questionElement.setVarInitialValue(SceneElement.VAR_ALPHA, 0.0f);
		root.getSceneElements().add(questionElement);
		SceneElementEv event = new SceneElementEv();

		InterpolationEf interpolation = new InterpolationEf(questionElement,
				SceneElement.VAR_ALPHA, 0, 1.0f, 500);
		event.addEffect(SceneElementEvType.FIRST_UPDATE, interpolation);

		questionElement.getEvents().add(event);

		y += fontSize * 2 + padding * 2;
	}

	private void setUpAnswer(GroupElement question, int pos,
			Entry<EAdString, EAdEffect> entry, EAdEffect selectEffect,
			EAdEffect inEffect, EAdEffect outEffect) {
		int fontSize = 18;
		int padding = 5;
		EAdFont font = new BasicFont(fontSize);
		Caption caption = new Caption(entry.getKey());
		caption.setFont(font);
		caption.setPadding(padding);
		caption.setBubblePaint(ColorFill.WHITE);
		SceneElement answerElement = new SceneElement(caption);
		answerElement.setPosition(-800, y);
		answerElement.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, selectEffect);
		answerElement.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, entry.getValue());

		answerElement.addBehavior(MouseGEv.MOUSE_ENTERED, inEffect);
		answerElement.addBehavior(MouseGEv.MOUSE_EXITED, outEffect);
		y += fontSize * 2 + padding * 2;

		SceneElementEv event = new SceneElementEv();

		InterpolationEf interpolation = new InterpolationEf(answerElement,
				SceneElement.VAR_X, 0, 820, 400, 500 + pos * 100,
				InterpolationLoopType.NO_LOOP, 1, InterpolationType.LINEAR);
		event.addEffect(SceneElementEvType.FIRST_UPDATE, interpolation);

		answerElement.getEvents().add(event);

		question.getSceneElements().add(answerElement);
	}

}

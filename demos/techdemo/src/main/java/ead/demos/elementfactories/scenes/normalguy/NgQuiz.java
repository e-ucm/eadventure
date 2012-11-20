package ead.demos.elementfactories.scenes.normalguy;

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

import ead.common.model.elements.effects.text.ShowQuestionEf;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.StringFactory;
import ead.demos.elementfactories.scenes.scenes.EmptyScene;

public class NgQuiz extends EmptyScene {

	public NgQuiz() {
		setBackground(new SceneElement(new Image("@drawable/ng_quiz_bg.png")));

		SceneElement element = createQuestions();

		getSceneElements().add(element);

		//ChangeSceneEf changeScene = new ChangeSceneEf( );
		//changeScene.setId("corridorScene");
		//changeScene.setNextScene(NgSceneCreator.getRoom2());

	}

	private SceneElement createQuestions() {
		String intro = "You must answer correctly to these questions to get out of the house!";

		SceneElement element = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(intro, 10, 10);
		element.setPosition(new EAdPosition(Corner.CENTER, 400, 575));

		String q = "Question 1";
		String a1 = "Answer 1 true";
		String a2 = "Answer 2 false";
		String a3 = "Answer 3 false";

		StringFactory stringFactory = EAdElementsFactory.getInstance()
				.getStringFactory();

		ShowQuestionEf effect = new ShowQuestionEf();
		stringFactory.setString(effect.getQuestion(), q);

		effect.addAnswer(stringFactory.getString(a1), getAnswer1(true,
				stringFactory));
		effect.addAnswer(stringFactory.getString(a2), getAnswer1(false,
				stringFactory));
		effect.addAnswer(stringFactory.getString(a3), getAnswer1(false,
				stringFactory));

		effect.setUpNewInstance();

		element.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, effect);
		return element;
	}

	private ShowQuestionEf getAnswer1(boolean isCorrect,
			StringFactory stringFactory) {
		String questionOk = "Okay... that was easy but... let'see this one! pregunta número dos";
		String questionWr = "Hehehe... I'm afraid you are wrong!!! hahaha! veamos la segunda";
		String b1 = "Answer 1 false";
		String b2 = "Answer 2 true";
		String b3 = "Answer 3 false";

		ShowQuestionEf effect = new ShowQuestionEf();
		if (isCorrect) {
			stringFactory.setString(effect.getQuestion(), questionOk);
		} else {
			stringFactory.setString(effect.getQuestion(), questionWr);
		}

		effect.addAnswer(stringFactory.getString(b1), getAnswer2(false,
				stringFactory));
		effect.addAnswer(stringFactory.getString(b2), getAnswer2(true,
				stringFactory));
		effect.addAnswer(stringFactory.getString(b3), getAnswer2(false,
				stringFactory));

		effect.setUpNewInstance();

		return effect;
	}

	private ShowQuestionEf getAnswer2(boolean isCorrect,
			StringFactory stringFactory) {
		String questionOk = "Well... I think you are kinda clever but this one will be your end! pregunta número tres";
		String questionWr = "Hehehe... I'm afraid you are wrong!!! hahaha! veamos la tercera";
		String b1 = "Answer 1 false";
		String b2 = "Anser 2 false";
		String b3 = "Anser 3 true";

		ShowQuestionEf effect = new ShowQuestionEf();
		if (isCorrect) {
			stringFactory.setString(effect.getQuestion(), questionOk);
		} else {
			stringFactory.setString(effect.getQuestion(), questionWr);
		}

		effect.addAnswer(stringFactory.getString(b1), getAnswer3andClose(false,
				stringFactory));
		effect.addAnswer(stringFactory.getString(b2), getAnswer3andClose(false,
				stringFactory));
		effect.addAnswer(stringFactory.getString(b3), getAnswer3andClose(true,
				stringFactory));

		effect.setUpNewInstance();

		return effect;
	}

	private ShowQuestionEf getAnswer3andClose(boolean result,
			StringFactory stringFactory) {
		String questionOk = "Well... I think you are kinda clever but this one will be your end! pregunta número tres";
		String questionWr = "Hehehe... I'm afraid you are wrong!!! hahaha! veamos la tercera";
		String finalQ = "Close quiz? or another way to get back to room2?";

		ShowQuestionEf effect = new ShowQuestionEf();
		if (result) {
			stringFactory.setString(effect.getQuestion(), questionOk);
		} else {
			stringFactory.setString(effect.getQuestion(), questionWr);
		}
		effect.addAnswer(stringFactory.getString(finalQ), null);

		effect.setUpNewInstance();

		return effect;
	}

	@Override
	public String getSceneDescription() {
		return "A scene to test show question effect";
	}

	public String getDemoName() {
		return "Show Question Scene";
	}
}

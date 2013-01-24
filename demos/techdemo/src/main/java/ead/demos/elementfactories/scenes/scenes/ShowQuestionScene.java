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

package ead.demos.elementfactories.scenes.scenes;

import ead.common.model.elements.effects.text.ShowQuestionEf;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.params.fills.ColorFill;
import ead.common.params.guievents.MouseGEv;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Caption;

public class ShowQuestionScene extends EmptyScene {

	public ShowQuestionScene() {
		this.setId("ShowQuestionScene");

		Caption c = new Caption("techDemo.ShowQuestionScene.launchquestion");
		c.setBubblePaint(ColorFill.LIGHT_GRAY);

		SceneElement element = new SceneElement(c);
		element.setPosition(10, 10);

		getSceneElements().add(element);

		ShowQuestionEf effect = new ShowQuestionEf();
		effect
				.setQuestion(new EAdString(
						"techDemo.ShowQuestionScene.question"));
		effect.addAnswer(new EAdString("techDemo.ShowQuestionScene.answer1"),
				effect);
		effect.addAnswer(new EAdString("techDemo.ShowQuestionScene.answer2"),
				null);
		effect.addAnswer(new EAdString("techDemo.ShowQuestionScene.answer3"),
				null);
		effect.addAnswer(new EAdString("techDemo.ShowQuestionScene.answer4"),
				null);

		element.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, effect);
	}

}

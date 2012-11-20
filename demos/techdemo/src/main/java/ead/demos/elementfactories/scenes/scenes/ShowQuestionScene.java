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
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.SceneElement;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.StringFactory;

public class ShowQuestionScene extends EmptyScene {

	public ShowQuestionScene() {
		SceneElement element = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(
						"Launch show question", 10, 10);

		getSceneElements().add(element);

		StringFactory stringFactory = EAdElementsFactory.getInstance()
				.getStringFactory();

		ShowQuestionEf effect = new ShowQuestionEf();
		stringFactory.setString(effect.getQuestion(),
				"A question has been made");

		effect.addAnswer(stringFactory.getString("Answer 1"), effect);
		effect.addAnswer(stringFactory.getString("Answer 2"), null);
		effect.addAnswer(stringFactory.getString("Answer 3"), null);

		effect.setUpNewInstance();

		element.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, effect);
	}

	@Override
	public String getSceneDescription() {
		return "A scene to test show question effect";
	}

	public String getDemoName() {
		return "Show Question Scene";
	}

}

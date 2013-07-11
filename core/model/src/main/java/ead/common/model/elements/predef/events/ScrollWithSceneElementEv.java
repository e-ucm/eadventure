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

package ead.common.model.elements.predef.events;

import ead.common.model.elements.EAdElement;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.operations.MathOp;
import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.scenes.SceneElement;

public class ScrollWithSceneElementEv extends SceneElementEv {

	public ScrollWithSceneElementEv(EAdElement scene, EAdElement character) {
		EAdField<Float> xElement = new BasicField<Float>(character,
				SceneElement.VAR_X);
		EAdField<Float> yElement = new BasicField<Float>(character,
				SceneElement.VAR_Y);
		EAdField<Float> xScene = new BasicField<Float>(scene,
				SceneElement.VAR_X);
		EAdField<Float> yScene = new BasicField<Float>(scene,
				SceneElement.VAR_Y);
		EAdField<Integer> widthScene = new BasicField<Integer>(scene,
				SceneElement.VAR_WIDTH);
		EAdField<Integer> heightScene = new BasicField<Integer>(scene,
				SceneElement.VAR_HEIGHT);

		// [0] = x-element
		// [1] = width
		// [2] = scene-width
		String expression = " -( ([1] - [2]) min ( 0 max ([0] - ([2] / 2 )) ))";
		EAdOperation opX = new MathOp(expression, xElement, widthScene,
				SystemFields.GAME_WIDTH);
		EAdOperation opY = new MathOp(expression, yElement, heightScene,
				SystemFields.GAME_HEIGHT);

		ChangeFieldEf effectX = new ChangeFieldEf();
		effectX.addField(xScene);
		effectX.setOperation(opX);
		ChangeFieldEf effectY = new ChangeFieldEf();
		effectY.addField(yScene);
		effectY.setOperation(opY);

		addEffect(SceneElementEvType.ALWAYS, effectX);
		addEffect(SceneElementEvType.ALWAYS, effectY);

	}

}

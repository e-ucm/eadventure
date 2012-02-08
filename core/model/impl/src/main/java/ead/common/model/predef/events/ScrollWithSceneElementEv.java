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

package ead.common.model.predef.events;

import ead.common.model.EAdElement;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEventType;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.model.elements.variables.EAdFieldImpl;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.MathOp;

public class ScrollWithSceneElementEv extends SceneElementEv {

	public ScrollWithSceneElementEv(EAdElement scene, EAdElement character) {
		this.setId("scrollWidthElement");
		EAdField<Integer> xElement = new EAdFieldImpl<Integer>(character,
				SceneElementImpl.VAR_X);
		EAdField<Integer> yElement = new EAdFieldImpl<Integer>(character,
				SceneElementImpl.VAR_Y);
		EAdField<Integer> xScene = new EAdFieldImpl<Integer>(scene,
				SceneElementImpl.VAR_X);
		EAdField<Integer> yScene = new EAdFieldImpl<Integer>(scene,
				SceneElementImpl.VAR_Y);
		EAdField<Integer> widthScene = new EAdFieldImpl<Integer>(scene,
				SceneElementImpl.VAR_WIDTH);
		EAdField<Integer> heightScene = new EAdFieldImpl<Integer>(scene,
				SceneElementImpl.VAR_HEIGHT);
		
		
		// [0] = x-element
		// [1] = width
		// [2] = scene-width
		String expression = " -( ([1] - [2]) min ( 0 max ([0] - ([2] / 2 )) ))";
		EAdOperation opX = new MathOp( expression,  xElement, widthScene, SystemFields.GAME_WIDTH  );
		EAdOperation opY = new MathOp( expression,  yElement, heightScene, SystemFields.GAME_HEIGHT );
		
		ChangeFieldEf effectX = new ChangeFieldEf( );
		effectX.addField(xScene);
		effectX.setOperation(opX);
		ChangeFieldEf effectY = new ChangeFieldEf( );
		effectY.addField(yScene);
		effectY.setOperation(opY);
		
		addEffect(SceneElementEventType.ALWAYS, effectX);
		addEffect(SceneElementEventType.ALWAYS, effectY);

	}

}
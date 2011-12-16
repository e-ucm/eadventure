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

package es.eucm.eadventure.common.predef.model.events;

import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.Comparator;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;

/**
 * This event keeps an {@link EAdSceneElement} in the window bounds
 * 
 */
public class StayInBoundsEvent extends EAdSceneElementEventImpl {

	/**
	 * 
	 * @param e
	 *            the element to stay in bounds
	 */
	public StayInBoundsEvent(EAdSceneElement e) {
		super();
		setId("stayInBoundsEvent");
		EAdField<Integer> maxX = SystemFields.GAME_WIDTH;
		EAdField<Integer> maxY = SystemFields.GAME_HEIGHT;

		EAdField<Integer> x = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_X);

		EAdField<Integer> y = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_Y);

		EAdField<Integer> left = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_LEFT);
		EAdField<Integer> top = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_TOP);
		EAdField<Integer> right = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_RIGHT);
		EAdField<Integer> bottom = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_BOTTOM);

		// Correct X Left
		String expression1 = "[0] - [1]";
		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect(x, new MathOperation(expression1, x, left));
		effect.setId("correctXLeft");
		OperationCondition c = new OperationCondition(left, 0, Comparator.LESS);
		effect.setCondition(c);

		addEffect(SceneElementEventType.ALWAYS, effect);

		// Correct X Right
		String expression2 = "[0] - ( [1] - [2] )";
		effect = new EAdChangeFieldValueEffect( x,
				new MathOperation(expression2, x, right, maxX));
		effect.setId("correctXRight");
		c = new OperationCondition(maxX, left, Comparator.LESS);
		effect.setCondition(c);
		addEffect(SceneElementEventType.ALWAYS, effect);

		// Correct Y top
		effect = new EAdChangeFieldValueEffect( y,
				new MathOperation(expression1, y, top));
		effect.setId("correctYTop");
		c = new OperationCondition(top, 0, Comparator.LESS);
		effect.setCondition(c);
		addEffect(SceneElementEventType.ALWAYS, effect);

		// Correct Y bottom
		effect = new EAdChangeFieldValueEffect( y,
				new MathOperation(expression2, y, bottom, maxY));
		effect.setId("correctXRight");
		c = new OperationCondition(maxY, bottom, Comparator.LESS);
		effect.setCondition(c);
		addEffect(SceneElementEventType.ALWAYS, effect);

	}

}

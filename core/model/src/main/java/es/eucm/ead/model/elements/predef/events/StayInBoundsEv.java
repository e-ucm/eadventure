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

package es.eucm.ead.model.elements.predef.events;

import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.conditions.enums.Comparator;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.SceneElement;

/**
 * This event keeps an {@link EAdSceneElement} in the window bounds
 * 
 */
public class StayInBoundsEv extends SceneElementEv {

	/**
	 * 
	 * @param e
	 *            the element to stay in bounds
	 */
	public StayInBoundsEv(EAdSceneElement e) {
		super();
		EAdField<Integer> maxX = SystemFields.GAME_WIDTH;
		EAdField<Integer> maxY = SystemFields.GAME_HEIGHT;

		EAdField<Float> x = new BasicField<Float>(e, SceneElement.VAR_X);

		EAdField<Float> y = new BasicField<Float>(e, SceneElement.VAR_Y);

		EAdField<Float> left = new BasicField<Float>(e, SceneElement.VAR_LEFT);
		EAdField<Float> top = new BasicField<Float>(e, SceneElement.VAR_TOP);
		EAdField<Float> right = new BasicField<Float>(e, SceneElement.VAR_RIGHT);
		EAdField<Float> bottom = new BasicField<Float>(e,
				SceneElement.VAR_BOTTOM);

		// Correct X Left
		String expression1 = "[0] - [1]";
		ChangeFieldEf effect = new ChangeFieldEf(x, new MathOp(expression1, x,
				left));
		OperationCond c = new OperationCond(left, 0, Comparator.LESS);
		effect.setCondition(c);

		addEffect(SceneElementEvType.ALWAYS, effect);

		// Correct X Right
		String expression2 = "[0] - ( [1] - [2] )";
		effect = new ChangeFieldEf(x, new MathOp(expression2, x, right, maxX));
		c = new OperationCond(maxX, left, Comparator.LESS);
		effect.setCondition(c);
		addEffect(SceneElementEvType.ALWAYS, effect);

		// Correct Y top
		effect = new ChangeFieldEf(y, new MathOp(expression1, y, top));
		c = new OperationCond(top, 0, Comparator.LESS);
		effect.setCondition(c);
		addEffect(SceneElementEvType.ALWAYS, effect);

		// Correct Y bottom
		effect = new ChangeFieldEf(y, new MathOp(expression2, y, bottom, maxY));
		c = new OperationCond(maxY, bottom, Comparator.LESS);
		effect.setCondition(c);
		addEffect(SceneElementEvType.ALWAYS, effect);

	}

}

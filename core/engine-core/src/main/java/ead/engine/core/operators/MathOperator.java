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

package ead.engine.core.operators;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.operations.MathOp;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.game.interfaces.ValueMap;
import ead.tools.MathEvaluator;

/**
 * Calculates results for {@link MathOp}. The result returned by
 * {@link MathOp#operate(ValueMap, MathOp)} is always a Float
 * object
 * 
 * 
 */
@Singleton
public class MathOperator implements Operator<MathOp> {

	/**
	 * Math evaluator
	 */
	private MathEvaluator evaluator = new MathEvaluator();

	private GameState gameState;

	@Inject
	public MathOperator(GameState valueMap) {
		this.gameState = valueMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S operate(Class<S> clazz, MathOp operation) {
		evaluator.setExpression(operation.getExpression(), gameState, operation
				.getOperations());

		if (clazz.equals(Integer.class) || operation.isResultAsInteger()) {
			Float f = evaluator.getValue();
			return (S) new Integer(f.intValue());
		} else {
			return (S) evaluator.getValue();
		}
	}

}

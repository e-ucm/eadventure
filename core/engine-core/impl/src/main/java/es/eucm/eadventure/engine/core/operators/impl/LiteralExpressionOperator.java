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

package es.eucm.eadventure.engine.core.operators.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.operator.Operator;
import es.eucm.eadventure.engine.core.operators.impl.util.MathEvaluator;

/**
 * Calculates results for {@link LiteralExpressionOperation}. The result
 * returned by
 * {@link LiteralExpressionOperation#operate(ValueMap, LiteralExpressionOperation)}
 * is always a Float object
 * 
 * 
 */
@Singleton
public class LiteralExpressionOperator implements
		Operator<LiteralExpressionOperation> {

	/**
	 * Math evaluator
	 */
	private static final MathEvaluator evaluator = new MathEvaluator();

	private ValueMap valueMap;

	@Inject
	public LiteralExpressionOperator(ValueMap valueMap) {
		this.valueMap = valueMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S operate(Class<S> clazz, LiteralExpressionOperation operation) {
		evaluator.setExpression(operation.getExpression(), valueMap,
				operation.getVarList());

		if (clazz.equals(Integer.class))
			return (S) new Integer(evaluator.getValue().intValue());
		else
			return (S) evaluator.getValue();
	}

}

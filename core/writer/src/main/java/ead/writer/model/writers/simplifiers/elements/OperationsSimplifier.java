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

package ead.writer.model.writers.simplifiers.elements;

import es.eucm.ead.model.elements.operations.EAdOperation;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.tools.MathEvaluator;
import ead.writer.model.writers.simplifiers.ObjectSimplifier;

public class OperationsSimplifier implements ObjectSimplifier<EAdOperation> {

	/**
	 * MathEvaluator to help simplify MathOp with no values
	 */
	private MathEvaluator mathEvaluator;

	public OperationsSimplifier() {
		mathEvaluator = new MathEvaluator();
	}

	public Object simplify(EAdOperation operation) {
		// If it is math expression with no operands, simplify to a ValueOp
		if (operation instanceof MathOp
				&& !((MathOp) operation).getExpression().contains("[")) {
			String expression = ((MathOp) operation).getExpression();
			mathEvaluator.setExpression(expression, null, null);
			Float value = mathEvaluator.getValue();
			operation = new ValueOp(
					((MathOp) operation).isResultAsInteger() ? value.intValue()
							: value);
		}

		return operation;
	}

	@Override
	public void clear() {
		// Do nothing

	}

}

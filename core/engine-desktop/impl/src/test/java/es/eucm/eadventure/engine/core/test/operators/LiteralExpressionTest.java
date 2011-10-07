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

package es.eucm.eadventure.engine.core.test.operators;

import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;

public class LiteralExpressionTest extends
		OperatorsTest<MathOperation> {



	@Override
	public void generateOperations() {
		
		EAdField<Integer> x = new EAdFieldImpl<Integer>(null,
				new EAdVarDefImpl<Integer>("x", Integer.class, 1));
		EAdField<Float> y = new EAdFieldImpl<Float>(null,
				new EAdVarDefImpl<Float>("y", Float.class, 0.5f));
		EAdField<Float> z = new EAdFieldImpl<Float>(null,
				new EAdVarDefImpl<Float>("z", Float.class, 4.2f));
		EAdField<Double> w = new EAdFieldImpl<Double>(null,
				new EAdVarDefImpl<Double>("w", Double.class, 10928408.868979));

		MathOperation[] operations = new MathOperation[] {
				new MathOperation("1", "3 + 5 + 8"),
				new MathOperation("[0] + [1] + [2]", x, y, z),
				new MathOperation("1", "[0] * [1] / [2]", x, y, z),
				new MathOperation("1",
						"(5  + [0] ) * [1] * ( [2] - 4 ) ", x, y, z),
				new MathOperation(
						"1",
						"cos( 4 + [2] ) - (5  + [0] ) * [1] * sqrt( [2] - 4 ) ",
						x, y, z),
				new MathOperation("1", " 7 * somestring "),
				new MathOperation("1", " somestring + 2"),
				new MathOperation("1", " [0]^[1]", w, z),
				new MathOperation("1", " 1 / 0 "),
				new MathOperation("1", "200 max 300"),
				new MathOperation(
						"1",
						"[0] + ((400*(1+[1]) - [0]*[1]) max (300*(1+[2]) - [3]*[2]))*[1]",
						x, x, x, x),
				new MathOperation("acos( ( [2] - [0] ) / sqrt( sqr( [2] - [0] ) + sqr( [3] - [1] ) ) )", x, y, z, x)

		};

		int xV = x.getVarDefinition().getInitialValue();
		float yV = y.getVarDefinition().getInitialValue();
		float zV = z.getVarDefinition().getInitialValue();
		double wV = w.getVarDefinition().getInitialValue();
		Object results[] = new Object[] { 
				new Integer(3 + 5 + 8),
				new Float(xV + yV + zV), 
				new Float(xV * yV / zV),
				new Float((5 + xV) * yV * (zV - 4)),
				new Float(Math.cos(4 + zV) - (5 + xV) * yV * Math.sqrt(zV - 4)),
				new Float(0), 
				new Float(2), 
				new Float(Math.pow(wV, zV)),
				new Float(Float.POSITIVE_INFINITY), 
				new Integer(300),
				new Integer(800),
				new Float(Math.acos( ( zV - xV ) / Math.sqrt( ( zV - xV )*( zV - xV ) + ( xV - yV )*( xV - yV ) ) ))

		};

		for (int i = 0; i < operations.length; i++) {
			this.addOperationTest(operations[i], results[i]);
		}

	}
}

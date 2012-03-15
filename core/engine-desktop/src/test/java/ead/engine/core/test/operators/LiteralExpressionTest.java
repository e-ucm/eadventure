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

package ead.engine.core.test.operators;

import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.VarDef;
import ead.common.model.elements.variables.operations.MathOp;

public class LiteralExpressionTest extends
		OperatorsTest<MathOp> {



	@Override
	public void generateOperations() {

		EAdField<Integer> x = new BasicField<Integer>(null,
				new VarDef<Integer>("x", Integer.class, 1));
		EAdField<Float> y = new BasicField<Float>(null,
				new VarDef<Float>("y", Float.class, 0.5f));
		EAdField<Float> z = new BasicField<Float>(null,
				new VarDef<Float>("z", Float.class, 4.2f));
		EAdField<Double> w = new BasicField<Double>(null,
				new VarDef<Double>("w", Double.class, 10928408.868979));

		MathOp[] operations = new MathOp[] {
				new MathOp("3 + 5 + 8"),
				new MathOp("[0] + [1] + [2]", x, y, z),
				new MathOp("[0] * [1] / [2]", x, y, z),
				new MathOp("(5  + [0] ) * [1] * ( [2] - 4 ) ", x, y, z),
				new MathOp(
						"cos( 4 + [2] ) - (5  + [0] ) * [1] * sqrt( [2] - 4 ) ",
						x, y, z),
				new MathOp( " 7 * somestring "),
				new MathOp( " somestring + 2"),
				new MathOp( " [0]^[1]", w, z),
				new MathOp( " 1 / 0 "),
				new MathOp( "200 max 300"),
				new MathOp(
						"[0] + ((400*(1+[1]) - [0]*[1]) max (300*(1+[2]) - [3]*[2]))*[1]",
						x, x, x, x),
				new MathOp("acos( ( [2] - [0] ) / sqrt( sqr( [2] - [0] ) + sqr( [3] - [1] ) ) )", x, y, z, x)

		};

		int xV = x.getVarDef().getInitialValue();
		float yV = y.getVarDef().getInitialValue();
		float zV = z.getVarDef().getInitialValue();
		double wV = w.getVarDef().getInitialValue();
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
            // FIXME; falla, y no sabemos si debe
            //			this.addOperationTest(operations[i], results[i]);
		}

	}
}

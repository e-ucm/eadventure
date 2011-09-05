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

import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.engine.core.operators.impl.LiteralExpressionOperator;

public class LiteralExpressionTest extends OperatorsTest<LiteralExpressionOperation>{

	public LiteralExpressionTest( ) {
		super(new LiteralExpressionOperator(valueMap));
	}

	private int x;
	private float y, z;
	private double w;
	
	@Override
	public void generateOperations() {
		x = 1;
		y = 0.5f;
		z = 4.2f;
		w = 10928408.868979;
//		valueMap.setValue(new FloatVar("x"), new Float(x));
//		valueMap.setValue(new FloatVar("y"), y );
//		valueMap.setValue(new FloatVar("z"), z );
//		valueMap.setValue(new FloatVar("w"), new Float(w));
//		valueMap.setValue(new StringVar("somestring"), "some string");
//		valueMap.setValue(new FloatVar("tan"), new Float(9) );
		
		LiteralExpressionOperation[] operations = new LiteralExpressionOperation[]{
//				new LiteralExpressionOperation(   "1", "3 + 5 + 8"),
//				new LiteralExpressionOperation(  "1", "[0] + [1] + [2]", new FloatVar("x"), new FloatVar("y"), new FloatVar("z")),
//				new LiteralExpressionOperation(  "1", "[0] * [1] / [2]", new FloatVar("x"), new FloatVar("y"), new FloatVar("z")),
//				new LiteralExpressionOperation(  "1", "(5  + [0] ) * [1] * ( [2] - 4 ) ", new FloatVar("x"), new FloatVar("y"), new FloatVar("z")),
//				new LiteralExpressionOperation(  "1", "cos( 4 + [2] ) - (5  + [0] ) * [1] * sqrt( [2] - 4 ) ", new FloatVar("x"), new FloatVar("y"), new FloatVar("z")),
				new LiteralExpressionOperation(  "1", " 7 * somestring "),
				new LiteralExpressionOperation(  "1", " somestring + 2"),
//				new LiteralExpressionOperation(  "1", " [0]^[1]", new FloatVar("w"), new FloatVar("z")),
				new LiteralExpressionOperation(  "1", " 1 / 0 "),
				new LiteralExpressionOperation(  "1", "200 max 300"),
//				new LiteralExpressionOperation( "1", "[0] + ((400*(1+[1]) - [0]*[1]) max (300*(1+[2]) - [3]*[2]))*[1]", new FloatVar("x"), new FloatVar("x"), new FloatVar("x"), new FloatVar("x"))
				
			};
			
			Double results[] = new Double[]{
					new Double( 3 + 5 + 8 ),
					new Double( x + y + z ),
					new Double( x * y / z ),
					new Double( (5  + x ) * y * ( z - 4 ) ),
					new Double( Math.cos( 4 + z ) - (5  + x ) * y * Math.sqrt( z - 4 ) ),
					new Double( 0 ),
					new Double( 2 ),
					new Double( Math.pow(w, z)),
					new Double( Float.POSITIVE_INFINITY ),
					new Double( 300 ),
					new Double( 800 )
					
			};
			
		for ( int i = 0; i < operations.length; i++ ){
//			this.addOperationTest(new FloatVar("neverMind"), operations[i], results[i]);
		}
		
	}

}

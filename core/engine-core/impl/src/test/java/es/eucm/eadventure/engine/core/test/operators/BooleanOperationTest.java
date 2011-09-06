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

import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.engine.core.operators.impl.BooleanOperator;

public class BooleanOperationTest extends OperatorsTest<BooleanOperation> {

	private EAdVarDef<Boolean> someBoolean = new EAdVarDefImpl<Boolean>(
			"SomeBoolean", Boolean.class, Boolean.FALSE);
	private EAdVarDef<Boolean> someBooleanTrue = new EAdVarDefImpl<Boolean>(
			"SomeBoolean", Boolean.class, Boolean.FALSE);
	private EAdVarDef<Boolean> someBooleanFalse = new EAdVarDefImpl<Boolean>(
			"SomeBoolean", Boolean.class, Boolean.FALSE);
	private EAdVarDef<Integer> someInteger = new EAdVarDefImpl<Integer>(
			"SomeBoolean", Integer.class, 0);

	public BooleanOperationTest() {
		super(new BooleanOperator(null));
	}

	@Override
	public void generateOperations() {
		BooleanOperation falseOp = BooleanOperation.FALSE_OP;

		BooleanOperation trueOp = BooleanOperation.TRUE_OP;

		super.addOperationTest(someBoolean, falseOp,
				Boolean.FALSE);
		super.addOperationTest(someBooleanTrue,
				falseOp, Boolean.FALSE);
		super.addOperationTest(someBooleanFalse,
				falseOp, Boolean.FALSE);
		super.addOperationTest(someBoolean, trueOp,
				Boolean.TRUE);
		super.addOperationTest(someBooleanTrue,
				trueOp, Boolean.TRUE);
		super.addOperationTest(someBooleanFalse,
				trueOp, Boolean.TRUE);
		super.addOperationTest(someInteger, trueOp,
				Boolean.TRUE);
		super.addOperationTest(someInteger, falseOp,
				Boolean.FALSE);

	}

}

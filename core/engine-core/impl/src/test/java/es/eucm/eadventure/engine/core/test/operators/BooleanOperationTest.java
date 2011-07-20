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

import org.junit.Before;

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;
import es.eucm.eadventure.engine.core.operators.impl.BooleanOperator;

public class BooleanOperationTest extends OperatorsTest<BooleanOperation> {

	public BooleanOperationTest() {
		super(new BooleanOperator(valueMap, null));
	}

	@Before
	public void setUp() {
		valueMap.setValue(new BooleanVar("SomeBoolean"), Boolean.FALSE);
		valueMap.setValue(new BooleanVar("SomeBooleanTrue"), Boolean.TRUE);
		valueMap.setValue(new BooleanVar("SomeBooleanFalse"), Boolean.FALSE);
		valueMap.setValue(new IntegerVar("Integer"), new Integer(10));
	}

	@Override
	public void generateOperations() {
		BooleanOperation falseOp = new BooleanOperation( "neverMind", EmptyCondition.FALSE_EMPTY_CONDITION);

		BooleanOperation trueOp = new BooleanOperation( "neverMind", EmptyCondition.TRUE_EMPTY_CONDITION);

		super.addOperationTest(new BooleanVar("SomeBoolean"), falseOp, Boolean.FALSE);
		super.addOperationTest(new BooleanVar("SomeBooleanTrue"), falseOp, Boolean.FALSE);
		super.addOperationTest(new BooleanVar("SomeBooleanFalse"), falseOp, Boolean.FALSE);
		super.addOperationTest(new BooleanVar("SomeBoolean"), trueOp, Boolean.TRUE);
		super.addOperationTest(new BooleanVar("SomeBooleanTrue"), trueOp, Boolean.TRUE);
		super.addOperationTest(new BooleanVar("SomeBooleanFalse"), trueOp, Boolean.TRUE);
		super.addOperationTest(new BooleanVar("Integer"), trueOp, Boolean.TRUE);
		super.addOperationTest(new BooleanVar("Integer"), falseOp, Boolean.FALSE);
		
	}

}

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

package es.eucm.eadventure.engine.core.test.effects;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.model.variables.impl.vars.FloatVar;
import es.eucm.eadventure.engine.core.OperatorFactory;
import es.eucm.eadventure.engine.core.ValueMap;

public class ChangeVariableTest extends EffectTest {
	
	private ValueMap valueMap;
	
	private OperatorFactory operatorFactory;
	
	@Before
	public void setUp( ){
		super.setUp();
		valueMap = testEngine.valueMap;
		operatorFactory = testEngine.operatorFactory;
		valueMap.setValue(new FloatVar("var1"), new Float( 8.0f ));
	}
	
	@Test
	public void testChangeVariable( ){
		EAdChangeVarValueEffect changeVar = new EAdChangeVarValueEffect( "cv" );
		changeVar.addVar(new FloatVar("var1"));
		// Change var value without operation
		testEngine.addEffect(changeVar);
		assertEquals( testEngine.getEffects().size(), 1 );
		testEngine.update();
		assertEquals( testEngine.getEffects().size(), 0 );
		assertEquals( (Float) valueMap.getValue(new FloatVar("var1")), new Float(8.0f) );
		
		EAdOperation operation = new LiteralExpressionOperation( "l", "7 + 8 ");
		changeVar.setOperation( operation );
		testEngine.addEffect(changeVar);
		assertEquals( testEngine.getEffects().size(), 1 );
		testEngine.update();
		assertEquals( testEngine.getEffects().size(), 0 );
		assertEquals( valueMap.getValue(new FloatVar("var1")), operatorFactory.operate(new FloatVar("var1"), operation));
		
		// Change value of a non-exisitng variable
		changeVar.addVar(new FloatVar("non-existing var"));
		testEngine.addEffect(changeVar);
		assertEquals( testEngine.getEffects().size(), 1 );
		testEngine.update();
		assertEquals( testEngine.getEffects().size(), 0 );
		assertEquals( valueMap.getValue(new FloatVar("non-existing var")), null );
		
	}

}

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

package es.eucm.eadventure.engine.core.test.evaluators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.eucm.eadventure.common.model.elements.conditions.ANDCond;
import es.eucm.eadventure.common.model.elements.conditions.EmptyCond;
import es.eucm.eadventure.common.model.elements.conditions.NOTCond;
import es.eucm.eadventure.common.model.elements.conditions.ORCond;
import es.eucm.eadventure.common.model.elements.conditions.enums.EmptyConditionValue;
import es.eucm.eadventure.engine.core.evaluators.impl.EvaluatorFactoryImpl;
import es.eucm.eadventure.engine.core.game.ValueMap;
import es.eucm.eadventure.engine.core.impl.ValueMapImpl;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.operators.impl.OperatorFactoryImpl;
import es.eucm.eadventure.engine.core.platform.impl.JavaReflectionProvider;

public class EvaluatorTest {
	
	protected EvaluatorFactoryImpl evaluator;	

	protected EmptyCond cTrue = new EmptyCond(EmptyConditionValue.TRUE);
	
	protected EmptyCond cFalse = new EmptyCond(EmptyConditionValue.FALSE);
	
	@Before
	public void setUp(){
		evaluator = new EvaluatorFactoryImpl(new JavaReflectionProvider());
		OperatorFactory operatorFactory = new OperatorFactoryImpl(new JavaReflectionProvider());
		ValueMap valueMap = new ValueMapImpl(new JavaReflectionProvider(), operatorFactory, evaluator);
		operatorFactory.install(valueMap, evaluator);
		evaluator.install(valueMap, operatorFactory);
	}

	@Test
	public void test() {
		assertTrue(evaluator.evaluate(new ANDCond(cTrue, cTrue)));
		assertFalse(evaluator.evaluate(new ANDCond(cTrue, cFalse)));
		assertFalse(evaluator.evaluate(new ANDCond(cFalse, cTrue)));
		assertFalse(evaluator.evaluate(new ANDCond(cFalse, cFalse)));

		assertTrue(evaluator.evaluate(new ORCond(cTrue, cTrue)));
		assertTrue(evaluator.evaluate(new ORCond(cTrue, cFalse)));
		assertTrue(evaluator.evaluate(new ORCond(cFalse, cTrue)));
		assertFalse(evaluator.evaluate(new ORCond(cFalse, cFalse)));

		assertTrue(evaluator.evaluate(new NOTCond(cFalse)));
		assertFalse(evaluator.evaluate(new NOTCond(cTrue)));

		assertTrue(evaluator.evaluate(cTrue));
		assertFalse(evaluator.evaluate(cFalse));
	}

}

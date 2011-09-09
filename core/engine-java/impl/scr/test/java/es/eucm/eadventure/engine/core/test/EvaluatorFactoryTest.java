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

package es.eucm.eadventure.engine.core.test;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.impl.ValueMapImpl;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.impl.modules.EvaluatorFactoryModule;
import junit.framework.TestCase;

public class EvaluatorFactoryTest extends TestCase {

	private Injector injector;
	
	private ValueMap valueMap;
	
	private class TestModule extends BasicGameModule {

		@Override
		protected void configure() {
			bind(ValueMap.class).to(ValueMapImpl.class);
			install(new EvaluatorFactoryModule());
		}
		
	}
	
	@Override
	public void setUp() {
		injector = Guice.createInjector(new TestModule());
		valueMap = injector.getInstance(ValueMap.class);

//		valueMap.setValue(new BooleanVar("testBool"), Boolean.TRUE);
//		valueMap.setValue(new IntegerVar("testInt"), new Integer(20));
	}

	@Test
	public void testEvaluador1() {
		EvaluatorFactory factory = injector.getInstance(EvaluatorFactory.class);
		
//		ANDCondition c = new ANDCondition(new FlagCondition(new BooleanVar("testBool"), FlagCondition.Value.ACTIVE));
//		c.addCondition(new VarValCondition(new IntegerVar("testInt"), 20, VarCondition.Operator.EQUAL));

//		assertEquals(true, factory.evaluate(c));
	}
	
}

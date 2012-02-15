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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.EAdOperation;
import ead.engine.core.TestUtil;
import ead.engine.core.game.ValueMap;
import ead.engine.core.operator.OperatorFactory;

public abstract class OperatorsTest<T extends EAdOperation> {

	protected ValueMap valueMap;
	protected OperatorFactory operatorFactory;

	private ArrayList<T> operations = new ArrayList<T>();
	private ArrayList<Object> results = new ArrayList<Object>();
	protected SceneElement dummyElement = new SceneElement();

	public OperatorsTest() {
		operatorFactory = TestUtil.getInjector().getInstance(OperatorFactory.class);
		valueMap = TestUtil.getInjector().getInstance(ValueMap.class);
		generateOperations();
	}

	public abstract void generateOperations();

	public void addOperationTest(T operation, Object result) {
		operations.add(operation);
		results.add(result);
	}

	@Test
	public void testOperations() {
		int i = 0;
		for (T op : operations) {
			Object result = results.get(i);
			Object value = operatorFactory.operate(result.getClass(), op);
			assertEquals(value, result);
			i++;
		}
	}

}

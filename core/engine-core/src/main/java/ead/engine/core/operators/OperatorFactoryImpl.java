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

package ead.engine.core.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.operations.EAdOperation;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.factories.mapproviders.OperatorsMapProvider;
import ead.engine.core.game.ValueMap;
import ead.tools.AbstractFactory;
import ead.tools.reflection.ReflectionProvider;

@Singleton
public class OperatorFactoryImpl extends AbstractFactory<Operator<?>> implements
		OperatorFactory {

	private static final Logger logger = LoggerFactory
			.getLogger("Operator Factory");

	@Inject
	public OperatorFactoryImpl(ReflectionProvider interfacesProvider) {
		super(null, interfacesProvider);
	}

	public void init(ValueMap valueMap, EvaluatorFactory evaluatorFactory) {
		setMap(new OperatorsMapProvider(this, evaluatorFactory, valueMap,
				reflectionProvider));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EAdOperation, S> S operate(Class<S> clazz, T operation) {
		if (operation == null) {
			logger.error("Null operation attempted: null returned as class {}",
					clazz);
			return null;
		}
		Operator<T> operator = (Operator<T>) get(operation.getClass());
		return operator.operate(clazz, operation);
	}

}
